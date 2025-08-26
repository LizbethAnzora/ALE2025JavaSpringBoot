package com.ALE2025.ClinicaMedica.Servicios.Implementaciones;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import jakarta.persistence.criteria.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.ALE2025.ClinicaMedica.Modelos.Rol;
import com.ALE2025.ClinicaMedica.Modelos.Usuario;
import com.ALE2025.ClinicaMedica.Modelos.Usuario.EstadoUsuario;
import com.ALE2025.ClinicaMedica.Repositorios.IUsuarioRepository;
import com.ALE2025.ClinicaMedica.Servicios.Interfaces.IUsuarioService;

@Service
public class UsuarioService implements IUsuarioService {

    @Autowired
    private IUsuarioRepository usuarioRepository;

    @Override
    public Page<Usuario> buscarTodosPaginados(Pageable pageable) {
        return usuarioRepository.findAll(pageable);
    }

    @Override
    public List<Usuario> obtenerTodos() {
        return usuarioRepository.findAll();
    }

    @Override
    public Optional<Usuario> buscarPorId(Integer id) {
        return usuarioRepository.findById(id);
    }

    @Override
    public Usuario crearOEditar(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    @Override
    public void eliminarPorId(Integer id) {
        usuarioRepository.deleteById(id);
    }
    
    @Override
    public List<Usuario> buscarConFiltros(String nombre, String email, Rol rol, EstadoUsuario estado) {
        Specification<Usuario> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            // Añade el filtro de nombre si no es nulo o vacío
            if (StringUtils.hasText(nombre)) {
                predicates.add(criteriaBuilder.like(root.get("nombre"), "%" + nombre + "%"));
            }
            
            // Añade el filtro de email si no es nulo o vacío
            if (StringUtils.hasText(email)) {
                predicates.add(criteriaBuilder.like(root.get("email"), "%" + email + "%"));
            }

            // Añade el filtro de rol si no es nulo
            if (rol != null) {
                predicates.add(criteriaBuilder.equal(root.get("rol"), rol));
            }

            // Añade el filtro de estado si no es nulo
            if (estado != null) {
                predicates.add(criteriaBuilder.equal(root.get("estado"), estado));
            }

            // Combina todas las condiciones con un 'AND'
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        return usuarioRepository.findAll(spec);
    }
}