package com.ALE2025.ClinicaMedica.Controladores;

import com.ALE2025.ClinicaMedica.Modelos.Usuario;
import com.ALE2025.ClinicaMedica.Modelos.Rol;
import com.ALE2025.ClinicaMedica.Modelos.Usuario.EstadoUsuario;
import com.ALE2025.ClinicaMedica.Servicios.Interfaces.IUsuarioService;
import com.ALE2025.ClinicaMedica.Servicios.Interfaces.IRolService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private IUsuarioService usuarioService;

    @Autowired
    private IRolService rolService;


    @GetMapping
    public String index(Model model,
                        @RequestParam(value = "nombre", required = false) String nombre,
                        @RequestParam(value = "email", required = false) String email,
                        @RequestParam(value = "estado", required = false) EstadoUsuario estado,
                        @RequestParam(value = "rol", required = false) Integer rolId,
                        @RequestParam("page") Optional<Integer> page,
                        @RequestParam("size") Optional<Integer> size) {

        Rol rol = null;
        if (rolId != null) {
            rol = rolService.buscarPorId(rolId).orElse(null);
        }

        List<Usuario> usuariosEncontrados;

        if (nombre != null || email != null || estado != null || rol != null) {
            usuariosEncontrados = usuarioService.buscarConFiltros(nombre, email, rol, estado);
            model.addAttribute("filtrado", true);
        } else {
            int currentPage = page.orElse(1) - 1;
            int pageSize = size.orElse(5);
            Pageable pageable = PageRequest.of(currentPage, pageSize);
            Page<Usuario> usuariosPaginados = usuarioService.buscarTodosPaginados(pageable);
            usuariosEncontrados = usuariosPaginados.getContent();
            model.addAttribute("usuariosPaginados", usuariosPaginados);
            model.addAttribute("filtrado", false);
        }

        model.addAttribute("usuarios", usuariosEncontrados);
        model.addAttribute("roles", rolService.obtenerTodos());
        model.addAttribute("estados", Usuario.EstadoUsuario.values());
        model.addAttribute("nombreFiltro", nombre);
        model.addAttribute("emailFiltro", email);
        model.addAttribute("estadoFiltro", estado);
        model.addAttribute("rolFiltro", rolId);

        return "usuario/index";
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("usuario", new Usuario());
        model.addAttribute("roles", rolService.obtenerTodos());
        model.addAttribute("action", "create");
        return "usuario/mant";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {
        Usuario usuario = usuarioService.buscarPorId(id).orElseThrow();
        model.addAttribute("usuario", usuario);
        model.addAttribute("roles", rolService.obtenerTodos());
        model.addAttribute("action", "edit");
        return "usuario/mant";
    }

    @GetMapping("/view/{id}")
    public String view(@PathVariable Integer id, Model model) {
        Usuario usuario = usuarioService.buscarPorId(id).orElseThrow();
        model.addAttribute("usuario", usuario);
        model.addAttribute("roles", rolService.obtenerTodos());
        model.addAttribute("action", "view");
        return "usuario/mant";
    }

    @GetMapping("/delete/{id}")
    public String deleteConfirm(@PathVariable Integer id, Model model) {
        Usuario usuario = usuarioService.buscarPorId(id).orElseThrow();
        model.addAttribute("usuario", usuario);
        model.addAttribute("roles", rolService.obtenerTodos());
        model.addAttribute("action", "delete");
        return "usuario/mant";
    }

    @PostMapping("/create")
    public String saveNuevo(@Valid @ModelAttribute Usuario usuario, BindingResult result,
                            RedirectAttributes redirect, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("action", "create");
            model.addAttribute("roles", rolService.obtenerTodos());
            return "usuario/mant";
        }
        try {
            usuarioService.crearOEditar(usuario);
        } catch (DataIntegrityViolationException e) {
            model.addAttribute("error", "Ya existe un usuario con ese email. Por favor, ingrese un correo diferente.");
            model.addAttribute("action", "create");
            model.addAttribute("roles", rolService.obtenerTodos());
            return "usuario/mant";
        }
        redirect.addFlashAttribute("msg", "Usuario creado correctamente");
        return "redirect:/usuarios";
    }

    @PostMapping("/edit")
    public String saveEditado(@Valid @ModelAttribute Usuario usuario, BindingResult result,
                              RedirectAttributes redirect, Model model) {

        if (usuario.getId() == null) {
            redirect.addFlashAttribute("error", "Error: No se puede editar un usuario sin ID.");
            return "redirect:/usuarios";
        }

        if (usuario.getContraseña() == null || usuario.getContraseña().trim().isEmpty()) {
            usuario.setContraseña(null);
        }

        if (result.hasErrors()) {
            model.addAttribute("action", "edit");
            model.addAttribute("roles", rolService.obtenerTodos());
            return "usuario/mant";
        }

        try {
            usuarioService.crearOEditar(usuario);
        } catch (DataIntegrityViolationException e) {
            model.addAttribute("error", "Ya existe un usuario con ese email. Por favor, ingrese un correo diferente.");
            model.addAttribute("action", "edit");
            model.addAttribute("roles", rolService.obtenerTodos());
            return "usuario/mant";
        }

        redirect.addFlashAttribute("msg", "Usuario editado correctamente");
        return "redirect:/usuarios";
    }

    @PostMapping("/delete")
    public String deleteUsuario(@ModelAttribute Usuario usuario, RedirectAttributes redirect) {
        usuarioService.eliminarPorId(usuario.getId());
        redirect.addFlashAttribute("msg", "Usuario eliminado correctamente");
        return "redirect:/usuarios";
    }

}