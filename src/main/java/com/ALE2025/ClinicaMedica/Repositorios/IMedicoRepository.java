package com.ALE2025.ClinicaMedica.Repositorios;

import com.ALE2025.ClinicaMedica.Modelos.Medico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IMedicoRepository extends JpaRepository<Medico, Integer> {
    
}