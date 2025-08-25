package com.ALE2025.ClinicaMedica.Controladores;


import com.ALE2025.ClinicaMedica.Modelos.Historial;
import com.ALE2025.ClinicaMedica.Servicios.Interfaces.IEspecialidadService;
import com.ALE2025.ClinicaMedica.Servicios.Interfaces.IHistorialService;
import com.ALE2025.ClinicaMedica.Servicios.Interfaces.IMedicoService;
import com.ALE2025.ClinicaMedica.Servicios.Interfaces.IPacienteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/historiales")
public class HistorialController {

    @Autowired
    private IHistorialService historialService;

    @Autowired
    private IPacienteService pacienteService;

    @Autowired
    private IMedicoService medicoService;

    @Autowired
    private IEspecialidadService especialidadService;

    @GetMapping
    public String index(Model model,
                        @RequestParam("page") Optional<Integer> page,
                        @RequestParam("size") Optional<Integer> size,
                        @RequestParam(value = "nombreMedico", required = false) String nombreMedico,
                        @RequestParam(value = "nombreEspecialidad", required = false) String nombreEspecialidad,
                        @RequestParam(value = "nombrePaciente", required = false) String nombrePaciente,
                        @RequestParam(value = "apellidoPaciente", required = false) String apellidoPaciente,
                        @RequestParam(value = "duiPaciente", required = false) String duiPaciente) {

        int currentPage = page.orElse(1) - 1;
        int pageSize = size.orElse(5);
        Pageable pageable = PageRequest.of(currentPage, pageSize);

        Page<Historial> historiales;
        boolean isFiltered = StringUtils.hasText(nombreMedico) ||
                             StringUtils.hasText(nombreEspecialidad) ||
                             StringUtils.hasText(nombrePaciente) ||
                             StringUtils.hasText(apellidoPaciente) ||
                             StringUtils.hasText(duiPaciente);

        if (isFiltered) {
            historiales = historialService.buscarPorFiltrosPaginado(
                    nombreMedico,
                    nombreEspecialidad,
                    nombrePaciente,
                    apellidoPaciente,
                    duiPaciente,
                    pageable);
        } else {
            historiales = historialService.buscarTodosPaginados(pageable);
        }

        model.addAttribute("historiales", historiales);
        model.addAttribute("nombreMedico", nombreMedico);
        model.addAttribute("nombreEspecialidad", nombreEspecialidad);
        model.addAttribute("nombrePaciente", nombrePaciente);
        model.addAttribute("apellidoPaciente", apellidoPaciente);
        model.addAttribute("duiPaciente", duiPaciente);

        int totalPages = historiales.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed().collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        return "historial/index";
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("historial", new Historial());
        model.addAttribute("pacientes", pacienteService.obtenerTodos());
        model.addAttribute("medicos", medicoService.obtenerTodos());
        model.addAttribute("especialidades", especialidadService.obtenerTodos());
        model.addAttribute("action", "create");
        return "historial/mant";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {
        Historial historial = historialService.buscarPorId(id).orElseThrow();
        model.addAttribute("historial", historial);
        model.addAttribute("pacientes", pacienteService.obtenerTodos());
        model.addAttribute("medicos", medicoService.obtenerTodos());
        model.addAttribute("especialidades", especialidadService.obtenerTodos());
        model.addAttribute("action", "edit");
        return "historial/mant";
    }

    @GetMapping("/view/{id}")
    public String view(@PathVariable Integer id, Model model) {
        Historial historial = historialService.buscarPorId(id).orElseThrow();
        model.addAttribute("historial", historial);
        model.addAttribute("pacientes", pacienteService.obtenerTodos());
        model.addAttribute("medicos", medicoService.obtenerTodos());
        model.addAttribute("especialidades", especialidadService.obtenerTodos());
        model.addAttribute("action", "view");
        return "historial/mant";
    }

    @GetMapping("/delete/{id}")
    public String deleteConfirm(@PathVariable Integer id, Model model) {
        Historial historial = historialService.buscarPorId(id).orElseThrow();
        model.addAttribute("historial", historial);
        model.addAttribute("pacientes", pacienteService.obtenerTodos());
        model.addAttribute("medicos", medicoService.obtenerTodos());
        model.addAttribute("especialidades", especialidadService.obtenerTodos());
        model.addAttribute("action", "delete");
        return "historial/mant";
    }

    @PostMapping("/create")
    public String saveNuevo(@Valid @ModelAttribute Historial historial, BindingResult result,
                            RedirectAttributes redirect, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("action", "create");
            model.addAttribute("pacientes", pacienteService.obtenerTodos());
            model.addAttribute("medicos", medicoService.obtenerTodos());
            model.addAttribute("especialidades", especialidadService.obtenerTodos());
            return "historial/mant";
        }
        historialService.crearOEditar(historial);
        redirect.addFlashAttribute("msg", "Historial creado correctamente");
        return "redirect:/historiales";
    }

    @PostMapping("/edit")
    public String saveEditado(@Valid @ModelAttribute Historial historial, BindingResult result,
                              RedirectAttributes redirect, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("action", "edit");
            model.addAttribute("pacientes", pacienteService.obtenerTodos());
            model.addAttribute("medicos", medicoService.obtenerTodos());
            model.addAttribute("especialidades", especialidadService.obtenerTodos());
            return "historial/mant";
        }
        historialService.crearOEditar(historial);
        redirect.addFlashAttribute("msg", "Historial actualizado correctamente");
        return "redirect:/historiales";
    }

    @PostMapping("/delete")
    public String deleteHistorial(@ModelAttribute Historial historial, RedirectAttributes redirect) {
        historialService.eliminarPorId(historial.getId());
        redirect.addFlashAttribute("msg", "Historial eliminado correctamente");
        return "redirect:/historiales";
    }
}