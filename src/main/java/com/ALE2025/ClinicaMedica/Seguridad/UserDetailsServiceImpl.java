package com.ALE2025.ClinicaMedica.Seguridad;

import com.ALE2025.ClinicaMedica.Modelos.Usuario;
import com.ALE2025.ClinicaMedica.Repositorios.IUsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private IUsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        
        Optional<Usuario> usuarioOptional = usuarioRepository.findByEmail(email);

        if (usuarioOptional.isEmpty()) {
            throw new UsernameNotFoundException("Usuario no encontrado con el email: " + email);
        }

        Usuario usuario = usuarioOptional.get();

        // Crear una lista de roles de Spring Security
        List<GrantedAuthority> authorities = new ArrayList<>();
        // Corrección: el rol de la base de datos ya tiene el prefijo, lo usamos directamente.
        authorities.add(new SimpleGrantedAuthority(usuario.getRol().getNombre().toUpperCase()));
        
        return new User(usuario.getEmail(), usuario.getContraseña(), authorities);
    }
}