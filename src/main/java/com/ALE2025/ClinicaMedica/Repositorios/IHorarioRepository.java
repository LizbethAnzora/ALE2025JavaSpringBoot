package com.ALE2025.ClinicaMedica.Repositorios;

import com.ALE2025.ClinicaMedica.Modelos.Horario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IHorarioRepository extends JpaRepository<Horario, Integer> {
    
}
