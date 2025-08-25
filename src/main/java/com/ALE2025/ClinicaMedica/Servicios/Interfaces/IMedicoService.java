package com.ALE2025.ClinicaMedica.Servicios.Interfaces;

import com.ALE2025.ClinicaMedica.Modelos.Medico;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface IMedicoService {
    Page<Medico> buscarTodosPaginados(Pageable pageable);

    List<Medico> obtenerTodos();

    Optional<Medico> buscarPorId(Integer id);

    Medico crearOEditar(Medico medico, MultipartFile imagenFile);

    void eliminarPorId(Integer id);
}