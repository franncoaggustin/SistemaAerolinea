package com.aerolinea.SistemaAerolinea.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.util.Date;

/**
 * DTO de Reserva.
 * Transfiere los datos de una reserva entre el backend y el frontend.
 */
@Getter
@Setter
@NoArgsConstructor
public class ReservaDTO {

    /** Identificador único de la reserva. */
    private Integer idReserva;

    /** Número identificador visible de la reserva. */
    private int numeroReserva;

    /** Estado actual de la reserva (CONFIRMADA, CANCELADA, PENDIENTE, etc.). */
    private String estadoReserva;

    /** Fecha y hora en que se realizó la reserva. */
    private Date fechaReserva;

    /** Identificador del usuario que realizó la reserva. */
    private Integer idUsuario;

    /** Identificador del vuelo reservado. */
    private Integer idVuelo;

    /** Identificador del asiento reservado. */
    private Integer idAsiento;

    /** Identificador del pago asociado a la reserva. */
    private Integer idPago;
}