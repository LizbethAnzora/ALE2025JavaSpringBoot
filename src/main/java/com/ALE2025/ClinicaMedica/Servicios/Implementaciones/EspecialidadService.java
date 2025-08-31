package com.ALE2025.ClinicaMedica.Servicios.Implementaciones;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ALE2025.ClinicaMedica.Modelos.Especialidad;
import com.ALE2025.ClinicaMedica.Repositorios.IEspecialidadRepository;
import com.ALE2025.ClinicaMedica.Servicios.Interfaces.IEspecialidadService;

@Service
public class EspecialidadService implements IEspecialidadService {

    @Autowired
    private IEspecialidadRepository especialidadRepository;

    @Override
    public Page<Especialidad> buscarTodosPaginados(Pageable pageable) {
        return especialidadRepository.findAll(pageable);
    }

    @Override
    public List<Especialidad> obtenerTodos() {
        return especialidadRepository.findAll();
    }

    @Override
    public Optional<Especialidad> buscarPorId(Integer id) {
        return especialidadRepository.findById(id);
    }

    // Método corregido
    @Override
    public Especialidad crearOEditar(Especialidad especialidad) {
        return especialidadRepository.save(especialidad);
    }

    @Override
    public void eliminarPorId(Integer id) {
        especialidadRepository.deleteById(id);
    }

    @Override
    public boolean existeEspecialidadConNombre(String nombre) {
        return especialidadRepository.findByNombre(nombre).isPresent();
    }
}