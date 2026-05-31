package com.aerolinea.SistemaAerolinea.service;

import com.aerolinea.SistemaAerolinea.dto.AvionDTO;
import com.aerolinea.SistemaAerolinea.model.Avion;
import com.aerolinea.SistemaAerolinea.repository.AvionRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Servicio para la gestión de aviones.
 * Contiene la lógica de negocio y conversión a DTO.
 * Se conecta con AvionRepository para acceder a la base de datos.
 */
@Service
public class AvionService {

    @Autowired
    private AvionRepository avionRepository;

    private final ModelMapper modelMapper = new ModelMapper();

    /**
     * Retorna la lista de todos los aviones registrados.
     * @return lista de aviones.
     */
    public List<Avion> listarAviones() {
        return avionRepository.findAll();
    }

    /**
     * Retorna la lista de aviones como DTOs para el frontend.
     * Incluye el id y nombre de la aerolínea asociada.
     * @return lista de AvionDTO.
     */
    public List<AvionDTO> listarDTO() {
        return avionRepository.findAll().stream()
                .map(a -> {
                    AvionDTO dto = modelMapper.map(a, AvionDTO.class);
                    if (a.getAerolinea() != null) {
                        dto.setIdAerolinea(a.getAerolinea().getIdAerolinea());
                        dto.setNombreAerolinea(a.getAerolinea().getNombreAerolinea());
                    }
                    return dto;
                })
                .collect(Collectors.toList());
    }

    /**
     * Busca un avión por su id.
     * @param id el id del avión.
     * @return el avión encontrado o vacío si no existe.
     */
    public Optional<Avion> buscarPorId(Integer id) {
        return avionRepository.findById(id);
    }

    /**
     * Guarda un nuevo avión o actualiza uno existente.
     * @param avion el avión a guardar.
     * @return el avión guardado con su id generado.
     */
    public Avion guardar(Avion avion) {
        return avionRepository.save(avion);
    }

    /**
     * Elimina un avión por su id.
     * @param id el id del avión a eliminar.
     */
    public void eliminar(Integer id) {
        avionRepository.deleteById(id);
    }
}