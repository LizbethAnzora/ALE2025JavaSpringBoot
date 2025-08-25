package com.ALE2025.ClinicaMedica.Servicios.Interfaces;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.ALE2025.ClinicaMedica.Modelos.Especialidad;

public interface IEspecialidadService {

    Page<Especialidad> buscarTodosPaginados(Pageable pageable);

    List<Especialidad> obtenerTodos();

    Optional<Especialidad> buscarPorId(Integer id);

    // Método corregido
    Especialidad crearOEditar(Especialidad especialidad);

    void eliminarPorId(Integer id);
    
    boolean existeEspecialidadConNombre(String nombre); 
}