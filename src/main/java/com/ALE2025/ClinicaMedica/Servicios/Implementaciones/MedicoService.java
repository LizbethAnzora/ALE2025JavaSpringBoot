package com.ALE2025.ClinicaMedica.Servicios.Implementaciones;

import com.ALE2025.ClinicaMedica.Modelos.Medico;
import com.ALE2025.ClinicaMedica.Repositorios.IMedicoRepository;
import com.ALE2025.ClinicaMedica.Servicios.Interfaces.IMedicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
public class MedicoService implements IMedicoService {

    @Autowired
    private IMedicoRepository medicoRepository;

    private static final String UPLOAD_DIR = "src/main/resources/static/images/";

    @Override
    public Page<Medico> buscarTodosPaginados(Pageable pageable) {
        return medicoRepository.findAll(pageable);
    }

    @Override
    public List<Medico> obtenerTodos() {
        return medicoRepository.findAll();
    }

    @Override
    public Optional<Medico> buscarPorId(Integer id) {
        return medicoRepository.findById(id);
    }

    @Override
    public Medico crearOEditar(Medico medico, MultipartFile imagenFile) {
        if (imagenFile != null && !imagenFile.isEmpty()) {
            try {
                // Guarda la imagen en el servidor
                String originalFilename = imagenFile.getOriginalFilename();
                String filename = System.currentTimeMillis() + "_" + originalFilename;
                Path uploadPath = Paths.get(UPLOAD_DIR);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }
                Files.copy(imagenFile.getInputStream(), uploadPath.resolve(filename));
                medico.setImagenPath("/images/" + filename);
            } catch (IOException e) {
                // Manejo de errores en la carga de la imagen
                e.printStackTrace();
            }
        }
        return medicoRepository.save(medico);
    }

    @Override
    public void eliminarPorId(Integer id) {
        medicoRepository.deleteById(id);
    }
}