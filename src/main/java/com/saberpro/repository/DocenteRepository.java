package com.saberpro.repository;

import com.saberpro.model.Docente;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface DocenteRepository extends JpaRepository<Docente, Long> {
    Optional<Docente> findByEmail(String email);
    Optional<Docente> findByCedula(String cedula);
}