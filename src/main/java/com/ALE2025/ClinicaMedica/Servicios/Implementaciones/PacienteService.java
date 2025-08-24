package com.ALE2025.ClinicaMedica.Servicios.Implementaciones;

import com.ALE2025.ClinicaMedica.Modelos.Paciente;
import com.ALE2025.ClinicaMedica.Repositorios.IPacienteRepository;
import com.ALE2025.ClinicaMedica.Servicios.Interfaces.IPacienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PacienteService implements IPacienteService {

    @Autowired
    private IPacienteRepository pacienteRepository;

    @Override
    public Page<Paciente> buscarTodosPaginados(Pageable pageable) {
        return pacienteRepository.findAll(pageable);
    }
    
    @Override
    public List<Paciente> obtenerTodos() {
        return pacienteRepository.findAll();
    }

    @Override
    public Optional<Paciente> buscarPorId(Integer id) {
        return pacienteRepository.findById(id);
    }

    @Override
    public Paciente crearOEditar(Paciente paciente) {
        return pacienteRepository.save(paciente);
    }

    @Override
    public void eliminarPorId(Integer id) {
        pacienteRepository.deleteById(id);
    }
}