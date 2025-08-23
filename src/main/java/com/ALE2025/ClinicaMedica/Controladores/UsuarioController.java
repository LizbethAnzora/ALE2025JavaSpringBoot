package com.ALE2025.ClinicaMedica.Controladores;

import com.ALE2025.ClinicaMedica.Modelos.Rol;
import com.ALE2025.ClinicaMedica.Modelos.Usuario;
import com.ALE2025.ClinicaMedica.Servicios.Interfaces.IRolService;
import com.ALE2025.ClinicaMedica.Servicios.Interfaces.IUsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private IUsuarioService usuarioService;

    @Autowired
    private IRolService rolService;

    @GetMapping
    public String listar(Model model,
                         @RequestParam(defaultValue = "0") int page,
                         @RequestParam(defaultValue = "5") int size,
                         @RequestParam(required = false) String keyword) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Usuario> usuarios;

        if (keyword != null && !keyword.isEmpty()) {
            usuarios = usuarioService.buscarPorNombreOEmailPaginado(keyword, pageable);
        } else {
            usuarios = usuarioService.buscarTodosPaginados(pageable);
        }

        model.addAttribute("usuarios", usuarios);
        model.addAttribute("currentPage", page);
        model.addAttribute("keyword", keyword);
        model.addAttribute("pageNumbers", IntStream.rangeClosed(0, usuarios.getTotalPages() - 1).boxed().collect(Collectors.toList()));

        return "usuario/index";
    }

    @GetMapping("/create")
    public String crear(Model model) {
        model.addAttribute("usuario", new Usuario());
        model.addAttribute("action", "create");
        List<Rol> roles = rolService.obtenerTodos();
        model.addAttribute("roles", roles);
        return "usuario/mant";
    }

    @PostMapping("/create")
    public String guardar(@Valid Usuario usuario, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("action", "create");
            model.addAttribute("roles", rolService.obtenerTodos());
            return "usuario/mant";
        }
        usuarioService.crearOEditar(usuario);
        redirectAttributes.addFlashAttribute("msg", "Usuario guardado exitosamente");
        return "redirect:/usuarios";
    }

    @GetMapping("/edit/{id}")
    public String editar(@PathVariable Integer id, Model model) {
        Optional<Usuario> usuario = usuarioService.buscarPorId(id);
        if (usuario.isPresent()) {
            model.addAttribute("usuario", usuario.get());
            model.addAttribute("action", "edit");
            List<Rol> roles = rolService.obtenerTodos();
            model.addAttribute("roles", roles);
            return "usuario/mant";
        }
        return "redirect:/usuarios";
    }

    @PostMapping("/edit")
    public String actualizar(@Valid Usuario usuario, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("action", "edit");
            model.addAttribute("roles", rolService.obtenerTodos());
            return "usuario/mant";
        }
        usuarioService.crearOEditar(usuario);
        redirectAttributes.addFlashAttribute("msg", "Usuario actualizado exitosamente");
        return "redirect:/usuarios";
    }

    @GetMapping("/view/{id}")
    public String ver(@PathVariable Integer id, Model model) {
        Optional<Usuario> usuario = usuarioService.buscarPorId(id);
        if (usuario.isPresent()) {
            model.addAttribute("usuario", usuario.get());
            model.addAttribute("action", "view");
            return "usuario/mant";
        }
        return "redirect:/usuarios";
    }

    @GetMapping("/delete/{id}")
    public String eliminar(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        try {
            usuarioService.eliminarPorId(id);
            redirectAttributes.addFlashAttribute("msg", "Usuario eliminado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar el usuario");
        }
        return "redirect:/usuarios";
    }
}