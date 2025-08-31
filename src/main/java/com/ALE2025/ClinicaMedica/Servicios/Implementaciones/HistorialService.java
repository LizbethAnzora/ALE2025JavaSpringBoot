package com.ALE2025.ClinicaMedica.Servicios.Implementaciones;

import com.ALE2025.ClinicaMedica.Modelos.Historial;
import com.ALE2025.ClinicaMedica.Repositorios.IHistorialRepository;
import com.ALE2025.ClinicaMedica.Servicios.Interfaces.IHistorialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HistorialService implements IHistorialService {

    @Autowired
    private IHistorialRepository historialRepository;

    @Override
    public Page<Historial> buscarTodosPaginados(Pageable pageable) {
        return historialRepository.findAll(pageable);
    }

    @Override
    public Page<Historial> buscarPorFiltrosPaginado(
            String nombreMedico,
            String nombreEspecialidad,
            String nombrePaciente,
            String apellidoPaciente,
            String duiPaciente,
            Pageable pageable) {
        return historialRepository.findByFilters(
                nombreMedico,
                nombreEspecialidad,
                nombrePaciente,
                apellidoPaciente,
                duiPaciente,
                pageable);
    }

    @Override
    public List<Historial> obtenerTodos() {
        return historialRepository.findAll();
    }

    @Override
    public Optional<Historial> buscarPorId(Integer id) {
        return historialRepository.findById(id);
    }

    @Override
    public Historial crearOEditar(Historial historial) {
        return historialRepository.save(historial);
    }

    @Override
    public void eliminarPorId(Integer id) {
        historialRepository.deleteById(id);
    }

    @Override
    public List<Historial> buscarPorFiltros(
            String nombreMedico,
            String nombreEspecialidad,
            String nombrePaciente,
            String apellidoPaciente,
            String duiPaciente) {
        return historialRepository.findByFiltersWithoutPagination(
                nombreMedico,
                nombreEspecialidad,
                nombrePaciente,
                apellidoPaciente,
                duiPaciente);
    }
}