package com.aerolinea.SistemaAerolinea.service;

import com.aerolinea.SistemaAerolinea.dto.AeropuertoDTO;
import com.aerolinea.SistemaAerolinea.model.Aeropuerto;
import com.aerolinea.SistemaAerolinea.repository.AeropuertoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Servicio para la gestión de aeropuertos.
 * Contiene la lógica de negocio y conversión a DTO.
 * Se conecta con AeropuertoRepository para acceder a la base de datos.
 */
@Service
public class AeropuertoService {

    @Autowired
    private AeropuertoRepository aeropuertoRepository;

    private final ModelMapper modelMapper = new ModelMapper();

    /**
     * Retorna la lista de todos los aeropuertos registrados.
     * @return lista de aeropuertos.
     */
    public List<Aeropuerto> listarAeropuertos() {
        return aeropuertoRepository.findAll();
    }

    /**
     * Retorna la lista de aeropuertos como DTOs para el frontend.
     * Incluye el id y nombre de la ciudad asociada.
     * @return lista de AeropuertoDTO.
     */
    public List<AeropuertoDTO> listarDTO() {
        return aeropuertoRepository.findAll().stream()
                .map(a -> {
                    AeropuertoDTO dto = modelMapper.map(a, AeropuertoDTO.class);
                    if (a.getCiudad() != null) {
                        dto.setIdCiudad(a.getCiudad().getIdCiudad());
                        dto.setNombreCiudad(a.getCiudad().getNombreCiudad());
                    }
                    return dto;
                })
                .collect(Collectors.toList());
    }

    /**
     * Busca un aeropuerto por su id.
     * @param id el id del aeropuerto.
     * @return el aeropuerto encontrado o vacío si no existe.
     */
    public Optional<Aeropuerto> buscarPorId(Integer id) {
        return aeropuertoRepository.findById(id);
    }

    /**
     * Guarda un nuevo aeropuerto o actualiza uno existente.
     * @param aeropuerto el aeropuerto a guardar.
     * @return el aeropuerto guardado con su id generado.
     */
    public Aeropuerto guardar(Aeropuerto aeropuerto) {
        return aeropuertoRepository.save(aeropuerto);
    }

    /**
     * Elimina un aeropuerto por su id.
     * @param id el id del aeropuerto a eliminar.
     */
    public void eliminar(Integer id) {
        aeropuertoRepository.deleteById(id);
    }
}