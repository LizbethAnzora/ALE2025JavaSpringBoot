package com.ALE2025.ClinicaMedica.Controladores;

import com.ALE2025.ClinicaMedica.Modelos.Horario;
import com.ALE2025.ClinicaMedica.Modelos.DiaSemana;
import com.ALE2025.ClinicaMedica.Servicios.Interfaces.IHorarioService;
import com.ALE2025.ClinicaMedica.Servicios.Interfaces.IMedicoService;
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
import java.util.Arrays;

@Controller
@RequestMapping("/horarios")
public class HorarioController {

    @Autowired
    private IHorarioService horarioService;

    @Autowired
    private IMedicoService medicoService;

    @GetMapping
    public String index(Model model,
                        @RequestParam("page") Optional<Integer> page,
                        @RequestParam("size") Optional<Integer> size) {

        int currentPage = page.orElse(1) - 1;
        int pageSize = size.orElse(5);
        Pageable pageable = PageRequest.of(currentPage, pageSize);

        Page<Horario> horarios = horarioService.buscarTodosPaginados(pageable);
        model.addAttribute("horarios", horarios);

        int totalPages = horarios.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed().collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        return "horario/index";
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("horario", new Horario());
        model.addAttribute("medicos", medicoService.obtenerTodos());
        model.addAttribute("diasSemana", DiaSemana.values());
        model.addAttribute("action", "create");
        return "horario/mant";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {
        Horario horario = horarioService.buscarPorId(id).orElseThrow();
        model.addAttribute("horario", horario);
        model.addAttribute("medicos", medicoService.obtenerTodos());
        model.addAttribute("diasSemana", DiaSemana.values());
        model.addAttribute("action", "edit");
        return "horario/mant";
    }

    @GetMapping("/view/{id}")
    public String view(@PathVariable Integer id, Model model) {
        Horario horario = horarioService.buscarPorId(id).orElseThrow();
        model.addAttribute("horario", horario);
        model.addAttribute("action", "view");
        return "horario/mant";
    }

    @GetMapping("/delete/{id}")
    public String deleteConfirm(@PathVariable Integer id, Model model) {
        Horario horario = horarioService.buscarPorId(id).orElseThrow();
        model.addAttribute("horario", horario);
        model.addAttribute("action", "delete");
        return "horario/mant";
    }

    @PostMapping("/create")
    public String saveNuevo(@Valid @ModelAttribute Horario horario, BindingResult result,
                            RedirectAttributes redirect, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("medicos", medicoService.obtenerTodos());
            model.addAttribute("diasSemana", DiaSemana.values());
            model.addAttribute("action", "create");
            return "horario/mant";
        }
        horarioService.crearOEditar(horario);
        redirect.addFlashAttribute("msg", "Horario creado correctamente");
        return "redirect:/horarios";
    }

    @PostMapping("/edit")
    public String saveEditado(@Valid @ModelAttribute Horario horario, BindingResult result,
                              RedirectAttributes redirect, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("medicos", medicoService.obtenerTodos());
            model.addAttribute("diasSemana", DiaSemana.values());
            model.addAttribute("action", "edit");
            return "horario/mant";
        }
        horarioService.crearOEditar(horario);
        redirect.addFlashAttribute("msg", "Horario actualizado correctamente");
        return "redirect:/horarios";
    }

    @PostMapping("/delete")
    public String deleteHorario(@ModelAttribute Horario horario, RedirectAttributes redirect) {
        horarioService.eliminarPorId(horario.getId());
        redirect.addFlashAttribute("msg", "Horario eliminado correctamente");
        return "redirect:/horarios";
    }
}