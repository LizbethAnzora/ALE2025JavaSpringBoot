package com.ALE2025.ClinicaMedica.Modelos;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.springframework.web.multipart.MultipartFile;

@Entity
@Table(name = "Medicos")
public class Medico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "El nombre es requerido")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    private String nombre;

    @NotBlank(message = "El apellido es requerido")
    @Size(min = 2, max = 100, message = "El apellido debe tener entre 2 y 100 caracteres")
    private String apellido;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_Especialidad", nullable = false)
    @NotNull(message = "La especialidad es requerida")
    private Especialidad especialidad;

    @NotNull(message = "El costo de consulta es requerido")
    @DecimalMin(value = "0.01", message = "El costo debe ser mayor a 0")
    @Column(name = "costo_consulta")
    private Double costoConsulta;

    @NotBlank(message = "El género es requerido")
    @Pattern(regexp = "Masculino|Femenino|Otro", message = "Género no válido")
    private String genero;

    @NotBlank(message = "El DUI es requerido")
    @Size(min = 10, max = 10, message = "El DUI debe tener 10 caracteres")
    @Column(unique = true)
    private String DUI;

    @Size(max = 20, message = "El teléfono no puede tener más de 20 caracteres")
    private String telefono;

    @Size(max = 255, message = "La dirección no puede tener más de 255 caracteres")
    private String direccion;

    @NotBlank(message = "El email es requerido")
    @Email(message = "El email debe ser una dirección de correo válida")
    @Column(unique = true)
    private String email;

    @Column(name = "imagen")
    private String imagenPath;

    @Transient
    private MultipartFile imagenFile;

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public Especialidad getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(Especialidad especialidad) {
        this.especialidad = especialidad;
    }

    public Double getCostoConsulta() {
        return costoConsulta;
    }

    public void setCostoConsulta(Double costoConsulta) {
        this.costoConsulta = costoConsulta;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getDUI() {
        return DUI;
    }

    public void setDUI(String DUI) {
        this.DUI = DUI;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImagenPath() {
        return imagenPath;
    }

    public void setImagenPath(String imagenPath) {
        this.imagenPath = imagenPath;
    }

    public MultipartFile getImagenFile() {
        return imagenFile;
    }

    public void setImagenFile(MultipartFile imagenFile) {
        this.imagenFile = imagenFile;
    }
}