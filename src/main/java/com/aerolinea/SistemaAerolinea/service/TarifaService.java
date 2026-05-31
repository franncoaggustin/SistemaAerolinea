package com.aerolinea.SistemaAerolinea.service;

import com.aerolinea.SistemaAerolinea.dto.TarifaDTO;
import com.aerolinea.SistemaAerolinea.model.Tarifa;
import com.aerolinea.SistemaAerolinea.repository.TarifaRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Servicio para la gestión de tarifas.
 * Contiene la lógica de negocio y conversión a DTO.
 * Se conecta con TarifaRepository para acceder a la base de datos.
 */
@Service
public class TarifaService {

    @Autowired
    private TarifaRepository tarifaRepository;

    private final ModelMapper modelMapper = new ModelMapper();

    /**
     * Retorna la lista de todas las tarifas registradas.
     * @return lista de tarifas.
     */
    public List<Tarifa> listarTarifas() {
        return tarifaRepository.findAll();
    }

    /**
     * Retorna la lista de tarifas como DTOs para el frontend.
     * Incluye el id del vuelo y el id y nombre de la clase asociada.
     * @return lista de TarifaDTO.
     */
    public List<TarifaDTO> listarDTO() {
        return tarifaRepository.findAll().stream()
                .map(t -> {
                    TarifaDTO dto = modelMapper.map(t, TarifaDTO.class);
                    if (t.getVuelo() != null) dto.setIdVuelo(t.getVuelo().getIdVuelo());
                    if (t.getClase() != null) {
                        dto.setIdClase(t.getClase().getIdClase());
                        dto.setNombreClase(t.getClase().getNombreClase());
                    }
                    return dto;
                })
                .collect(Collectors.toList());
    }

    /**
     * Busca una tarifa por su id.
     * @param id el id de la tarifa.
     * @return la tarifa encontrada o vacío si no existe.
     */
    public Optional<Tarifa> buscarPorId(Integer id) {
        return tarifaRepository.findById(id);
    }

    /**
     * Guarda una nueva tarifa o actualiza una existente.
     * @param tarifa la tarifa a guardar.
     * @return la tarifa guardada con su id generado.
     */
    public Tarifa guardar(Tarifa tarifa) {
        return tarifaRepository.save(tarifa);
    }

    /**
     * Elimina una tarifa por su id.
     * @param id el id de la tarifa a eliminar.
     */
    public void eliminar(Integer id) {
        tarifaRepository.deleteById(id);
    }
}