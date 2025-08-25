package com.ALE2025.ClinicaMedica.Repositorios;

import com.ALE2025.ClinicaMedica.Modelos.Historial;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IHistorialRepository extends JpaRepository<Historial, Integer> {

    @Query("SELECT h FROM Historial h WHERE " +
           "(:nombreMedico IS NULL OR LOWER(h.medico.nombre) LIKE LOWER(CONCAT('%', :nombreMedico, '%'))) AND " +
           "(:nombreEspecialidad IS NULL OR LOWER(h.especialidad.nombre) LIKE LOWER(CONCAT('%', :nombreEspecialidad, '%'))) AND " +
           "(:nombrePaciente IS NULL OR LOWER(h.paciente.nombre) LIKE LOWER(CONCAT('%', :nombrePaciente, '%'))) AND " +
           "(:apellidoPaciente IS NULL OR LOWER(h.paciente.apellido) LIKE LOWER(CONCAT('%', :apellidoPaciente, '%'))) AND " +
           "(:duiPaciente IS NULL OR LOWER(h.paciente.DUI) LIKE LOWER(CONCAT('%', :duiPaciente, '%')))")
    Page<Historial> findByFilters(
            @Param("nombreMedico") String nombreMedico,
            @Param("nombreEspecialidad") String nombreEspecialidad,
            @Param("nombrePaciente") String nombrePaciente,
            @Param("apellidoPaciente") String apellidoPaciente,
            @Param("duiPaciente") String duiPaciente,
            Pageable pageable);
}