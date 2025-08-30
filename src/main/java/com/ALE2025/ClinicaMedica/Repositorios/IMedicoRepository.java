package com.ALE2025.ClinicaMedica.Repositorios;

import com.ALE2025.ClinicaMedica.Modelos.Medico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IMedicoRepository extends JpaRepository<Medico, Integer> {

    @Query("SELECT m FROM Medico m WHERE " +
           "(:nombre IS NULL OR lower(m.nombre) LIKE lower(concat('%', :nombre, '%'))) AND " +
           "(:apellido IS NULL OR lower(m.apellido) LIKE lower(concat('%', :apellido, '%'))) AND " +
           "(:especialidad IS NULL OR lower(m.especialidad.nombre) LIKE lower(concat('%', :especialidad, '%'))) AND " +
           "(:dui IS NULL OR lower(m.DUI) LIKE lower(concat('%', :dui, '%')))")
    List<Medico> buscarPorFiltros(
            @Param("nombre") String nombre,
            @Param("apellido") String apellido,
            @Param("especialidad") String especialidad,
            @Param("dui") String dui
    );
}