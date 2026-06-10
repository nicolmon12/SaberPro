package com.saberpro.controller;

import com.saberpro.model.Director;
import com.saberpro.model.Facultad;
import com.saberpro.model.Docente;
import com.saberpro.model.ResolucionBeneficio;
import com.saberpro.repository.DirectorRepository;
import com.saberpro.repository.FacultadRepository;
import com.saberpro.repository.DocenteRepository;
import com.saberpro.repository.ResolucionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {
    
    @Autowired private FacultadRepository facultadRepository;
    @Autowired private DocenteRepository docenteRepository;
    @Autowired private ResolucionRepository resolucionRepository;
    @Autowired private DirectorRepository directorRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("totalFacultades", facultadRepository.count());
        model.addAttribute("totalDocentes", docenteRepository.count());
        model.addAttribute("totalResoluciones", resolucionRepository.count());
        model.addAttribute("totalDirectores", directorRepository.count());
        return "admin/dashboard";
    }
    
    // ── FACULTADES ──────────────────────────────────────────────────────────
    @GetMapping("/facultades")
    public String facultades(Model model) {
        model.addAttribute("facultades", facultadRepository.findAll());
        return "admin/facultades";
    }
    
    @PostMapping("/facultades/guardar")
    public String guardarFacultad(@ModelAttribute Facultad facultad) {
        facultadRepository.save(facultad);
        return "redirect:/admin/facultades";
    }
    
    @GetMapping("/facultades/eliminar/{id}")
    public String eliminarFacultad(@PathVariable Long id) {
        facultadRepository.deleteById(id);
        return "redirect:/admin/facultades";
    }
    
    // ── DOCENTES ────────────────────────────────────────────────────────────
    @GetMapping("/docentes")
    public String docentes(Model model) {
        model.addAttribute("docentes", docenteRepository.findAll());
        model.addAttribute("facultades", facultadRepository.findAll());
        return "admin/docentes";
    }
    
    @PostMapping("/docentes/guardar")
    public String guardarDocente(@ModelAttribute Docente docente) {
        if (docente.getId() == null || docente.getPassword() == null || docente.getPassword().isBlank()) {
            docente.setPassword(passwordEncoder.encode(docente.getCedula()));
        }
        docenteRepository.save(docente);
        return "redirect:/admin/docentes";
    }
    
    @GetMapping("/docentes/eliminar/{id}")
    public String eliminarDocente(@PathVariable Long id) {
        docenteRepository.deleteById(id);
        return "redirect:/admin/docentes";
    }
    
    // ── DIRECTORES ──────────────────────────────────────────────────────────
    @GetMapping("/directores")
    public String directores(Model model) {
        model.addAttribute("directores", directorRepository.findAll());
        model.addAttribute("facultades", facultadRepository.findAll());
        return "admin/directores";
    }
    
    @PostMapping("/directores/guardar")
    public String guardarDirector(
            @RequestParam(required = false) Long id,
            @RequestParam String nombre,
            @RequestParam String apellido,
            @RequestParam String email,
            @RequestParam(required = false) String password,
            @RequestParam(required = false, defaultValue = "ADMIN") String rol,
            @RequestParam(required = false) Long facultadId) {
        
        Director director;
        if (id != null) {
            director = directorRepository.findById(id).orElse(new Director());
        } else {
            director = new Director();
        }
        director.setNombre(nombre);
        director.setApellido(apellido);
        director.setEmail(email);
        director.setRol(rol);
        if (password != null && !password.isBlank()) {
            director.setPassword(passwordEncoder.encode(password));
        } else if (director.getId() == null) {
            // contraseña inicial = email
            director.setPassword(passwordEncoder.encode(email));
        }
        if (facultadId != null) {
            director.setFacultad(facultadRepository.findById(facultadId).orElse(null));
        }
        directorRepository.save(director);
        return "redirect:/admin/directores";
    }
    
    @GetMapping("/directores/eliminar/{id}")
    public String eliminarDirector(@PathVariable Long id) {
        directorRepository.deleteById(id);
        return "redirect:/admin/directores";
    }
    
    // ── RESOLUCIONES ────────────────────────────────────────────────────────
    @GetMapping("/resoluciones")
    public String resoluciones(Model model) {
        model.addAttribute("resoluciones", resolucionRepository.findAll());
        return "admin/resoluciones";
    }
    
    @PostMapping("/resoluciones/guardar")
    public String guardarResolucion(@ModelAttribute ResolucionBeneficio resolucion) {
        resolucionRepository.save(resolucion);
        return "redirect:/admin/resoluciones";
    }
    
    @GetMapping("/resoluciones/eliminar/{id}")
    public String eliminarResolucion(@PathVariable Long id) {
        resolucionRepository.deleteById(id);
        return "redirect:/admin/resoluciones";
    }
}
