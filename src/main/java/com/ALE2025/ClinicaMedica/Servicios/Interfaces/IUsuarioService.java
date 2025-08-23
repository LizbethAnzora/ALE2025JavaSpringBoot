package com.ALE2025.ClinicaMedica.Servicios.Interfaces;

import com.ALE2025.ClinicaMedica.Modelos.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

public interface IUsuarioService {
    public List<Usuario> obtenerTodos();

    public Page<Usuario> buscarTodosPaginados(Pageable pageable);

    public Page<Usuario> buscarPorNombreOEmailPaginado(String keyword, Pageable pageable);
   
    public Optional<Usuario> buscarPorId(Integer id);
    
    public Usuario crearOEditar(Usuario u);
    
    public void eliminarPorId(Integer id);
}