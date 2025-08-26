package com.ALE2025.ClinicaMedica.Repositorios;

import com.ALE2025.ClinicaMedica.Modelos.Paciente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPacienteRepository extends JpaRepository<Paciente, Integer> {
    
    // Método para buscar por nombre, apellido o DUI, con paginación
    Page<Paciente> findByNombreContainingIgnoreCaseAndApellidoContainingIgnoreCaseAndDUIContainingIgnoreCaseAndTelefonoContainingIgnoreCase(
            String nombre, String apellido, String dui, String telefono, Pageable pageable);
}