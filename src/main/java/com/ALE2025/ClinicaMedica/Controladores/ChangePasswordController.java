package com.ALE2025.ClinicaMedica.Controladores;

import com.ALE2025.ClinicaMedica.Modelos.Usuario;
import com.ALE2025.ClinicaMedica.Servicios.Interfaces.IUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/perfil")
public class ChangePasswordController {

    @Autowired
    private IUsuarioService usuarioService;

    @GetMapping("/cambiar-contraseña")
    public String mostrarFormularioCambio() {
        return "perfil/cambiar-contraseña"; // Vista con formulario
    }

    @PostMapping("/cambiar-contraseña")
    public String cambiarContraseña(
            @AuthenticationPrincipal User userDetails,
            @RequestParam("username") String username,
            @RequestParam("nuevaContrasena") String nuevaContrasena,
            @RequestParam("confirmarContrasena") String confirmarContrasena,
            RedirectAttributes redirectAttributes,
            Model model) {
        
        String emailAutenticado = userDetails.getUsername();

        // 1. Validar que el correo del formulario coincida con el usuario autenticado
        if (!emailAutenticado.equals(username)) {
            model.addAttribute("error", "El correo proporcionado no coincide con tu cuenta.");
            return "perfil/cambiar-contraseña";
        }
        
        // 2. Validar que las contraseñas coincidan
        if (!nuevaContrasena.equals(confirmarContrasena)) {
            model.addAttribute("error", "Las contraseñas no coinciden.");
            return "perfil/cambiar-contraseña";
        }

        // 3. Buscar y actualizar la contraseña del usuario
        Usuario usuario = usuarioService.buscarPorEmail(emailAutenticado).orElse(null);

        if (usuario == null) {
            model.addAttribute("error", "Usuario no encontrado.");
            return "perfil/cambiar-contraseña";
        }

        usuarioService.actualizarContraseña(usuario, nuevaContrasena);
        
        // 4. Redirigir y añadir un mensaje flash para que persista
        redirectAttributes.addFlashAttribute("msg", "Contraseña actualizada correctamente.");

        // Redirigimos a la vista principal o a la que desees
        return "redirect:/";
    }
}