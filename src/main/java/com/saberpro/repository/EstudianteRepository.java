package com.saberpro.repository;

import com.saberpro.model.Estudiante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface EstudianteRepository extends JpaRepository<Estudiante, Long> {
    Optional<Estudiante> findByEmail(String email);
    Optional<Estudiante> findByNumeroDocumento(String numeroDocumento);
    List<Estudiante> findByFacultadId(Long facultadId);
    
    @Query("SELECT COUNT(e) FROM Estudiante e WHERE e.aprobadoSaberPro = true")
    long countEstudiantesAprobados();
}
