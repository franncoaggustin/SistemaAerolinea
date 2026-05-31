package com.aerolinea.SistemaAerolinea.service;

import com.aerolinea.SistemaAerolinea.dto.AsientoDTO;
import com.aerolinea.SistemaAerolinea.model.Asiento;
import com.aerolinea.SistemaAerolinea.repository.AsientoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Servicio para la gestión de asientos.
 * Contiene la lógica de negocio y conversión a DTO.
 * Se conecta con AsientoRepository para acceder a la base de datos.
 */
@Service
public class AsientoService {

    @Autowired
    private AsientoRepository asientoRepository;

    private final ModelMapper modelMapper = new ModelMapper();

    /**
     * Retorna la lista de todos los asientos registrados.
     * @return lista de asientos.
     */
    public List<Asiento> listarAsientos() {
        return asientoRepository.findAll();
    }

    /**
     * Retorna la lista de asientos como DTOs para el frontend.
     * Incluye el id del avión y el id y nombre de la clase asociada.
     * @return lista de AsientoDTO.
     */
    public List<AsientoDTO> listarDTO() {
        return asientoRepository.findAll().stream()
                .map(a -> {
                    AsientoDTO dto = modelMapper.map(a, AsientoDTO.class);
                    if (a.getAvion() != null) dto.setIdAvion(a.getAvion().getIdAvion());
                    if (a.getClase() != null) {
                        dto.setIdClase(a.getClase().getIdClase());
                        dto.setNombreClase(a.getClase().getNombreClase());
                    }
                    return dto;
                })
                .collect(Collectors.toList());
    }

    /**
     * Busca un asiento por su id.
     * @param id el id del asiento.
     * @return el asiento encontrado o vacío si no existe.
     */
    public Optional<Asiento> buscarPorId(Integer id) {
        return asientoRepository.findById(id);
    }

    /**
     * Guarda un nuevo asiento o actualiza uno existente.
     * @param asiento el asiento a guardar.
     * @return el asiento guardado con su id generado.
     */
    public Asiento guardar(Asiento asiento) {
        return asientoRepository.save(asiento);
    }

    /**
     * Elimina un asiento por su id.
     * @param id el id del asiento a eliminar.
     */
    public void eliminar(Integer id) {
        asientoRepository.deleteById(id);
    }
}