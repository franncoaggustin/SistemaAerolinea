package com.aerolinea.SistemaAerolinea.service;

import com.aerolinea.SistemaAerolinea.dto.CiudadDTO;
import com.aerolinea.SistemaAerolinea.model.Ciudad;
import com.aerolinea.SistemaAerolinea.repository.CiudadRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Servicio para la gestión de ciudades.
 * Contiene la lógica de negocio y conversión a DTO.
 * Se conecta con CiudadRepository para acceder a la base de datos.
 */
@Service
public class CiudadService {

    @Autowired
    private CiudadRepository ciudadRepository;

    private final ModelMapper modelMapper = new ModelMapper();

    /**
     * Retorna la lista de todas las ciudades registradas.
     * @return lista de ciudades.
     */
    public List<Ciudad> listarCiudades() {
        return ciudadRepository.findAll();
    }

    /**
     * Retorna la lista de ciudades como DTOs para el frontend.
     * @return lista de CiudadDTO.
     */
    public List<CiudadDTO> listarDTO() {
        return ciudadRepository.findAll().stream()
                .map(c -> modelMapper.map(c, CiudadDTO.class))
                .collect(Collectors.toList());
    }

    /**
     * Busca una ciudad por su id.
     * @param id el id de la ciudad.
     * @return la ciudad encontrada o vacío si no existe.
     */
    public Optional<Ciudad> buscarPorId(Integer id) {
        return ciudadRepository.findById(id);
    }

    /**
     * Guarda una nueva ciudad o actualiza una existente.
     * @param ciudad la ciudad a guardar.
     * @return la ciudad guardada con su id generado.
     */
    public Ciudad guardar(Ciudad ciudad) {
        return ciudadRepository.save(ciudad);
    }

    /**
     * Elimina una ciudad por su id.
     * @param id el id de la ciudad a eliminar.
     */
    public void eliminar(Integer id) {
        ciudadRepository.deleteById(id);
    }
}