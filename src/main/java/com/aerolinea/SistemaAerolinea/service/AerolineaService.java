package com.aerolinea.SistemaAerolinea.service;

import com.aerolinea.SistemaAerolinea.dto.AerolineaDTO;
import com.aerolinea.SistemaAerolinea.model.Aerolinea;
import com.aerolinea.SistemaAerolinea.repository.AerolineaRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Servicio para la gestión de aerolíneas.
 * Contiene la lógica de negocio y conversión a DTO.
 * Se conecta con AerolineaRepository para acceder a la base de datos.
 */
@Service
public class AerolineaService {

    @Autowired
    private AerolineaRepository aerolineaRepository;

    private final ModelMapper modelMapper = new ModelMapper();

    /**
     * Retorna la lista de todas las aerolíneas registradas.
     * @return lista de aerolíneas.
     */
    public List<Aerolinea> listarAerolineas() {
        return aerolineaRepository.findAll();
    }

    /**
     * Retorna la lista de aerolíneas como DTOs para el frontend.
     * @return lista de AerolineaDTO.
     */
    public List<AerolineaDTO> listarDTO() {
        return aerolineaRepository.findAll().stream()
                .map(a -> modelMapper.map(a, AerolineaDTO.class))
                .collect(Collectors.toList());
    }

    /**
     * Busca una aerolínea por su id.
     * @param id el id de la aerolínea.
     * @return la aerolínea encontrada o vacío si no existe.
     */
    public Optional<Aerolinea> buscarPorId(Integer id) {
        return aerolineaRepository.findById(id);
    }

    /**
     * Guarda una nueva aerolínea o actualiza una existente.
     * @param aerolinea la aerolínea a guardar.
     * @return la aerolínea guardada con su id generado.
     */
    public Aerolinea guardar(Aerolinea aerolinea) {
        return aerolineaRepository.save(aerolinea);
    }

    /**
     * Elimina una aerolínea por su id.
     * @param id el id de la aerolínea a eliminar.
     */
    public void eliminar(Integer id) {
        aerolineaRepository.deleteById(id);
    }
}