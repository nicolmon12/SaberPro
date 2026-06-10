package com.saberpro.repository;

import com.saberpro.model.ResultadoSaberPro;
import com.saberpro.model.Estudiante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface ResultadoRepository extends JpaRepository<ResultadoSaberPro, Long> {
    Optional<ResultadoSaberPro> findByEstudianteId(Long estudianteId);
    List<ResultadoSaberPro> findByEstudianteOrderByPuntajeTotalDesc(Estudiante estudiante);
    ResultadoSaberPro findTopByEstudianteOrderByPuntajeTotalDesc(Estudiante estudiante);

    @Query("SELECT AVG(r.puntajeTotal) FROM ResultadoSaberPro r")
    Double getPromedioPuntajes();

    @Query("SELECT COUNT(r) FROM ResultadoSaberPro r WHERE r.puntajeTotal >= 150")
    long countEstudiantesPuntajeAlto();
}
