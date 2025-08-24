package com.ALE2025.ClinicaMedica.Servicios.Interfaces;

import com.ALE2025.ClinicaMedica.Modelos.Paciente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IPacienteService {
    Page<Paciente> buscarTodosPaginados(Pageable pageable);
    
    List<Paciente> obtenerTodos();

    Optional<Paciente> buscarPorId(Integer id);

    Paciente crearOEditar(Paciente paciente);

    void eliminarPorId(Integer id);
}