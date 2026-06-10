package com.saberpro.repository;

import com.saberpro.model.PagoSaberPro;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PagoRepository extends JpaRepository<PagoSaberPro, Long> {
    List<PagoSaberPro> findByEstado(PagoSaberPro.EstadoPago estado);
    List<PagoSaberPro> findByEstudianteId(Long estudianteId);
}
