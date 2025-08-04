package com.ALE2025.ClinicaMedica.Modelos;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.sql.Time;


@Entity
@Table(name = "Horarios")
public class Horario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "medico_id", referencedColumnName = "id")
    private Medico medico;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "El día de la semana es requerido")
    private DiaSemana diaSemana;

    @NotNull(message = "La hora de inicio es requerida")
    private Time horaInicio;

    @NotNull(message = "La hora de fin es requerida")
    private Time horaFin;

    public enum DiaSemana {
        Lunes, Martes, Miércoles, Jueves, Viernes, Sábado, Domingo
    }

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

    public Time getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(Time horaInicio) {
        this.horaInicio = horaInicio;
    }

    public Time getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(Time horaFin) {
        this.horaFin = horaFin;
    }
}
