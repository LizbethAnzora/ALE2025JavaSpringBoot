package com.ALE2025.ClinicaMedica.Repositorios;

import com.ALE2025.ClinicaMedica.Modelos.Especialidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para la entidad Especialidad.
 * Proporciona métodos para interactuar con la base de datos,
 * heredando las funcionalidades de JpaRepository.
 */
@Repository
public interface IEspecialidadRepository extends JpaRepository<Especialidad, Integer> {

}