package com.ALE2025.ClinicaMedica.Controladores;

import com.ALE2025.ClinicaMedica.Modelos.Historial;
import com.ALE2025.ClinicaMedica.Servicios.Interfaces.IEspecialidadService;
import com.ALE2025.ClinicaMedica.Servicios.Interfaces.IHistorialService;
import com.ALE2025.ClinicaMedica.Servicios.Interfaces.IMedicoService;
import com.ALE2025.ClinicaMedica.Servicios.Interfaces.IPacienteService;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
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

    // Métodos CRUD y de paginación
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

    // Nuevo método para generar el PDF
    @GetMapping("/reporte/pdf")
    public ResponseEntity<byte[]> generatePdfReport(
            @RequestParam(value = "nombreMedico", required = false) String nombreMedico,
            @RequestParam(value = "nombreEspecialidad", required = false) String nombreEspecialidad,
            @RequestParam(value = "nombrePaciente", required = false) String nombrePaciente,
            @RequestParam(value = "apellidoPaciente", required = false) String apellidoPaciente,
            @RequestParam(value = "duiPaciente", required = false) String duiPaciente) {

        // 1. Obtener la lista de historiales según los filtros
        List<Historial> historiales;
        boolean isFiltered = StringUtils.hasText(nombreMedico) ||
                             StringUtils.hasText(nombreEspecialidad) ||
                             StringUtils.hasText(nombrePaciente) ||
                             StringUtils.hasText(apellidoPaciente) ||
                             StringUtils.hasText(duiPaciente);

        if (isFiltered) {
            historiales = historialService.buscarPorFiltros(
                    nombreMedico,
                    nombreEspecialidad,
                    nombrePaciente,
                    apellidoPaciente,
                    duiPaciente);
        } else {
            historiales = historialService.obtenerTodos();
        }

        // 2. Generar el PDF
        byte[] pdfBytes = generateHistorialReport(historiales);

        // 3. Devolver la respuesta como PDF para que se visualice
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("inline", "reporte_historial.pdf"); // "inline" para visualizar en el navegador
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }
    
    // Método auxiliar para la creación del PDF (con iText)
    private byte[] generateHistorialReport(List<Historial> historiales) {
    Document document = new Document(PageSize.A4);
    ByteArrayOutputStream out = new ByteArrayOutputStream();

    try {
        PdfWriter.getInstance(document, out);
        document.open();

        // 1. Añadir el logo y el título
        // Asegúrate de que la ruta de la imagen sea correcta. Por defecto, está en 'src/main/resources/static/assets/img/'
        Image logo = Image.getInstance(new ClassPathResource("static/assets/img/7fac475e1a4af4071f103cd3beb8cebb.jpg").getURL());
        logo.scaleAbsolute(80f, 80f);
        logo.setAlignment(Element.ALIGN_CENTER);
        document.add(logo);

        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLACK);
        Paragraph title = new Paragraph("Reporte de Historiales Médicos", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        document.add(Chunk.NEWLINE);

        if (historiales.isEmpty()) {
            Paragraph noData = new Paragraph("No se encontraron historiales para los filtros seleccionados.",
                    FontFactory.getFont(FontFactory.HELVETICA, 12));
            document.add(noData);
        } else {
            // 2. Crear una tabla con los datos del historial
            PdfPTable table = new PdfPTable(5); // 5 columnas
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);

            // Anchos de las columnas (opcional)
            float[] columnWidths = {2f, 2f, 2f, 1.5f, 3f};
            table.setWidths(columnWidths);

            // Encabezados de la tabla
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, BaseColor.WHITE);
            BaseColor headerBgColor = new BaseColor(46, 64, 83); // Un color oscuro para el fondo del encabezado

            addHeaderCell(table, "Paciente", headerFont, headerBgColor);
            addHeaderCell(table, "Médico", headerFont, headerBgColor);
            addHeaderCell(table, "Especialidad", headerFont, headerBgColor);
            addHeaderCell(table, "Fecha", headerFont, headerBgColor);
            addHeaderCell(table, "Observaciones", headerFont, headerBgColor);

            // Filas de la tabla
            Font cellFont = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.BLACK);
            for (Historial historial : historiales) {
                table.addCell(createCell(historial.getPaciente().getNombre() + " " + historial.getPaciente().getApellido(), cellFont));
                table.addCell(createCell(historial.getMedico().getNombre() + " " + historial.getMedico().getApellido(), cellFont));
                table.addCell(createCell(historial.getEspecialidad().getNombre(), cellFont));
                table.addCell(createCell(historial.getFecha_cita().toString(), cellFont));
                table.addCell(createCell(historial.getObservaciones(), cellFont));
            }

            document.add(table);
        }

        document.close();
    } catch (DocumentException | IOException e) {
        e.printStackTrace();
    }
    return out.toByteArray();
}

// Métodos auxiliares para la tabla
private void addHeaderCell(PdfPTable table, String text, Font font, BaseColor color) {
    PdfPCell headerCell = new PdfPCell(new Phrase(text, font));
    headerCell.setBackgroundColor(color);
    headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
    headerCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
    headerCell.setPadding(8);
    table.addCell(headerCell);
}

private PdfPCell createCell(String text, Font font) {
    PdfPCell cell = new PdfPCell(new Phrase(text, font));
    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
    cell.setPadding(6);
    return cell;
}
    
    // El resto de tus métodos (create, edit, view, delete, etc.)
    // ...
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