package com.ALE2025.ClinicaMedica.Modelos;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Clase que representa la entidad Especialidad en la base de datos.
 * Esta clase mapea a la tabla 'Especialidades' y define sus campos.
 * Utiliza anotaciones de JPA para la configuración de la persistencia.
 */
@Entity
@Table(name = "Especialidades")
public class Especialidad {

    // Identificador de la entidad, con generación automática
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Nombre de la especialidad, requerido y con un tamaño específico
    @NotBlank(message = "El nombre es requerido")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    private String nombre;

    // Descripción de la especialidad, puede ser nula
    private String descripcion;

    // Métodos para acceder y modificar los atributos de la entidad (getters y setters)

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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}