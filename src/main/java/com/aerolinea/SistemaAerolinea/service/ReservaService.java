package com.aerolinea.SistemaAerolinea.service;

import com.aerolinea.SistemaAerolinea.dto.ReservaDTO;
import com.aerolinea.SistemaAerolinea.model.Reserva;
import com.aerolinea.SistemaAerolinea.repository.ReservaRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Servicio para la gestión de reservas.
 * Contiene la lógica de negocio y conversión a DTO.
 * Se conecta con ReservaRepository para acceder a la base de datos.
 */
@Service
public class ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;

    private final ModelMapper modelMapper = new ModelMapper();

    /**
     * Retorna la lista de todas las reservas registradas.
     * @return lista de reservas.
     */
    public List<Reserva> listarReservas() {
        return reservaRepository.findAll();
    }

    /**
     * Retorna la lista de reservas como DTOs para el frontend.
     * Incluye los ids de usuario, vuelo, asiento y pago asociados.
     * @return lista de ReservaDTO.
     */
    public List<ReservaDTO> listarDTO() {
        return reservaRepository.findAll().stream()
                .map(r -> {
                    ReservaDTO dto = modelMapper.map(r, ReservaDTO.class);
                    if (r.getUsuario() != null) dto.setIdUsuario(r.getUsuario().getIdUsuario());
                    if (r.getVuelo() != null) dto.setIdVuelo(r.getVuelo().getIdVuelo());
                    if (r.getAsiento() != null) dto.setIdAsiento(r.getAsiento().getIdAsiento());
                    if (r.getPago() != null) dto.setIdPago(r.getPago().getIdPago());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    /**
     * Busca una reserva por su id.
     * @param id el id de la reserva.
     * @return la reserva encontrada o vacío si no existe.
     */
    public Optional<Reserva> buscarPorId(Integer id) {
        return reservaRepository.findById(id);
    }

    /**
     * Busca todas las reservas realizadas por un usuario.
     * @param idUsuario el id del usuario.
     * @return lista de reservas del usuario.
     */
    public List<Reserva> buscarPorUsuario(Integer idUsuario) {
        return reservaRepository.findByUsuarioIdUsuario(idUsuario);
    }

    /**
     * Busca todas las reservas asociadas a un vuelo.
     * @param idVuelo el id del vuelo.
     * @return lista de reservas del vuelo.
     */
    public List<Reserva> buscarPorVuelo(Integer idVuelo) {
        return reservaRepository.findByVueloIdVuelo(idVuelo);
    }

    /**
     * Guarda una nueva reserva o actualiza una existente.
     * @param reserva la reserva a guardar.
     * @return la reserva guardada con su id generado.
     */
    public Reserva guardar(Reserva reserva) {
        return reservaRepository.save(reserva);
    }

    /**
     * Elimina una reserva por su id.
     * @param id el id de la reserva a eliminar.
     */
    public void eliminar(Integer id) {
        reservaRepository.deleteById(id);
    }
}