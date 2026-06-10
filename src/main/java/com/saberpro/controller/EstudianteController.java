package com.saberpro.controller;

import com.saberpro.model.Estudiante;
import com.saberpro.model.PagoSaberPro;
import com.saberpro.model.ResultadoSaberPro;
import com.saberpro.repository.EstudianteRepository;
import com.saberpro.repository.PagoRepository;
import com.saberpro.repository.ResolucionRepository;
import com.saberpro.repository.ResultadoRepository;
import com.saberpro.service.BeneficioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.UUID;

@Controller
@RequestMapping("/estudiante")
public class EstudianteController {

    @Autowired private EstudianteRepository estudianteRepository;
    @Autowired private ResultadoRepository resultadoRepository;
    @Autowired private PagoRepository pagoRepository;
    @Autowired private BeneficioService beneficioService;
    @Autowired private ResolucionRepository resolucionRepository;

    private final String UPLOAD_DIR = "uploads/comprobantes/";

    private Estudiante getCurrentEstudiante() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        return estudianteRepository.findByEmail(email).orElse(null);
    }

    private String getTipoPrograma(Estudiante estudiante) {
        if (estudiante.getFacultad() == null) return "INGENIERIA";
        String nombre = estudiante.getFacultad().getNombre().toUpperCase();
        String codigo = estudiante.getFacultad().getCodigo().toUpperCase();
        if (nombre.contains("TECNOLOG") || codigo.contains("TECNOLOG")) {
            return "TECNOLOGIA";
        }
        return "INGENIERIA";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        Estudiante estudiante = getCurrentEstudiante();
        model.addAttribute("estudiante", estudiante);

        ResultadoSaberPro ultimoResultado = resultadoRepository.findTopByEstudianteOrderByPuntajeTotalDesc(estudiante);
        if (ultimoResultado != null) {
            String tipoPrograma = getTipoPrograma(estudiante);
            model.addAttribute("ultimoResultado", ultimoResultado);
            model.addAttribute("tipoPrograma", tipoPrograma);
            model.addAttribute("beneficio", beneficioService.obtenerBeneficio(ultimoResultado.getPuntajeTotal(), tipoPrograma));
        }
        return "estudiante/dashboard";
    }

    @GetMapping("/subir-pago")
    public String subirPago(Model model) {
        Estudiante estudiante = getCurrentEstudiante();
        model.addAttribute("pagos", pagoRepository.findByEstudianteId(estudiante.getId()));
        return "estudiante/subir-pago";
    }

    @PostMapping("/subir-pago")
    public String doSubirPago(@RequestParam("comprobante") MultipartFile file) throws IOException {
        Estudiante estudiante = getCurrentEstudiante();

        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String filename = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path filePath = uploadPath.resolve(filename);
        Files.copy(file.getInputStream(), filePath);

        PagoSaberPro pago = new PagoSaberPro();
        pago.setEstudiante(estudiante);
        pago.setFechaPago(LocalDate.now());
        pago.setComprobanteUrl("/" + UPLOAD_DIR + filename);
        pago.setEstado(PagoSaberPro.EstadoPago.PENDIENTE);
        pagoRepository.save(pago);

        return "redirect:/estudiante/subir-pago";
    }

    @GetMapping("/resultados")
    public String resultados(Model model) {
        Estudiante estudiante = getCurrentEstudiante();
        model.addAttribute("resultados", resultadoRepository.findByEstudianteOrderByPuntajeTotalDesc(estudiante));
        model.addAttribute("estudiante", estudiante);
        return "estudiante/resultados";
    }

    @GetMapping("/beneficios")
    public String beneficios(Model model) {
        Estudiante estudiante = getCurrentEstudiante();
        ResultadoSaberPro ultimoResultado = resultadoRepository.findTopByEstudianteOrderByPuntajeTotalDesc(estudiante);

        if (ultimoResultado != null) {
            String tipoPrograma = getTipoPrograma(estudiante);
            model.addAttribute("puntaje", ultimoResultado.getPuntajeTotal());
            model.addAttribute("tipoPrograma", tipoPrograma);
            model.addAttribute("beneficio", beneficioService.obtenerBeneficio(ultimoResultado.getPuntajeTotal(), tipoPrograma));
        }
        return "estudiante/beneficios";
    }

    @GetMapping("/resolucion")
    public String resolucion(Model model) {
        Estudiante estudiante = getCurrentEstudiante();
        String tipoPrograma = getTipoPrograma(estudiante);
        model.addAttribute("tipoPrograma", tipoPrograma);
        model.addAttribute("resoluciones",
            resolucionRepository.findAll().stream()
                .filter(r -> tipoPrograma.equals(r.getTipoPrograma()))
                .toList());
        return "estudiante/resolucion";
    }
}
