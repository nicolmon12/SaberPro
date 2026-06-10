package com.saberpro.controller;

import com.saberpro.model.ResolucionBeneficio;
import com.saberpro.model.ResultadoSaberPro;
import com.saberpro.repository.EstudianteRepository;
import com.saberpro.repository.FacultadRepository;
import com.saberpro.repository.ResolucionRepository;
import com.saberpro.repository.ResultadoRepository;
import com.saberpro.service.BeneficioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/docente")
public class DocenteController {

    @Autowired private EstudianteRepository estudianteRepository;
    @Autowired private FacultadRepository facultadRepository;
    @Autowired private ResultadoRepository resultadoRepository;
    @Autowired private ResolucionRepository resolucionRepository;
    @Autowired private BeneficioService beneficioService;

    @GetMapping("/dashboard")
    public String dashboard(
            @RequestParam(required = false) String cedula,
            @RequestParam(required = false) Long facultadId,
            Model model) {

        if (cedula != null && !cedula.isBlank()) {
            model.addAttribute("estudiantes",
                estudianteRepository.findByNumeroDocumento(cedula)
                    .map(java.util.List::of)
                    .orElse(java.util.List.of()));
        } else if (facultadId != null) {
            model.addAttribute("estudiantes",
                estudianteRepository.findByFacultadId(facultadId));
        } else {
            model.addAttribute("estudiantes", estudianteRepository.findAll());
        }

        model.addAttribute("facultades", facultadRepository.findAll());
        model.addAttribute("cedulaFiltro", cedula);
        model.addAttribute("facultadFiltro", facultadId);
        return "docente/dashboard";
    }

    @GetMapping("/informe-general")
    public String informeGeneral(
            @RequestParam(required = false) Long facultadId,
            Model model) {
        if (facultadId != null) {
            model.addAttribute("estudiantes", estudianteRepository.findByFacultadId(facultadId));
        } else {
            model.addAttribute("estudiantes", estudianteRepository.findAll());
        }
        model.addAttribute("facultades", facultadRepository.findAll());
        model.addAttribute("facultadFiltro", facultadId);
        return "docente/informe-general";
    }

    @GetMapping("/informe-estudiantes")
    public String informeEstudiante(
            @RequestParam(required = false) Long estudianteId,
            @RequestParam(required = false) String cedula,
            Model model) {

        if (estudianteId != null) {
            estudianteRepository.findById(estudianteId).ifPresent(est -> {
                model.addAttribute("estudiante", est);
                model.addAttribute("resultados",
                    resultadoRepository.findByEstudianteOrderByPuntajeTotalDesc(est));
            });
        } else if (cedula != null && !cedula.isBlank()) {
            estudianteRepository.findByNumeroDocumento(cedula).ifPresent(est -> {
                model.addAttribute("estudiante", est);
                model.addAttribute("resultados",
                    resultadoRepository.findByEstudianteOrderByPuntajeTotalDesc(est));
            });
        }
        model.addAttribute("facultades", facultadRepository.findAll());
        return "docente/informe-estudiantes";
    }

    /** Informe de beneficios por estudiante según resolución */
    @GetMapping("/informe-beneficios")
    public String informeBeneficios(
            @RequestParam(required = false) Long facultadId,
            Model model) {
        if (facultadId != null) {
            model.addAttribute("estudiantes", estudianteRepository.findByFacultadId(facultadId));
        } else {
            model.addAttribute("estudiantes", estudianteRepository.findAll());
        }
        model.addAttribute("facultades", facultadRepository.findAll());
        model.addAttribute("facultadFiltro", facultadId);
        model.addAttribute("beneficioService", beneficioService);
        return "docente/informe-beneficios";
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
        return "docente/resolucion";
    }
}
