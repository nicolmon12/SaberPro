package com.saberpro.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "facultades")
public class Facultad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String nombre;
    
    @Column(unique = true, nullable = false)
    private String codigo;
    
    @OneToMany(mappedBy = "facultad")
    private List<Docente> docentes;
    
    @OneToMany(mappedBy = "facultad")
    private List<Estudiante> estudiantes;
    
    public Facultad() {}
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    
    public List<Docente> getDocentes() { return docentes; }
    public void setDocentes(List<Docente> docentes) { this.docentes = docentes; }
    
    public List<Estudiante> getEstudiantes() { return estudiantes; }
    public void setEstudiantes(List<Estudiante> estudiantes) { this.estudiantes = estudiantes; }
}