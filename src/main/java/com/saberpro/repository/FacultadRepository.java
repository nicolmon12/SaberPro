package com.saberpro.repository;

import com.saberpro.model.Facultad;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface FacultadRepository extends JpaRepository<Facultad, Long> {
    Optional<Facultad> findByCodigo(String codigo);
}