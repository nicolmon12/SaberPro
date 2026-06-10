package com.saberpro.model;

import jakarta.persistence.*;

@Entity
@Table(name = "resultados_saber_pro")
public class ResultadoSaberPro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "estudiante_id", nullable = false)
    private Estudiante estudiante;
    
    @Column(name = "numero_registro")
    private String numeroRegistro;
    
    @Column(name = "puntaje_total")
    private Integer puntajeTotal;
    
    @Column(name = "puntaje_nivel")
    private String puntajeNivel;
    
    @Column(name = "comunicacion_escrita")
    private Integer comunicacionEscrita;
    
    @Column(name = "razonamiento_cuantitativo")
    private Integer razonamientoCuantitativo;
    
    @Column(name = "lectura_critica")
    private Integer lecturaCritica;
    
    @Column(name = "competencias_ciudadanas")
    private Integer competenciasCiudadanas;
    
    @Column(name = "ingles")
    private Integer ingles;
    
    @Column(name = "nivel_ingles_general")
    private String nivelInglesGeneral;
    
    public ResultadoSaberPro() {}
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Estudiante getEstudiante() { return estudiante; }
    public void setEstudiante(Estudiante estudiante) { this.estudiante = estudiante; }
    
    public String getNumeroRegistro() { return numeroRegistro; }
    public void setNumeroRegistro(String numeroRegistro) { this.numeroRegistro = numeroRegistro; }
    
    public Integer getPuntajeTotal() { return puntajeTotal; }
    public void setPuntajeTotal(Integer puntajeTotal) { this.puntajeTotal = puntajeTotal; }
    
    public String getPuntajeNivel() { return puntajeNivel; }
    public void setPuntajeNivel(String puntajeNivel) { this.puntajeNivel = puntajeNivel; }
    
    public Integer getComunicacionEscrita() { return comunicacionEscrita; }
    public void setComunicacionEscrita(Integer comunicacionEscrita) { this.comunicacionEscrita = comunicacionEscrita; }
    
    public Integer getRazonamientoCuantitativo() { return razonamientoCuantitativo; }
    public void setRazonamientoCuantitativo(Integer razonamientoCuantitativo) { this.razonamientoCuantitativo = razonamientoCuantitativo; }
    
    public Integer getLecturaCritica() { return lecturaCritica; }
    public void setLecturaCritica(Integer lecturaCritica) { this.lecturaCritica = lecturaCritica; }
    
    public Integer getCompetenciasCiudadanas() { return competenciasCiudadanas; }
    public void setCompetenciasCiudadanas(Integer competenciasCiudadanas) { this.competenciasCiudadanas = competenciasCiudadanas; }
    
    public Integer getIngles() { return ingles; }
    public void setIngles(Integer ingles) { this.ingles = ingles; }
    
    public String getNivelInglesGeneral() { return nivelInglesGeneral; }
    public void setNivelInglesGeneral(String nivelInglesGeneral) { this.nivelInglesGeneral = nivelInglesGeneral; }

    public String getTipoExamen() { return tipoExamen; }
    public void setTipoExamen(String tipoExamen) { this.tipoExamen = tipoExamen; }

}