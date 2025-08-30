package com.ALE2025.ClinicaMedica.Servicios.Implementaciones;

import com.ALE2025.ClinicaMedica.Modelos.Cita;
import com.ALE2025.ClinicaMedica.Repositorios.ICitaRepository;
import com.ALE2025.ClinicaMedica.Servicios.Interfaces.ICitaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CitaService implements ICitaService {

    @Autowired
    private ICitaRepository citaRepository;

    @Override
    public Page<Cita> buscarTodosPaginados(Pageable pageable) {
        return citaRepository.findAll(pageable);
    }

    @Override
    public List<Cita> obtenerTodos() {
        return citaRepository.findAll();
    }

    @Override
    public Optional<Cita> buscarPorId(Integer id) {
        return citaRepository.findById(id);
    }

    @Override
    public Cita crearOEditar(Cita cita) {
        return citaRepository.save(cita);
    }

    @Override
    public void eliminarPorId(Integer id) {
        citaRepository.deleteById(id);
    }
    
    @Override
    public List<Cita> buscarCitas(String nombrePaciente, String duiPaciente, String nombreMedico, Date fechaCita) {
        return citaRepository.buscarConFiltros(nombrePaciente, duiPaciente, nombreMedico, fechaCita);
    }
}