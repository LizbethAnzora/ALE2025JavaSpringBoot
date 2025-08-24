package com.ALE2025.ClinicaMedica.Servicios.Interfaces;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.ALE2025.ClinicaMedica.Modelos.Paciente;

public interface IPacienteService {
    Page<Paciente> buscarTodosPaginados(Pageable pageable);

    List<Paciente> obtenerTodos();

    Optional<Paciente> buscarPorId(Integer id);

    Paciente crearOEditar(Paciente paciente);

    void eliminarPorId(Integer id);
}