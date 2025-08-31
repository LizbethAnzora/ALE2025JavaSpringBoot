package com.ALE2025.ClinicaMedica.Servicios.Implementaciones;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import jakarta.persistence.criteria.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Usuario crearOEditar(Usuario usuario) {
        if (usuario.getId() == null || (usuario.getContraseña() != null && !usuario.getContraseña().isEmpty())) {
            String hashedPassword = passwordEncoder.encode(usuario.getContraseña());
            usuario.setContraseña(hashedPassword);
        } else {
            Optional<Usuario> usuarioExistente = usuarioRepository.findById(usuario.getId());
            if (usuarioExistente.isPresent()) {
                usuario.setContraseña(usuarioExistente.get().getContraseña());
            }
        }
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

            if (StringUtils.hasText(nombre)) {
                predicates.add(criteriaBuilder.like(root.get("nombre"), "%" + nombre + "%"));
            }

            if (StringUtils.hasText(email)) {
                predicates.add(criteriaBuilder.like(root.get("email"), "%" + email + "%"));
            }

            if (rol != null) {
                predicates.add(criteriaBuilder.equal(root.get("rol"), rol));
            }

            if (estado != null) {
                predicates.add(criteriaBuilder.equal(root.get("estado"), estado));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        return usuarioRepository.findAll(spec);
    }

    


        @Override
    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    @Override
    public void actualizarContraseña(Usuario usuario, String nuevaContraseña) {
        String hashedPassword = passwordEncoder.encode(nuevaContraseña);
        usuario.setContraseña(hashedPassword);
        usuarioRepository.save(usuario);
    }
}