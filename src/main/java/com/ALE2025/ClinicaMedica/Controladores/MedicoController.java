package com.ALE2025.ClinicaMedica.Controladores;

import com.ALE2025.ClinicaMedica.Modelos.Medico;
import com.ALE2025.ClinicaMedica.Servicios.Interfaces.IMedicoService;
import com.ALE2025.ClinicaMedica.Servicios.Interfaces.IEspecialidadService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/medicos")
public class MedicoController {

    @Autowired
    private IMedicoService medicoService;

    @Autowired
    private IEspecialidadService especialidadService;

    @GetMapping
    public String index(Model model,
                        @RequestParam(value = "page", required = false) Optional<Integer> page,
                        @RequestParam(value = "size", required = false) Optional<Integer> size,
                        @RequestParam(value = "nombre", required = false) String nombre,
                        @RequestParam(value = "apellido", required = false) String apellido,
                        @RequestParam(value = "especialidad", required = false) String especialidad,
                        @RequestParam(value = "dui", required = false) String dui) {

        int currentPage = page.orElse(1) - 1;
        int pageSize = size.orElse(5);
        
        Page<Medico> medicos;
        
        // Determina si se está realizando una búsqueda con filtros
        if (nombre != null || apellido != null || especialidad != null || dui != null) {
            List<Medico> resultados = medicoService.buscarMedicos(nombre, apellido, especialidad, dui);
            medicos = new PageImpl<>(resultados); // Envuelve los resultados en un objeto Page
        } else {
            // Si no hay filtros, se utiliza la paginación normal
            Pageable pageable = PageRequest.of(currentPage, pageSize);
            medicos = medicoService.buscarTodosPaginados(pageable);
        }

        model.addAttribute("medicos", medicos);
        model.addAttribute("nombre", nombre);
        model.addAttribute("apellido", apellido);
        model.addAttribute("especialidad", especialidad);
        model.addAttribute("dui", dui);

        // Lógica de paginación solo si no se aplicaron filtros
        if (medicos.hasContent() && medicos.getTotalPages() > 0) {
            int totalPages = medicos.getTotalPages();
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed().collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        return "medico/index";
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("medico", new Medico());
        model.addAttribute("especialidades", especialidadService.obtenerTodos());
        model.addAttribute("generos", new String[]{"Masculino", "Femenino", "Otro"});
        model.addAttribute("action", "create");
        return "medico/mant";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {
        Medico medico = medicoService.buscarPorId(id).orElseThrow();
        model.addAttribute("medico", medico);
        model.addAttribute("especialidades", especialidadService.obtenerTodos());
        model.addAttribute("generos", new String[]{"Masculino", "Femenino", "Otro"});
        model.addAttribute("action", "edit");
        return "medico/mant";
    }

    @GetMapping("/view/{id}")
    public String view(@PathVariable Integer id, Model model) {
        Medico medico = medicoService.buscarPorId(id).orElseThrow();
        model.addAttribute("medico", medico);
        model.addAttribute("action", "view");
        return "medico/mant";
    }

    @GetMapping("/delete/{id}")
    public String deleteConfirm(@PathVariable Integer id, Model model) {
        Medico medico = medicoService.buscarPorId(id).orElseThrow();
        model.addAttribute("medico", medico);
        model.addAttribute("action", "delete");
        return "medico/mant";
    }

    @PostMapping("/create")
    public String saveNuevo(@Valid @ModelAttribute Medico medico, BindingResult result,
                            @RequestParam("imagenFile") MultipartFile imagenFile,
                            RedirectAttributes redirect, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("especialidades", especialidadService.obtenerTodos());
            model.addAttribute("generos", new String[]{"Masculino", "Femenino", "Otro"});
            model.addAttribute("action", "create");
            return "medico/mant";
        }
        try {
            medicoService.crearOEditar(medico, imagenFile);
        } catch (DataIntegrityViolationException e) {
            model.addAttribute("error", "Ya existe un médico con ese DUI. Por favor, ingrese un DUI diferente.");
            model.addAttribute("especialidades", especialidadService.obtenerTodos());
            model.addAttribute("generos", new String[]{"Masculino", "Femenino", "Otro"});
            model.addAttribute("action", "create");
            return "medico/mant";
        }
        redirect.addFlashAttribute("msg", "Médico creado correctamente");
        return "redirect:/medicos";
    }

    @PostMapping("/edit")
    public String saveEditado(@Valid @ModelAttribute Medico medico, BindingResult result,
                              @RequestParam("imagenFile") MultipartFile imagenFile,
                              RedirectAttributes redirect, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("especialidades", especialidadService.obtenerTodos());
            model.addAttribute("generos", new String[]{"Masculino", "Femenino", "Otro"});
            model.addAttribute("action", "edit");
            return "medico/mant";
        }
        try {
            medicoService.crearOEditar(medico, imagenFile);
        } catch (DataIntegrityViolationException e) {
            model.addAttribute("error", "Ya existe un médico con ese DUI. Por favor, ingrese un DUI diferente.");
            model.addAttribute("especialidades", especialidadService.obtenerTodos());
            model.addAttribute("generos", new String[]{"Masculino", "Femenino", "Otro"});
            model.addAttribute("action", "edit");
            return "medico/mant";
        }
        redirect.addFlashAttribute("msg", "Médico actualizado correctamente");
        return "redirect:/medicos";
    }

    @PostMapping("/delete")
    public String deleteMedico(@ModelAttribute Medico medico, RedirectAttributes redirect) {
        medicoService.eliminarPorId(medico.getId());
        redirect.addFlashAttribute("msg", "Médico eliminado correctamente");
        return "redirect:/medicos";
    }
}