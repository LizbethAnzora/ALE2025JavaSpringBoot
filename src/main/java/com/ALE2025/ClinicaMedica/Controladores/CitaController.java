package com.ALE2025.ClinicaMedica.Controladores;

import com.ALE2025.ClinicaMedica.Modelos.Cita;
import com.ALE2025.ClinicaMedica.Modelos.Medico;
import com.ALE2025.ClinicaMedica.Modelos.Paciente;
import com.ALE2025.ClinicaMedica.Servicios.Interfaces.ICitaService;
import com.ALE2025.ClinicaMedica.Servicios.Interfaces.IMedicoService;
import com.ALE2025.ClinicaMedica.Servicios.Interfaces.IPacienteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/citas")
public class CitaController {

    @Autowired
    private ICitaService citaService;

    @Autowired
    private IPacienteService pacienteService;

    @Autowired
    private IMedicoService medicoService;

    @GetMapping
    public String index(Model model,
                        @RequestParam(value = "page", required = false) Optional<Integer> page,
                        @RequestParam(value = "size", required = false) Optional<Integer> size,
                        @RequestParam(value = "nombrePaciente", required = false) String nombrePaciente,
                        @RequestParam(value = "duiPaciente", required = false) String duiPaciente,
                        @RequestParam(value = "nombreMedico", required = false) String nombreMedico,
                        @RequestParam(value = "fechaCita", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaCita) {

        List<Cita> citas;

        // Verificar si hay algún filtro de búsqueda activo
        if ((nombrePaciente != null && !nombrePaciente.isEmpty()) || (duiPaciente != null && !duiPaciente.isEmpty()) ||
            (nombreMedico != null && !nombreMedico.isEmpty()) || fechaCita != null) {

            // Si hay filtros, realiza la búsqueda y no uses paginación
            citas = citaService.buscarCitas(nombrePaciente, duiPaciente, nombreMedico, fechaCita);
            model.addAttribute("citas", citas);
            model.addAttribute("citasPaginadas", null); // Asegurarse de que el objeto de paginación es nulo

        } else {
            // Si no hay filtros, usa la paginación como antes
            int currentPage = page.orElse(1) - 1;
            int pageSize = size.orElse(5);
            Pageable pageable = PageRequest.of(currentPage, pageSize);
            Page<Cita> citasPaginadas = citaService.buscarTodosPaginados(pageable);

            citas = citasPaginadas.getContent();
            model.addAttribute("citas", citas);
            model.addAttribute("citasPaginadas", citasPaginadas);

            int totalPages = citasPaginadas.getTotalPages();
            if (totalPages > 0) {
                List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                        .boxed().collect(Collectors.toList());
                model.addAttribute("pageNumbers", pageNumbers);
            }
        }

        // Mantener los valores de los filtros en la vista
        model.addAttribute("nombrePaciente", nombrePaciente);
        model.addAttribute("duiPaciente", duiPaciente);
        model.addAttribute("nombreMedico", nombreMedico);
        model.addAttribute("fechaCita", fechaCita);

        return "cita/index";
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("cita", new Cita());
        model.addAttribute("action", "create");
        List<Paciente> pacientes = pacienteService.obtenerTodos();
        List<Medico> medicos = medicoService.obtenerTodos();
        model.addAttribute("pacientes", pacientes);
        model.addAttribute("medicos", medicos);
        return "cita/mant";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {
        Cita cita = citaService.buscarPorId(id).orElseThrow();
        model.addAttribute("cita", cita);
        model.addAttribute("action", "edit");
        List<Paciente> pacientes = pacienteService.obtenerTodos();
        List<Medico> medicos = medicoService.obtenerTodos();
        model.addAttribute("pacientes", pacientes);
        model.addAttribute("medicos", medicos);
        return "cita/mant";
    }

    @GetMapping("/view/{id}")
    public String view(@PathVariable Integer id, Model model) {
        Cita cita = citaService.buscarPorId(id).orElseThrow();
        model.addAttribute("cita", cita);
        model.addAttribute("action", "view");
        model.addAttribute("pacientes", pacienteService.obtenerTodos());
        model.addAttribute("medicos", medicoService.obtenerTodos());
        return "cita/mant";
    }

    @GetMapping("/delete/{id}")
    public String deleteConfirm(@PathVariable Integer id, Model model) {
        Cita cita = citaService.buscarPorId(id).orElseThrow();
        model.addAttribute("cita", cita);
        model.addAttribute("action", "delete");
        model.addAttribute("pacientes", pacienteService.obtenerTodos());
        model.addAttribute("medicos", medicoService.obtenerTodos());
        return "cita/mant";
    }

    @PostMapping("/create")
    public String saveNuevo(@Valid @ModelAttribute Cita cita, BindingResult result,
                            RedirectAttributes redirect, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("action", "create");
            model.addAttribute("pacientes", pacienteService.obtenerTodos());
            model.addAttribute("medicos", medicoService.obtenerTodos());
            return "cita/mant";
        }
        citaService.crearOEditar(cita);
        redirect.addFlashAttribute("msg", "Cita creada correctamente");
        return "redirect:/citas";
    }

    @PostMapping("/edit")
    public String saveEditado(@Valid @ModelAttribute Cita cita, BindingResult result,
                              RedirectAttributes redirect, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("action", "edit");
            model.addAttribute("pacientes", pacienteService.obtenerTodos());
            model.addAttribute("medicos", medicoService.obtenerTodos());
            return "cita/mant";
        }
        citaService.crearOEditar(cita);
        redirect.addFlashAttribute("msg", "Cita editada correctamente");
        return "redirect:/citas";
    }

    @PostMapping("/delete")
    public String deleteCita(@ModelAttribute Cita cita, RedirectAttributes redirect) {
        citaService.eliminarPorId(cita.getId());
        redirect.addFlashAttribute("msg", "Cita eliminada correctamente");
        return "redirect:/citas";
    }

    @GetMapping("/medico/{id}/costo")
    @ResponseBody
    public Double getMedicoCosto(@PathVariable Integer id) {
        Optional<Medico> medico = medicoService.buscarPorId(id);
        return medico.map(Medico::getCostoConsulta).orElse(0.0);
    }
}