package com.saberpro.repository;

import com.saberpro.model.ResolucionBeneficio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface ResolucionRepository extends JpaRepository<ResolucionBeneficio, Long> {
    
    @Query("SELECT r FROM ResolucionBeneficio r WHERE r.tipoPrograma = :tipo AND :puntaje BETWEEN r.puntajeMinimo AND COALESCE(r.puntajeMaximo, :puntaje)")
    Optional<ResolucionBeneficio> findBeneficioByPuntajeAndTipo(@Param("tipo") String tipo, @Param("puntaje") Integer puntaje);
}