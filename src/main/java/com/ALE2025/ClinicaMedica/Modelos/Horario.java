package com.ALE2025.ClinicaMedica.Modelos;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.DayOfWeek;
import java.time.LocalTime;

@Entity
@Table(name = "Horarios")
public class Horario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "medico_id", nullable = false)
    @NotNull(message = "El médico es requerido")
    private Medico medico;

    @Enumerated(EnumType.STRING)
    @Column(name = "diaSemana", nullable = false)
    @NotNull(message = "El día de la semana es requerido")
    private DiaSemana diaSemana;

    @Column(name = "horaInicio", nullable = false)
    @NotNull(message = "La hora de inicio es requerida")
    private LocalTime horaInicio;

    @Column(name = "horaFin", nullable = false)
    @NotNull(message = "La hora de fin es requerida")
    private LocalTime horaFin;

    // Getters y Setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Medico getMedico() {
        return medico;
    }

    public void setMedico(Medico medico) {
        this.medico = medico;
    }

    public DiaSemana getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(DiaSemana diaSemana) {
        this.diaSemana = diaSemana;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public LocalTime getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(LocalTime horaFin) {
        this.horaFin = horaFin;
    }
}