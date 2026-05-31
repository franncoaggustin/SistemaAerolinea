package com.aerolinea.SistemaAerolinea.service;

import com.aerolinea.SistemaAerolinea.dto.TipoTarjetaDTO;
import com.aerolinea.SistemaAerolinea.model.TipoTarjeta;
import com.aerolinea.SistemaAerolinea.repository.TipoTarjetaRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Servicio para la gestión de tipos de tarjeta.
 * Contiene la lógica de negocio y conversión a DTO.
 * Se conecta con TipoTarjetaRepository para acceder a la base de datos.
 */
@Service
public class TipoTarjetaService {

    @Autowired
    private TipoTarjetaRepository tipoTarjetaRepository;

    private final ModelMapper modelMapper = new ModelMapper();

    /**
     * Retorna la lista de todos los tipos de tarjeta registrados.
     * @return lista de tipos de tarjeta.
     */
    public List<TipoTarjeta> listarTiposTarjeta() {
        return tipoTarjetaRepository.findAll();
    }

    /**
     * Retorna la lista de tipos de tarjeta como DTOs para el frontend.
     * @return lista de TipoTarjetaDTO.
     */
    public List<TipoTarjetaDTO> listarDTO() {
        return tipoTarjetaRepository.findAll().stream()
                .map(t -> modelMapper.map(t, TipoTarjetaDTO.class))
                .collect(Collectors.toList());
    }

    /**
     * Busca un tipo de tarjeta por su id.
     * @param id el id del tipo de tarjeta.
     * @return el tipo de tarjeta encontrado o vacío si no existe.
     */
    public Optional<TipoTarjeta> buscarPorId(Integer id) {
        return tipoTarjetaRepository.findById(id);
    }

    /**
     * Guarda un nuevo tipo de tarjeta o actualiza uno existente.
     * @param tipoTarjeta el tipo de tarjeta a guardar.
     * @return el tipo de tarjeta guardado con su id generado.
     */
    public TipoTarjeta guardar(TipoTarjeta tipoTarjeta) {
        return tipoTarjetaRepository.save(tipoTarjeta);
    }

    /**
     * Elimina un tipo de tarjeta por su id.
     * @param id el id del tipo de tarjeta a eliminar.
     */
    public void eliminar(Integer id) {
        tipoTarjetaRepository.deleteById(id);
    }
}