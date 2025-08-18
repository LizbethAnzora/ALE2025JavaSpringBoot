package com.ALE2025.ClinicaMedica.Controladores;

import java.util.*;
import java.util.stream.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ALE2025.ClinicaMedica.Modelos.Rol;
import com.ALE2025.ClinicaMedica.Servicios.Interfaces.IRolService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/roles")
public class RolController {

    @Autowired
    private IRolService rolService;

    @GetMapping
    public String index(Model model,
                        @RequestParam("page") Optional<Integer> page,
                        @RequestParam("size") Optional<Integer> size) {

        int currentPage = page.orElse(1) - 1;
        int pageSize = size.orElse(5);
        Pageable pageable = PageRequest.of(currentPage, pageSize);

        Page<Rol> roles = rolService.buscarTodosPaginados(pageable);
        model.addAttribute("roles", roles);

        int totalPages = roles.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed().collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        return "rol/index";
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("rol", new Rol());
        model.addAttribute("action", "create");
        return "rol/mant";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {
        Rol rol = rolService.buscarPorId(id).orElseThrow();
        model.addAttribute("rol", rol);
        model.addAttribute("action", "edit");
        return "rol/mant";
    }

    @GetMapping("/view/{id}")
    public String view(@PathVariable Integer id, Model model) {
        Rol rol = rolService.buscarPorId(id).orElseThrow();
        model.addAttribute("rol", rol);
        model.addAttribute("action", "view");
        return "rol/mant";
    }

    @GetMapping("/delete/{id}")
    public String deleteConfirm(@PathVariable Integer id, Model model) {
        Rol rol = rolService.buscarPorId(id).orElseThrow();
        model.addAttribute("rol", rol);
        model.addAttribute("action", "delete");
        return "rol/mant";
    }

    @PostMapping("/create")
    public String saveNuevo(@Valid @ModelAttribute Rol rol, BindingResult result,
                            RedirectAttributes redirect, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("action", "create");
            return "rol/mant";
        }
        rolService.crearOEditar(rol);
        redirect.addFlashAttribute("msg", "Rol creado correctamente");
        return "redirect:/roles";
    }

    @PostMapping("/edit")
    public String saveEditado(@Valid @ModelAttribute Rol rol, BindingResult result,
                              RedirectAttributes redirect, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("action", "edit");
            return "rol/mant";
        }
        rolService.crearOEditar(rol);
        redirect.addFlashAttribute("msg", "Rol actualizado correctamente");
        return "redirect:/roles";
    }

    @PostMapping("/delete")
    public String deleteRol(@ModelAttribute Rol rol, RedirectAttributes redirect) {
        rolService.eliminarPorId(rol.getId());
        redirect.addFlashAttribute("msg", "Rol eliminado correctamente");
        return "redirect:/roles";
    }
}