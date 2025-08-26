package com.ALE2025.ClinicaMedica.Servicios.Interfaces;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.ALE2025.ClinicaMedica.Modelos.Rol;
import com.ALE2025.ClinicaMedica.Modelos.Usuario;
import com.ALE2025.ClinicaMedica.Modelos.Usuario.EstadoUsuario;

public interface IUsuarioService {
    
    Page<Usuario> buscarTodosPaginados(Pageable pageable);

    List<Usuario> obtenerTodos();

    Optional<Usuario> buscarPorId(Integer id);

    Usuario crearOEditar(Usuario usuario);

    void eliminarPorId(Integer id);

    // Nuevo método para la búsqueda con filtros
    List<Usuario> buscarConFiltros(String nombre, String email, Rol rol, EstadoUsuario estado);
}