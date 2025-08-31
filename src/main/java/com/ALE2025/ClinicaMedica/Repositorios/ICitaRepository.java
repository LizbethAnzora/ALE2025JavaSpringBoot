package com.ALE2025.ClinicaMedica.Repositorios;

import com.ALE2025.ClinicaMedica.Modelos.Cita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Date;

@Repository
public interface ICitaRepository extends JpaRepository<Cita, Integer> {
    
    @Query("SELECT c FROM Cita c WHERE " +
           "(:nombrePaciente IS NULL OR c.paciente.nombre LIKE %:nombrePaciente%) AND " +
           "(:duiPaciente IS NULL OR c.paciente.DUI LIKE %:duiPaciente%) AND " +
           "(:nombreMedico IS NULL OR c.medico.nombre LIKE %:nombreMedico%) AND " +
           "(:fechaCita IS NULL OR c.fechaCita = :fechaCita)")
    List<Cita> buscarConFiltros(@Param("nombrePaciente") String nombrePaciente,
                                @Param("duiPaciente") String duiPaciente,
                                @Param("nombreMedico") String nombreMedico,
                                @Param("fechaCita") Date fechaCita);

}