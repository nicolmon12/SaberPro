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

    @Column(name = "comunicacion_escrita_nivel")
    private String comunicacionEscritaNivel;
    
    @Column(name = "razonamiento_cuantitativo")
    private Integer razonamientoCuantitativo;

    @Column(name = "razonamiento_cuantitativo_nivel")
    private String razonamientoCuantitativoNivel;
    
    @Column(name = "lectura_critica")
    private Integer lecturaCritica;

    @Column(name = "lectura_critica_nivel")
    private String lecturaCriticaNivel;
    
    @Column(name = "competencias_ciudadanas")
    private Integer competenciasCiudadanas;

    @Column(name = "competencias_ciudadanas_nivel")
    private String competenciasCiudadanasNivel;
    
    @Column(name = "ingles")
    private Integer ingles;

    @Column(name = "ingles_nivel")
    private String inglesNivel;
    
    @Column(name = "nivel_ingles_general")
    private String nivelInglesGeneral;

    @Column(name = "tipo_examen")
    private String tipoExamen = "TOTAL";
    
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

    public String getComunicacionEscritaNivel() { return comunicacionEscritaNivel; }
    public void setComunicacionEscritaNivel(String v) { this.comunicacionEscritaNivel = v; }
    
    public Integer getRazonamientoCuantitativo() { return razonamientoCuantitativo; }
    public void setRazonamientoCuantitativo(Integer razonamientoCuantitativo) { this.razonamientoCuantitativo = razonamientoCuantitativo; }

    public String getRazonamientoCuantitativoNivel() { return razonamientoCuantitativoNivel; }
    public void setRazonamientoCuantitativoNivel(String v) { this.razonamientoCuantitativoNivel = v; }
    
    public Integer getLecturaCritica() { return lecturaCritica; }
    public void setLecturaCritica(Integer lecturaCritica) { this.lecturaCritica = lecturaCritica; }

    public String getLecturaCriticaNivel() { return lecturaCriticaNivel; }
    public void setLecturaCriticaNivel(String v) { this.lecturaCriticaNivel = v; }
    
    public Integer getCompetenciasCiudadanas() { return competenciasCiudadanas; }
    public void setCompetenciasCiudadanas(Integer competenciasCiudadanas) { this.competenciasCiudadanas = competenciasCiudadanas; }

    public String getCompetenciasCiudadanasNivel() { return competenciasCiudadanasNivel; }
    public void setCompetenciasCiudadanasNivel(String v) { this.competenciasCiudadanasNivel = v; }
    
    public Integer getIngles() { return ingles; }
    public void setIngles(Integer ingles) { this.ingles = ingles; }

    public String getInglesNivel() { return inglesNivel; }
    public void setInglesNivel(String v) { this.inglesNivel = v; }
    
    public String getNivelInglesGeneral() { return nivelInglesGeneral; }
    public void setNivelInglesGeneral(String nivelInglesGeneral) { this.nivelInglesGeneral = nivelInglesGeneral; }

    public String getTipoExamen() { return tipoExamen; }
    public void setTipoExamen(String tipoExamen) { this.tipoExamen = tipoExamen; }
}