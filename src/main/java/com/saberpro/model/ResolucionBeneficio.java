package com.saberpro.model;

import jakarta.persistence.*;

@Entity
@Table(name = "resoluciones_beneficios")
public class ResolucionBeneficio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "tipo_programa", nullable = false)
    private String tipoPrograma;
    
    @Column(name = "puntaje_minimo", nullable = false)
    private Integer puntajeMinimo;
    
    @Column(name = "puntaje_maximo")
    private Integer puntajeMaximo;
    
    @Column(nullable = false)
    private String beneficio;
    
    private String descripcion;
    
    public ResolucionBeneficio() {}
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getTipoPrograma() { return tipoPrograma; }
    public void setTipoPrograma(String tipoPrograma) { this.tipoPrograma = tipoPrograma; }
    
    public Integer getPuntajeMinimo() { return puntajeMinimo; }
    public void setPuntajeMinimo(Integer puntajeMinimo) { this.puntajeMinimo = puntajeMinimo; }
    
    public Integer getPuntajeMaximo() { return puntajeMaximo; }
    public void setPuntajeMaximo(Integer puntajeMaximo) { this.puntajeMaximo = puntajeMaximo; }
    
    public String getBeneficio() { return beneficio; }
    public void setBeneficio(String beneficio) { this.beneficio = beneficio; }
    
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
}