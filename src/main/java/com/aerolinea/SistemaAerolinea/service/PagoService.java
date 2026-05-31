package com.aerolinea.SistemaAerolinea.service;

import com.aerolinea.SistemaAerolinea.dto.PagoDTO;
import com.aerolinea.SistemaAerolinea.model.Pago;
import com.aerolinea.SistemaAerolinea.repository.PagoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Servicio para la gestión de pagos.
 * Contiene la lógica de negocio y conversión a DTO.
 * Se conecta con PagoRepository para acceder a la base de datos.
 */
@Service
public class PagoService {

    @Autowired
    private PagoRepository pagoRepository;

    private final ModelMapper modelMapper = new ModelMapper();

    /**
     * Retorna la lista de todos los pagos registrados.
     * @return lista de pagos.
     */
    public List<Pago> listarPagos() {
        return pagoRepository.findAll();
    }

    /**
     * Retorna la lista de pagos como DTOs para el frontend.
     * @return lista de PagoDTO.
     */
    public List<PagoDTO> listarDTO() {
        return pagoRepository.findAll().stream()
                .map(p -> modelMapper.map(p, PagoDTO.class))
                .collect(Collectors.toList());
    }

    /**
     * Busca un pago por su id.
     * @param id el id del pago.
     * @return el pago encontrado o vacío si no existe.
     */
    public Optional<Pago> buscarPorId(Integer id) {
        return pagoRepository.findById(id);
    }

    /**
     * Guarda un nuevo pago o actualiza uno existente.
     * @param pago el pago a guardar.
     * @return el pago guardado con su id generado.
     */
    public Pago guardar(Pago pago) {
        return pagoRepository.save(pago);
    }

    /**
     * Elimina un pago por su id.
     * @param id el id del pago a eliminar.
     */
    public void eliminar(Integer id) {
        pagoRepository.deleteById(id);
    }
}