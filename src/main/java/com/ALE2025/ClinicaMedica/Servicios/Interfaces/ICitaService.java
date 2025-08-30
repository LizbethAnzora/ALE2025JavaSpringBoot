package com.ALE2025.ClinicaMedica.Servicios.Interfaces;

import com.ALE2025.ClinicaMedica.Modelos.Cita;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface ICitaService {
    Page<Cita> buscarTodosPaginados(Pageable pageable);
    List<Cita> obtenerTodos();
    Optional<Cita> buscarPorId(Integer id);
    Cita crearOEditar(Cita cita);
    void eliminarPorId(Integer id);
    
    // El método sigue siendo el mismo en la interfaz
    List<Cita> buscarCitas(String nombrePaciente, String duiPaciente, String nombreMedico, Date fechaCita);
}