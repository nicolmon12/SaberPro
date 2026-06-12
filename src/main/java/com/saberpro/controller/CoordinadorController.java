package com.saberpro.controller;

import com.saberpro.model.Director;
import com.saberpro.model.Estudiante;
import com.saberpro.model.Facultad;
import com.saberpro.model.PagoSaberPro;
import com.saberpro.model.ResultadoSaberPro;
import com.saberpro.repository.DirectorRepository;
import com.saberpro.repository.EstudianteRepository;
import com.saberpro.repository.FacultadRepository;
import com.saberpro.repository.PagoRepository;
import com.saberpro.repository.ResolucionRepository;
import com.saberpro.repository.ResultadoRepository;
import com.saberpro.service.BeneficioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/coordinador")
public class CoordinadorController {

    @Autowired private DirectorRepository directorRepository;
    @Autowired private EstudianteRepository estudianteRepository;
    @Autowired private FacultadRepository facultadRepository;
    @Autowired private PagoRepository pagoRepository;
    @Autowired private ResultadoRepository resultadoRepository;
    @Autowired private ResolucionRepository resolucionRepository;
    @Autowired private BeneficioService beneficioService;
    @Autowired private PasswordEncoder passwordEncoder;

    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication authentication) {
        long total = estudianteRepository.count();
        long conRes = resultadoRepository.count();
        model.addAttribute("totalEstudiantes", total);
        model.addAttribute("conResultados", conRes);
        model.addAttribute("sinResultados", total - conRes);
        model.addAttribute("pagosPendientes", pagoRepository.findByEstado(PagoSaberPro.EstadoPago.PENDIENTE).size());
        model.addAttribute("promedioPuntajes", resultadoRepository.getPromedioPuntajes());

        // Datos del coordinador actual
        if (authentication != null) {
            String email = authentication.getName();
            directorRepository.findByEmail(email).ifPresent(d -> {
                model.addAttribute("coordinadorNombre", d.getNombre() + " " + d.getApellido());
                model.addAttribute("coordinadorFacultad",
                    d.getFacultad() != null ? d.getFacultad().getNombre() : "Sin facultad");
            });
        }
        if (!model.containsAttribute("coordinadorNombre")) {
            model.addAttribute("coordinadorNombre", "Coordinador");
            model.addAttribute("coordinadorFacultad", "Saber Pro");
        }
        return "coordinador/dashboard";
    }

    // ── CRUD ESTUDIANTES ────────────────────────────────────────────────────
    @GetMapping("/estudiantes")
    public String estudiantes(Model model) {
        model.addAttribute("estudiantes", estudianteRepository.findAll());
        model.addAttribute("facultades", facultadRepository.findAll());
        return "coordinador/estudiantes";
    }

    @PostMapping("/estudiantes/guardar")
    public String guardarEstudiante(
            @RequestParam String numeroDocumento,
            @RequestParam String primerNombre,
            @RequestParam(required = false, defaultValue = "") String segundoNombre,
            @RequestParam String primerApellido,
            @RequestParam(required = false, defaultValue = "") String segundoApellido,
            @RequestParam String email,
            @RequestParam(required = false, defaultValue = "") String telefono,
            @RequestParam Integer semestre,
            @RequestParam Long facultadId) {

        Estudiante e = new Estudiante();
        e.setNumeroDocumento(numeroDocumento);
        e.setPrimerNombre(primerNombre);
        e.setSegundoNombre(segundoNombre);
        e.setPrimerApellido(primerApellido);
        e.setSegundoApellido(segundoApellido);
        e.setEmail(email);
        e.setTelefono(telefono);
        e.setSemestre(semestre);
        e.setPassword(passwordEncoder.encode(numeroDocumento));
        Facultad facultad = facultadRepository.findById(facultadId).orElse(null);
        e.setFacultad(facultad);
        estudianteRepository.save(e);
        return "redirect:/coordinador/estudiantes";
    }

    @GetMapping("/estudiantes/editar/{id}")
    public String editarEstudiante(@PathVariable Long id, Model model) {
        model.addAttribute("estudiante", estudianteRepository.findById(id).orElse(null));
        model.addAttribute("facultades", facultadRepository.findAll());
        return "coordinador/editar-estudiantes";
    }

    @PostMapping("/estudiantes/actualizar")
    public String actualizarEstudiante(
            @RequestParam Long id,
            @RequestParam String numeroDocumento,
            @RequestParam String primerNombre,
            @RequestParam String primerApellido,
            @RequestParam String email,
            @RequestParam Integer semestre) {

        Estudiante existente = estudianteRepository.findById(id).orElse(null);
        if (existente != null) {
            existente.setNumeroDocumento(numeroDocumento);
            existente.setPrimerNombre(primerNombre);
            existente.setPrimerApellido(primerApellido);
            existente.setEmail(email);
            existente.setSemestre(semestre);
            estudianteRepository.save(existente);
        }
        return "redirect:/coordinador/estudiantes";
    }

    @GetMapping("/estudiantes/eliminar/{id}")
    public String eliminarEstudiante(@PathVariable Long id) {
        estudianteRepository.deleteById(id);
        return "redirect:/coordinador/estudiantes";
    }

    // ── APROBAR PAGOS ───────────────────────────────────────────────────────
    @GetMapping("/aprobar-pagos")
    public String aprobarPagos(Model model) {
        model.addAttribute("pagos", pagoRepository.findByEstado(PagoSaberPro.EstadoPago.PENDIENTE));
        return "coordinador/aprobar-pagos";
    }

    @PostMapping("/aprobar-pago/{id}")
    public String aprobarPago(@PathVariable Long id) {
        PagoSaberPro pago = pagoRepository.findById(id).orElse(null);
        if (pago != null) {
            pago.setEstado(PagoSaberPro.EstadoPago.APROBADO);
            pagoRepository.save(pago);
            Estudiante estudiante = pago.getEstudiante();
            estudiante.setAprobadoSaberPro(true);
            estudianteRepository.save(estudiante);
        }
        return "redirect:/coordinador/aprobar-pagos";
    }

    // ── INFORMES ────────────────────────────────────────────────────────────
    @GetMapping("/informe-general")
    public String informeGeneral(Model model, Authentication authentication) {
        List<Estudiante> estudiantes = estudianteRepository.findAll();
        long activos = estudiantes.stream().filter(e -> Boolean.TRUE.equals(e.getAprobadoSaberPro())).count();        long conRes     = resultadoRepository.count();
        model.addAttribute("estudiantes",           estudiantes);
        model.addAttribute("estudiantesActivos",    activos);
        model.addAttribute("estudiantesPendientes", estudiantes.size() - activos);
        model.addAttribute("conResultados",         conRes);
        model.addAttribute("fechaHoy",              java.time.LocalDate.now().toString());
        if (authentication != null) {
            directorRepository.findByEmail(authentication.getName()).ifPresent(d -> {
                model.addAttribute("coordinadorNombre", d.getNombre() + " " + d.getApellido());
                model.addAttribute("coordinadorFacultad",
                    d.getFacultad() != null ? d.getFacultad().getNombre() : "Sin facultad");
            });
        }
        if (!model.containsAttribute("coordinadorNombre")) {
            model.addAttribute("coordinadorNombre", "Coordinador");
            model.addAttribute("coordinadorFacultad", "Saber Pro");
        }
        return "coordinador/informe-general";
    }

    /** Informe detallado de alumnos filtrado por facultad/programa */
    @GetMapping("/informe-alumno")
    public String informeAlumno(
            @RequestParam(required = false) Long facultadId,
            Model model) {

        List<Estudiante> estudiantes = estudianteRepository.findAll();

        // Filtrar por facultad si se seleccionó una
        if (facultadId != null) {
            estudiantes = estudiantes.stream()
                .filter(e -> e.getFacultad() != null && facultadId.equals(e.getFacultad().getId()))
                .collect(Collectors.toList());
        }

        // Solo incluir estudiantes que tengan resultados
        List<Estudiante> conResultados = estudiantes.stream()
            .filter(e -> e.getResultados() != null && !e.getResultados().isEmpty())
            .collect(Collectors.toList());

        // Precompute beneficio per estudiante to avoid calling service from Thymeleaf
        java.util.Map<Long, String> beneficiosMap = new java.util.HashMap<>();
        for (Estudiante e : conResultados) {
            if (e.getResultados().get(0).getPuntajeTotal() != null) {
                String tipo = (e.getFacultad() != null
                        && e.getFacultad().getNombre() != null
                        && e.getFacultad().getNombre().toLowerCase().contains("tecnolog"))
                        ? "TECNOLOGIA" : "INGENIERIA";
                String ben = beneficioService.obtenerBeneficio(
                        e.getResultados().get(0).getPuntajeTotal(), tipo);
                beneficiosMap.put(e.getId(), ben);
            }
        }
        model.addAttribute("estudiantes", conResultados);
        model.addAttribute("facultades", facultadRepository.findAll());
        model.addAttribute("facultadFiltro", facultadId);
        model.addAttribute("beneficiosMap", beneficiosMap);
        return "coordinador/informe-alumno";
    }

    /** Página de detalle individual de un estudiante */
    @GetMapping("/informe-alumno/{id}")
    public String detalleEstudiante(@PathVariable Long id, Model model) {
        Estudiante estudiante = estudianteRepository.findById(id).orElse(null);
        if (estudiante == null) return "redirect:/coordinador/informe-alumno";

        model.addAttribute("estudiante", estudiante);

        if (estudiante.getResultados() != null && !estudiante.getResultados().isEmpty()) {
            ResultadoSaberPro resultado = estudiante.getResultados().get(0);
            model.addAttribute("resultado", resultado);

            String tipo = (estudiante.getFacultad() != null
                    && estudiante.getFacultad().getNombre() != null
                    && estudiante.getFacultad().getNombre().toLowerCase().contains("tecnolog"))
                    ? "TECNOLOGIA" : "INGENIERIA";

            com.saberpro.model.ResolucionBeneficio resolucion =
                    beneficioService.obtenerResolucionCompleta(resultado.getPuntajeTotal(), tipo);
            model.addAttribute("resolucion", resolucion);
            model.addAttribute("beneficio", resolucion != null ? resolucion.getBeneficio() : "Sin beneficio definido");

            // Calcular nota de trabajo de grado y % beca según puntaje
            if (resultado.getPuntajeTotal() != null && resolucion != null) {
                String ben = resolucion.getBeneficio().toLowerCase();
                // Nota trabajo de grado
                String notaTrabajo;
                if (ben.contains("aprobado") || ben.contains("100%") || resultado.getPuntajeTotal() >= 200) {
                    notaTrabajo = "5.0 (Exonerado)";
                } else if (ben.contains("repetir") || resultado.getPuntajeTotal() < 120) {
                    notaTrabajo = "N/A";
                } else {
                    // Calcular proporcionalmente: puntaje 120=1.0 puntaje 199=4.9
                    double puntaje = resultado.getPuntajeTotal();
                    double nota = 1.0 + ((puntaje - 120.0) / (199.0 - 120.0)) * (4.9 - 1.0);
                    notaTrabajo = String.format("%.1f", nota);
                }
                // % beca derechos de grado
                String becaPorcentaje;
                if (ben.contains("100%") || ben.contains("aprobado")) {
                    becaPorcentaje = "100%";
                } else if (ben.contains("50%")) {
                    becaPorcentaje = "50%";
                } else if (ben.contains("25%")) {
                    becaPorcentaje = "25%";
                } else {
                    becaPorcentaje = "0%";
                }
                model.addAttribute("notaTrabajo", notaTrabajo);
                model.addAttribute("becaPorcentaje", becaPorcentaje);
            }
        }
        return "coordinador/detalle-alumno";
    }

    /** Informe de beneficios de todos los estudiantes */
    @GetMapping("/informe-beneficios")
    public String informeBeneficios(Model model) {
        List<Estudiante> todos = estudianteRepository.findAll();
        List<java.util.Map<String, Object>> estudiantesConBeneficio = new java.util.ArrayList<>();

        for (Estudiante e : todos) {
            if (e.getResultados() == null || e.getResultados().isEmpty()) continue;
            ResultadoSaberPro r = e.getResultados().get(0);
            if (r.getPuntajeTotal() == null) continue;
            String tipo = (e.getFacultad() != null
                    && e.getFacultad().getNombre() != null
                    && e.getFacultad().getNombre().toLowerCase().contains("tecnolog"))
                    ? "TECNOLOGIA" : "INGENIERIA";
            com.saberpro.model.ResolucionBeneficio res =
                    beneficioService.obtenerResolucionCompleta(r.getPuntajeTotal(), tipo);
            if (res == null) continue;
            String benLower = res.getBeneficio().toLowerCase();
            if (benLower.contains("repetir")) continue;

            java.util.Map<String, Object> item = new java.util.HashMap<>();
            item.put("estudiante", e);
            item.put("resultado", r);
            item.put("resolucion", res);
            item.put("programa", e.getFacultad() != null ? e.getFacultad().getNombre() : "\u2014");
            String notaTrabajo;
            if (benLower.contains("aprobado") || r.getPuntajeTotal() >= 200) {
                notaTrabajo = "5.0";
            } else {
                double nota = 1.0 + ((r.getPuntajeTotal() - 120.0) / (199.0 - 120.0)) * (4.9 - 1.0);
                notaTrabajo = String.format("%.1f", nota);
            }
            String beca = benLower.contains("100%") || benLower.contains("aprobado") ? "100%" :
                          benLower.contains("50%") ? "50%" : benLower.contains("25%") ? "25%" : "0%";
            item.put("notaTrabajo", notaTrabajo);
            item.put("beca", beca);
            estudiantesConBeneficio.add(item);
        }

        model.addAttribute("estudiantesConBeneficio", estudiantesConBeneficio);
        model.addAttribute("totalConBeneficio", estudiantesConBeneficio.size());
        return "coordinador/informe-beneficios";
    }

    /** Ver la resolución de beneficios completa */
    @GetMapping("/resolucion")
    public String resolucion(Model model) {
        model.addAttribute("resolucionesIngenieria",
            resolucionRepository.findAll().stream()
                .filter(r -> "INGENIERIA".equals(r.getTipoPrograma()))
                .toList());
        model.addAttribute("resolucionesTecnologia",
            resolucionRepository.findAll().stream()
                .filter(r -> "TECNOLOGIA".equals(r.getTipoPrograma()))
                .toList());
        return "coordinador/resolucion";
    }
}