package com.ALE2025.ClinicaMedica.Servicios.Implementaciones;

import com.ALE2025.ClinicaMedica.Modelos.Horario;
import com.ALE2025.ClinicaMedica.Repositorios.IHorarioRepository;
import com.ALE2025.ClinicaMedica.Servicios.Interfaces.IHorarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class HorarioService implements IHorarioService {

    @Autowired
    private IHorarioRepository horarioRepository;

    @Override
    public Page<Horario> buscarTodosPaginados(Pageable pageable) {
        return horarioRepository.findAll(pageable);
    }
    
    @Override
    public Page<Horario> buscarPorFiltrosPaginado(
            String nombreMedico,
            String apellidoMedico,
            String nombreEspecialidad,
            Pageable pageable) {
        return horarioRepository.findByFilters(
                nombreMedico,
                apellidoMedico,
                nombreEspecialidad,
                pageable);
    }

    @Override
    public List<Horario> obtenerTodos() {
        return horarioRepository.findAll();
    }

    @Override
    public Optional<Horario> buscarPorId(Integer id) {
        return horarioRepository.findById(id);
    }

    @Override
    public Horario crearOEditar(Horario horario) {
        return horarioRepository.save(horario);
    }

    @Override
    public void eliminarPorId(Integer id) {
        horarioRepository.deleteById(id);
    }
}