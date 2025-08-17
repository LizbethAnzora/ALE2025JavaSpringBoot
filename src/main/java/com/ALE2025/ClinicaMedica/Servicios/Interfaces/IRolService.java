package com.ALE2025.ClinicaMedica.Servicios.Interfaces;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.ALE2025.ClinicaMedica.Modelos.Rol;

public interface IRolService {

    Page<Rol> buscarTodosPaginados(Pageable pageable);

    List<Rol> obtenerTodos();

    Optional<Rol> buscarPorId(Integer id);

    Rol crearOEditar(Rol rol);

    void eliminarPorId(Integer id);
}