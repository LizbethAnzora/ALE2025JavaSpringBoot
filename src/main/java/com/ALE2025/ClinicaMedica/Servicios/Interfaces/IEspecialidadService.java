package com.ALE2025.ClinicaMedica.Servicios.Interfaces;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.ALE2025.ClinicaMedica.Modelos.Especialidad;

/**
 * Interfaz de servicio para la gestión de Especialidades.
 * Define las operaciones de negocio para el CRUD de especialidades.
 */
public interface IEspecialidadService {
    
    /**
     * Busca todas las especialidades de forma paginada.
     * @param pageable Objeto que contiene la información de paginación.
     * @return Una página de especialidades.
     */
    Page<Especialidad> buscarTodosPaginados(Pageable pageable);

    /**
     * Obtiene una lista de todas las especialidades.
     * @return Una lista de todas las especialidades.
     */
    List<Especialidad> obtenerTodos();

    /**
     * Busca una especialidad por su ID.
     * @param id El ID de la especialidad a buscar.
     * @return Un Optional que puede contener la especialidad encontrada.
     */
    Optional<Especialidad> buscarPorId(Integer id);

    /**
     * Crea o actualiza una especialidad.
     * @param especialidad La especialidad a guardar.
     * @return La especialidad guardada.
     */
    Especialidad crearOEditar(Especialidad especialidad);

    /**
     * Elimina una especialidad por su ID.
     * @param id El ID de la especialidad a eliminar.
     */
    void eliminarPorId(Integer id);
}