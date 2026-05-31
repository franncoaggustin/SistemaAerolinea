package com.aerolinea.SistemaAerolinea.service;

import com.aerolinea.SistemaAerolinea.dto.VueloDTO;
import com.aerolinea.SistemaAerolinea.model.Vuelo;
import com.aerolinea.SistemaAerolinea.repository.VueloRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Servicio para la gestión de vuelos.
 * Contiene la lógica de negocio y conversión a DTO.
 * Se conecta con VueloRepository para acceder a la base de datos.
 */
@Service
public class VueloService {

    @Autowired
    private VueloRepository vueloRepository;

    private final ModelMapper modelMapper = new ModelMapper();

    /**
     * Retorna la lista de todos los vuelos registrados.
     * @return lista de vuelos.
     */
    public List<Vuelo> listarVuelos() {
        return vueloRepository.findAll();
    }

    /**
     * Retorna la lista de vuelos como DTOs para el frontend.
     * Incluye nombres de aerolínea y aeropuertos origen y destino.
     * @return lista de VueloDTO.
     */
    public List<VueloDTO> listarDTO() {
        return vueloRepository.findAll().stream()
                .map(v -> {
                    VueloDTO dto = modelMapper.map(v, VueloDTO.class);
                    if (v.getAvion() != null) dto.setIdAvion(v.getAvion().getIdAvion());
                    if (v.getPiloto() != null) dto.setIdPiloto(v.getPiloto().getIdPiloto());
                    if (v.getAerolinea() != null) {
                        dto.setIdAerolinea(v.getAerolinea().getIdAerolinea());
                        dto.setNombreAerolinea(v.getAerolinea().getNombreAerolinea());
                    }
                    if (v.getAeropuertoOrigen() != null) {
                        dto.setIdAeropuertoOrigen(v.getAeropuertoOrigen().getIdAeropuerto());
                        dto.setNombreAeropuertoOrigen(v.getAeropuertoOrigen().getNombreAeropuerto());
                    }
                    if (v.getAeropuertoDestino() != null) {
                        dto.setIdAeropuertoDestino(v.getAeropuertoDestino().getIdAeropuerto());
                        dto.setNombreAeropuertoDestino(v.getAeropuertoDestino().getNombreAeropuerto());
                    }
                    return dto;
                })
                .collect(Collectors.toList());
    }

    /**
     * Busca un vuelo por su id.
     * @param id el id del vuelo.
     * @return el vuelo encontrado o vacío si no existe.
     */
    public Optional<Vuelo> buscarPorId(Integer id) {
        return vueloRepository.findById(id);
    }

    /**
     * Guarda un nuevo vuelo o actualiza uno existente.
     * @param vuelo el vuelo a guardar.
     * @return el vuelo guardado con su id generado.
     */
    public Vuelo guardar(Vuelo vuelo) {
        return vueloRepository.save(vuelo);
    }

    /**
     * Elimina un vuelo por su id.
     * @param id el id del vuelo a eliminar.
     */
    public void eliminar(Integer id) {
        vueloRepository.deleteById(id);
    }
}