package com.saberpro.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "estudiantes")
public class Estudiante {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "tipo_documento")
    private String tipoDocumento = "CC";
    
    @Column(name = "numero_documento", unique = true, nullable = false)
    private String numeroDocumento;
    
    @Column(name = "primer_apellido", nullable = false)
    private String primerApellido;
    
    @Column(name = "segundo_apellido")
    private String segundoApellido;
    
    @Column(name = "primer_nombre", nullable = false)
    private String primerNombre;
    
    @Column(name = "segundo_nombre")
    private String segundoNombre;
    
    @Column(unique = true, nullable = false)
    private String email;
    
    private String telefono;
    
    @Column(nullable = false)
    private String password;
    
    @ManyToOne
    @JoinColumn(name = "facultad_id")
    private Facultad facultad;
    
    private Integer semestre = 1;
    
    @Column(name = "aprobado_saber_pro")
    private Boolean aprobadoSaberPro = false;
    
    @Column(name = "comprobante_pago")
    private String comprobantePago;
    
    @OneToMany(mappedBy = "estudiante", cascade = CascadeType.ALL)
    private List<ResultadoSaberPro> resultados;
    
    @OneToMany(mappedBy = "estudiante", cascade = CascadeType.ALL)
    private List<PagoSaberPro> pagos;
    
    public Estudiante() {}
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getTipoDocumento() { return tipoDocumento; }
    public void setTipoDocumento(String tipoDocumento) { this.tipoDocumento = tipoDocumento; }
    
    public String getNumeroDocumento() { return numeroDocumento; }
    public void setNumeroDocumento(String numeroDocumento) { this.numeroDocumento = numeroDocumento; }
    
    public String getPrimerApellido() { return primerApellido; }
    public void setPrimerApellido(String primerApellido) { this.primerApellido = primerApellido; }
    
    public String getSegundoApellido() { return segundoApellido; }
    public void setSegundoApellido(String segundoApellido) { this.segundoApellido = segundoApellido; }
    
    public String getPrimerNombre() { return primerNombre; }
    public void setPrimerNombre(String primerNombre) { this.primerNombre = primerNombre; }
    
    public String getSegundoNombre() { return segundoNombre; }
    public void setSegundoNombre(String segundoNombre) { this.segundoNombre = segundoNombre; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public Facultad getFacultad() { return facultad; }
    public void setFacultad(Facultad facultad) { this.facultad = facultad; }
    
    public Integer getSemestre() { return semestre; }
    public void setSemestre(Integer semestre) { this.semestre = semestre; }
    
    public Boolean getAprobadoSaberPro() { return aprobadoSaberPro; }
    public void setAprobadoSaberPro(Boolean aprobadoSaberPro) { this.aprobadoSaberPro = aprobadoSaberPro; }
    
    public String getComprobantePago() { return comprobantePago; }
    public void setComprobantePago(String comprobantePago) { this.comprobantePago = comprobantePago; }
    
    public List<ResultadoSaberPro> getResultados() { return resultados; }
    public void setResultados(List<ResultadoSaberPro> resultados) { this.resultados = resultados; }
    
    public List<PagoSaberPro> getPagos() { return pagos; }
    public void setPagos(List<PagoSaberPro> pagos) { this.pagos = pagos; }
    
    public String getNombreCompleto() {
        return primerNombre + " " + primerApellido;
    }
}