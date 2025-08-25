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

import com.ALE2025.ClinicaMedica.Modelos.Especialidad;
import com.ALE2025.ClinicaMedica.Servicios.Interfaces.IEspecialidadService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/especialidades")
public class EspecialidadController {

    @Autowired
    private IEspecialidadService especialidadService;

    @GetMapping
    public String index(Model model,
                        @RequestParam("page") Optional<Integer> page,
                        @RequestParam("size") Optional<Integer> size) {

        int currentPage = page.orElse(1) - 1;
        int pageSize = size.orElse(5);
        Pageable pageable = PageRequest.of(currentPage, pageSize);

        Page<Especialidad> especialidades = especialidadService.buscarTodosPaginados(pageable);
        model.addAttribute("especialidades", especialidades);

        int totalPages = especialidades.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed().collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        return "especialidad/index";
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("especialidad", new Especialidad());
        model.addAttribute("action", "create");
        return "especialidad/mant";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {
        Especialidad especialidad = especialidadService.buscarPorId(id).orElseThrow();
        model.addAttribute("especialidad", especialidad);
        model.addAttribute("action", "edit");
        return "especialidad/mant";
    }

    @GetMapping("/view/{id}")
    public String view(@PathVariable Integer id, Model model) {
        Especialidad especialidad = especialidadService.buscarPorId(id).orElseThrow();
        model.addAttribute("especialidad", especialidad);
        model.addAttribute("action", "view");
        return "especialidad/mant";
    }

    @GetMapping("/delete/{id}")
    public String deleteConfirm(@PathVariable Integer id, Model model) {
        Especialidad especialidad = especialidadService.buscarPorId(id).orElseThrow();
        model.addAttribute("especialidad", especialidad);
        model.addAttribute("action", "delete");
        return "especialidad/mant";
    }

    @PostMapping("/create")
    public String saveNuevo(@Valid @ModelAttribute Especialidad especialidad, BindingResult result,
                            RedirectAttributes redirect, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("action", "create");
            return "especialidad/mant";
        }

        if (especialidadService.existeEspecialidadConNombre(especialidad.getNombre())) {
            model.addAttribute("error", "El nombre de la especialidad ya existe. Por favor, ingrese un nombre diferente.");
            model.addAttribute("action", "create");
            return "especialidad/mant";
        }
        
        especialidadService.crearOEditar(especialidad);
        redirect.addFlashAttribute("msg", "Especialidad creada correctamente");
        return "redirect:/especialidades";
    }

    @PostMapping("/edit")
    public String saveEditado(@Valid @ModelAttribute Especialidad especialidad, BindingResult result,
                              RedirectAttributes redirect, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("action", "edit");
            return "especialidad/mant";
        }

        Optional<Especialidad> especialidadExistente = especialidadService.buscarPorId(especialidad.getId());
        if (especialidadExistente.isPresent() && !especialidadExistente.get().getNombre().equals(especialidad.getNombre())) {
            if (especialidadService.existeEspecialidadConNombre(especialidad.getNombre())) {
                model.addAttribute("error", "El nombre de la especialidad ya existe. Por favor, ingrese un nombre diferente.");
                model.addAttribute("action", "edit");
                return "especialidad/mant";
            }
        }
        
        especialidadService.crearOEditar(especialidad);
        redirect.addFlashAttribute("msg", "Especialidad actualizada correctamente");
        return "redirect:/especialidades";
    }

    @PostMapping("/delete")
    public String deleteEspecialidad(@ModelAttribute Especialidad especialidad, RedirectAttributes redirect) {
        especialidadService.eliminarPorId(especialidad.getId());
        redirect.addFlashAttribute("msg", "Especialidad eliminada correctamente");
        return "redirect:/especialidades";
    }
}