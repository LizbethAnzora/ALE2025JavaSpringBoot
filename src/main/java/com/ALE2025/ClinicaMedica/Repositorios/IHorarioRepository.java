package com.ALE2025.ClinicaMedica.Repositorios;

import com.ALE2025.ClinicaMedica.Modelos.Horario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IHorarioRepository extends JpaRepository<Horario, Integer> {

    @Query("SELECT h FROM Horario h WHERE " +
            "(:nombreMedico IS NULL OR LOWER(h.medico.nombre) LIKE LOWER(CONCAT('%', :nombreMedico, '%'))) AND " +
            "(:apellidoMedico IS NULL OR LOWER(h.medico.apellido) LIKE LOWER(CONCAT('%', :apellidoMedico, '%'))) AND " +
            "(:nombreEspecialidad IS NULL OR LOWER(h.medico.especialidad.nombre) LIKE LOWER(CONCAT('%', :nombreEspecialidad, '%')))")
    Page<Horario> findByFilters(
            @Param("nombreMedico") String nombreMedico,
            @Param("apellidoMedico") String apellidoMedico,
            @Param("nombreEspecialidad") String nombreEspecialidad,
            Pageable pageable);
}