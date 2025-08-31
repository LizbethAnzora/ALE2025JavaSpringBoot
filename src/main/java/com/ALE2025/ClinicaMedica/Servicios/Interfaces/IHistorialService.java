package com.ALE2025.ClinicaMedica.Servicios.Interfaces;

import com.ALE2025.ClinicaMedica.Modelos.Historial;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

public interface IHistorialService {
    Page<Historial> buscarTodosPaginados(Pageable pageable);

    Page<Historial> buscarPorFiltrosPaginado(
            String nombreMedico,
            String nombreEspecialidad,
            String nombrePaciente,
            String apellidoPaciente,
            String duiPaciente,
            Pageable pageable);

    List<Historial> buscarPorFiltros(
            String nombreMedico,
            String nombreEspecialidad,
            String nombrePaciente,
            String apellidoPaciente,
            String duiPaciente);

    List<Historial> obtenerTodos();

    Optional<Historial> buscarPorId(Integer id);

    Historial crearOEditar(Historial historial);

    void eliminarPorId(Integer id);
}