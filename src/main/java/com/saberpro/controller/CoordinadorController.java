package com.saberpro.controller;

import com.saberpro.model.Estudiante;
import com.saberpro.model.Facultad;
import com.saberpro.model.PagoSaberPro;
import com.saberpro.model.ResultadoSaberPro;
import com.saberpro.repository.EstudianteRepository;
import com.saberpro.repository.FacultadRepository;
import com.saberpro.repository.PagoRepository;
import com.saberpro.repository.ResolucionRepository;
import com.saberpro.repository.ResultadoRepository;
import com.saberpro.service.BeneficioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/coordinador")
public class CoordinadorController {

    @Autowired private EstudianteRepository estudianteRepository;
    @Autowired private FacultadRepository facultadRepository;
    @Autowired private PagoRepository pagoRepository;
    @Autowired private ResultadoRepository resultadoRepository;
    @Autowired private ResolucionRepository resolucionRepository;
    @Autowired private BeneficioService beneficioService;
    @Autowired private PasswordEncoder passwordEncoder;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("totalEstudiantes", estudianteRepository.count());
        model.addAttribute("pagosPendientes", pagoRepository.findByEstado(PagoSaberPro.EstadoPago.PENDIENTE).size());
        model.addAttribute("promedioPuntajes", resultadoRepository.getPromedioPuntajes());
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
    public String informeGeneral(Model model) {
        model.addAttribute("estudiantes", estudianteRepository.findAll());
        return "coordinador/informe-general";
    }

    /** Informe de alumnos filtrado por tipo de examen: TOTAL o UNICO */
    @GetMapping("/informe-alumno")
    public String informeAlumno(
            @RequestParam(required = false) String tipoExamen,
            Model model) {

        List<Estudiante> estudiantes = estudianteRepository.findAll();

        if (tipoExamen != null && !tipoExamen.isBlank()) {
            String filtro = tipoExamen.toUpperCase();
            estudiantes = estudiantes.stream()
                .filter(e -> e.getResultados() != null && !e.getResultados().isEmpty() &&
                             filtro.equals(e.getResultados().get(0).getTipoExamen()))
                .collect(Collectors.toList());
        }

        model.addAttribute("estudiantes", estudiantes);
        model.addAttribute("tipoExamenFiltro", tipoExamen);
        return "coordinador/informe-alumno";
    }

    /** Informe de beneficios de todos los estudiantes */
    @GetMapping("/informe-beneficios")
    public String informeBeneficios(Model model) {
        model.addAttribute("estudiantes", estudianteRepository.findAll());
        model.addAttribute("beneficioService", beneficioService);
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
