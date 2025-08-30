package com.ALE2025.ClinicaMedica.Servicios.Interfaces;

import com.ALE2025.ClinicaMedica.Modelos.Rol;
import com.ALE2025.ClinicaMedica.Modelos.Usuario;
import com.ALE2025.ClinicaMedica.Modelos.Usuario.EstadoUsuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IUsuarioService {

    Page<Usuario> buscarTodosPaginados(Pageable pageable);

    List<Usuario> obtenerTodos();

    Optional<Usuario> buscarPorId(Integer id);

    Usuario crearOEditar(Usuario usuario);

    void eliminarPorId(Integer id);

    List<Usuario> buscarConFiltros(String nombre, String email, Rol rol, EstadoUsuario estado);

    Optional<Usuario> buscarPorEmail(String email);

    // NUEVO: actualizar contraseña de un usuario
    void actualizarContraseña(Usuario usuario, String nuevaContraseña);
}