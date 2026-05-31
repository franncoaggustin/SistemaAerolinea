package com.aerolinea.SistemaAerolinea.service;

import com.aerolinea.SistemaAerolinea.dto.TarjetaDTO;
import com.aerolinea.SistemaAerolinea.model.Tarjeta;
import com.aerolinea.SistemaAerolinea.repository.TarjetaRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Servicio para la gestión de tarjetas de pago.
 * Contiene la lógica de negocio y conversión a DTO.
 * Se conecta con TarjetaRepository para acceder a la base de datos.
 */
@Service
public class TarjetaService {

    @Autowired
    private TarjetaRepository tarjetaRepository;

    private final ModelMapper modelMapper = new ModelMapper();

    /**
     * Retorna la lista de todas las tarjetas registradas.
     * @return lista de tarjetas.
     */
    public List<Tarjeta> listarTarjetas() {
        return tarjetaRepository.findAll();
    }

    /**
     * Retorna la lista de tarjetas como DTOs para el frontend.
     * Incluye el id y tipo de la tarjeta asociada.
     * @return lista de TarjetaDTO.
     */
    public List<TarjetaDTO> listarDTO() {
        return tarjetaRepository.findAll().stream()
                .map(t -> {
                    TarjetaDTO dto = modelMapper.map(t, TarjetaDTO.class);
                    if (t.getTipoTarjeta() != null) {
                        dto.setIdTipoTarjeta(t.getTipoTarjeta().getIdTipoTarjeta());
                        dto.setTipoTarjeta(t.getTipoTarjeta().getTipoTarjeta());
                    }
                    return dto;
                })
                .collect(Collectors.toList());
    }

    /**
     * Busca una tarjeta por su id.
     * @param id el id de la tarjeta.
     * @return la tarjeta encontrada o vacío si no existe.
     */
    public Optional<Tarjeta> buscarPorId(Integer id) {
        return tarjetaRepository.findById(id);
    }

    /**
     * Guarda una nueva tarjeta o actualiza una existente.
     * @param tarjeta la tarjeta a guardar.
     * @return la tarjeta guardada con su id generado.
     */
    public Tarjeta guardar(Tarjeta tarjeta) {
        return tarjetaRepository.save(tarjeta);
    }

    /**
     * Elimina una tarjeta por su id.
     * @param id el id de la tarjeta a eliminar.
     */
    public void eliminar(Integer id) {
        tarjetaRepository.deleteById(id);
    }
}