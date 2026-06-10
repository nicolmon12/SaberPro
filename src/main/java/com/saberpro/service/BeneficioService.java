package com.saberpro.service;

import com.saberpro.model.ResolucionBeneficio;
import com.saberpro.repository.ResolucionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BeneficioService {
    
    @Autowired
    private ResolucionRepository resolucionRepository;
    
    public String obtenerBeneficio(Integer puntaje, String tipoPrograma) {
        return resolucionRepository.findBeneficioByPuntajeAndTipo(tipoPrograma, puntaje)
            .map(ResolucionBeneficio::getBeneficio)
            .orElse("Sin beneficio definido");
    }
    
    public ResolucionBeneficio obtenerResolucionCompleta(Integer puntaje, String tipoPrograma) {
        return resolucionRepository.findBeneficioByPuntajeAndTipo(tipoPrograma, puntaje)
            .orElse(null);
    }
}