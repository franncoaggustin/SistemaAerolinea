package com.aerolinea.SistemaAerolinea.service;

import com.aerolinea.SistemaAerolinea.dto.PilotoDTO;
import com.aerolinea.SistemaAerolinea.model.Piloto;
import com.aerolinea.SistemaAerolinea.repository.PilotoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Servicio para la gestión de pilotos.
 * Contiene la lógica de negocio y conversión a DTO.
 * Se conecta con PilotoRepository para acceder a la base de datos.
 */
@Service
public class PilotoService {

    @Autowired
    private PilotoRepository pilotoRepository;

    private final ModelMapper modelMapper = new ModelMapper();

    /**
     * Retorna la lista de todos los pilotos registrados.
     * @return lista de pilotos.
     */
    public List<Piloto> listarPilotos() {
        return pilotoRepository.findAll();
    }

    /**
     * Retorna la lista de pilotos como DTOs para el frontend.
     * Incluye el id, nombre y apellido de la persona asociada.
     * @return lista de PilotoDTO.
     */
    public List<PilotoDTO> listarDTO() {
        return pilotoRepository.findAll().stream()
                .map(p -> {
                    PilotoDTO dto = modelMapper.map(p, PilotoDTO.class);
                    if (p.getPersona() != null) {
                        dto.setIdPersona(p.getPersona().getIdPersona());
                        dto.setNombrePersona(p.getPersona().getNombrePersona());
                        dto.setApellidoPersona(p.getPersona().getApellidoPersona());
                    }
                    return dto;
                })
                .collect(Collectors.toList());
    }

    /**
     * Busca un piloto por su id.
     * @param id el id del piloto.
     * @return el piloto encontrado o vacío si no existe.
     */
    public Optional<Piloto> buscarPorId(Integer id) {
        return pilotoRepository.findById(id);
    }

    /**
     * Guarda un nuevo piloto o actualiza uno existente.
     * @param piloto el piloto a guardar.
     * @return el piloto guardado con su id generado.
     */
    public Piloto guardar(Piloto piloto) {
        return pilotoRepository.save(piloto);
    }

    /**
     * Elimina un piloto por su id.
     * @param id el id del piloto a eliminar.
     */
    public void eliminar(Integer id) {
        pilotoRepository.deleteById(id);
    }
}