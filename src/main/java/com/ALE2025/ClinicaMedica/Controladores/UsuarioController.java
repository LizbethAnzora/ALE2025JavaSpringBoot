package com.ALE2025.ClinicaMedica.Controladores;

import java.util.*;
import java.util.stream.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ALE2025.ClinicaMedica.Modelos.Usuario;
import com.ALE2025.ClinicaMedica.Servicios.Interfaces.IUsuarioService;
import com.ALE2025.ClinicaMedica.Servicios.Interfaces.IRolService; // Necesario para obtener la lista de roles

import jakarta.validation.Valid;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private IUsuarioService usuarioService;

    @Autowired
    private IRolService rolService; // Inyectamos el servicio de roles para el dropdown

    @GetMapping
    public String index(Model model,
                        @RequestParam("page") Optional<Integer> page,
                        @RequestParam("size") Optional<Integer> size) {

        int currentPage = page.orElse(1) - 1;
        int pageSize = size.orElse(5);
        Pageable pageable = PageRequest.of(currentPage, pageSize);

        Page<Usuario> usuarios = usuarioService.buscarTodosPaginados(pageable);
        model.addAttribute("usuarios", usuarios);

        int totalPages = usuarios.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed().collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

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
        redirect.addFlashAttribute("msg", "Usuario actualizado correctamente");
        return "redirect:/usuarios";
    }

    @PostMapping("/delete")
    public String deleteUsuario(@ModelAttribute Usuario usuario, RedirectAttributes redirect) {
        usuarioService.eliminarPorId(usuario.getId());
        redirect.addFlashAttribute("msg", "Usuario eliminado correctamente");
        return "redirect:/usuarios";
    }
}