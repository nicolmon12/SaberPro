package com.saberpro.service;

import com.saberpro.model.Director;
import com.saberpro.model.Docente;
import com.saberpro.model.Estudiante;
import com.saberpro.repository.DirectorRepository;
import com.saberpro.repository.DocenteRepository;
import com.saberpro.repository.EstudianteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired private DirectorRepository directorRepository;
    @Autowired private DocenteRepository docenteRepository;
    @Autowired private EstudianteRepository estudianteRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        
        Director director = directorRepository.findByEmail(email).orElse(null);
        if (director != null) {
            // Asignar rol según el campo rol del director
            String role = "ROLE_" + (director.getRol() != null ? director.getRol() : "ADMIN");
            return new User(director.getEmail(), director.getPassword(),
                    Collections.singletonList(new SimpleGrantedAuthority(role)));
        }
        
        Docente docente = docenteRepository.findByEmail(email).orElse(null);
        if (docente != null) {
            return new User(docente.getEmail(), docente.getPassword(),
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_DOCENTE")));
        }
        
        Estudiante estudiante = estudianteRepository.findByEmail(email).orElse(null);
        if (estudiante != null) {
            return new User(estudiante.getEmail(), estudiante.getPassword(),
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_ESTUDIANTE")));
        }
        
        throw new UsernameNotFoundException("Usuario no encontrado: " + email);
    }
}
