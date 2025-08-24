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

import com.ALE2025.ClinicaMedica.Modelos.Paciente;
import com.ALE2025.ClinicaMedica.Servicios.Interfaces.IPacienteService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/pacientes")
public class PacienteController {

    @Autowired
    private IPacienteService pacienteService;

    @GetMapping
    public String index(Model model,
                        @RequestParam("page") Optional<Integer> page,
                        @RequestParam("size") Optional<Integer> size) {

        int currentPage = page.orElse(1) - 1;
        int pageSize = size.orElse(5);
        Pageable pageable = PageRequest.of(currentPage, pageSize);

        Page<Paciente> pacientes = pacienteService.buscarTodosPaginados(pageable);
        model.addAttribute("pacientes", pacientes);

        int totalPages = pacientes.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed().collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        return "paciente/index";
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("paciente", new Paciente());
        model.addAttribute("action", "create");
        return "paciente/mant";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {
        Paciente paciente = pacienteService.buscarPorId(id).orElseThrow();
        model.addAttribute("paciente", paciente);
        model.addAttribute("action", "edit");
        return "paciente/mant";
    }

    @GetMapping("/view/{id}")
    public String view(@PathVariable Integer id, Model model) {
        Paciente paciente = pacienteService.buscarPorId(id).orElseThrow();
        model.addAttribute("paciente", paciente);
        model.addAttribute("action", "view");
        return "paciente/mant";
    }

    @GetMapping("/delete/{id}")
    public String deleteConfirm(@PathVariable Integer id, Model model) {
        Paciente paciente = pacienteService.buscarPorId(id).orElseThrow();
        model.addAttribute("paciente", paciente);
        model.addAttribute("action", "delete");
        return "paciente/mant";
    }

    @PostMapping("/create")
    public String saveNuevo(@Valid @ModelAttribute Paciente paciente, BindingResult result,
                            RedirectAttributes redirect, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("action", "create");
            return "paciente/mant";
        }
        try {
            pacienteService.crearOEditar(paciente);
        } catch (DataIntegrityViolationException e) {
            model.addAttribute("error", "Ya existe un paciente con ese DUI. Por favor, ingrese un DUI diferente.");
            model.addAttribute("action", "create");
            return "paciente/mant";
        }
        redirect.addFlashAttribute("msg", "Paciente creado correctamente");
        return "redirect:/pacientes";
    }

    @PostMapping("/edit")
    public String saveEditado(@Valid @ModelAttribute Paciente paciente, BindingResult result,
                              RedirectAttributes redirect, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("action", "edit");
            return "paciente/mant";
        }
        try {
            pacienteService.crearOEditar(paciente);
        } catch (DataIntegrityViolationException e) {
            model.addAttribute("error", "Ya existe un paciente con ese DUI. Por favor, ingrese un DUI diferente.");
            model.addAttribute("action", "edit");
            return "paciente/mant";
        }
        redirect.addFlashAttribute("msg", "Paciente actualizado correctamente");
        return "redirect:/pacientes";
    }

    @PostMapping("/delete")
    public String deletePaciente(@ModelAttribute Paciente paciente, RedirectAttributes redirect) {
        pacienteService.eliminarPorId(paciente.getId());
        redirect.addFlashAttribute("msg", "Paciente eliminado correctamente");
        return "redirect:/pacientes";
    }
}