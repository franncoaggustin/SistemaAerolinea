package com.aerolinea.SistemaAerolinea.service;

import com.aerolinea.SistemaAerolinea.dto.ClaseDTO;
import com.aerolinea.SistemaAerolinea.model.Clase;
import com.aerolinea.SistemaAerolinea.repository.ClaseRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Servicio para la gestión de clases de asiento.
 * Contiene la lógica de negocio y conversión a DTO.
 * Se conecta con ClaseRepository para acceder a la base de datos.
 */
@Service
public class ClaseService {

    @Autowired
    private ClaseRepository claseRepository;

    private final ModelMapper modelMapper = new ModelMapper();

    /**
     * Retorna la lista de todas las clases registradas.
     * @return lista de clases.
     */
    public List<Clase> listarClases() {
        return claseRepository.findAll();
    }

    /**
     * Retorna la lista de clases como DTOs para el frontend.
     * @return lista de ClaseDTO.
     */
    public List<ClaseDTO> listarDTO() {
        return claseRepository.findAll().stream()
                .map(c -> modelMapper.map(c, ClaseDTO.class))
                .collect(Collectors.toList());
    }

    /**
     * Busca una clase por su id.
     * @param id el id de la clase.
     * @return la clase encontrada o vacío si no existe.
     */
    public Optional<Clase> buscarPorId(Integer id) {
        return claseRepository.findById(id);
    }

    /**
     * Guarda una nueva clase o actualiza una existente.
     * @param clase la clase a guardar.
     * @return la clase guardada con su id generado.
     */
    public Clase guardar(Clase clase) {
        return claseRepository.save(clase);
    }

    /**
     * Elimina una clase por su id.
     * @param id el id de la clase a eliminar.
     */
    public void eliminar(Integer id) {
        claseRepository.deleteById(id);
    }
}