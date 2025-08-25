package com.ALE2025.ClinicaMedica.Servicios.Interfaces;

import com.ALE2025.ClinicaMedica.Modelos.Horario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

public interface IHorarioService {
    Page<Horario> buscarTodosPaginados(Pageable pageable);
    List<Horario> obtenerTodos();
    Optional<Horario> buscarPorId(Integer id);
    Horario crearOEditar(Horario horario);
    void eliminarPorId(Integer id);
}