package com.aerolinea.SistemaAerolinea.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.util.Date;

/**
 * DTO de Pago.
 * Transfiere los datos de un pago entre el backend y el frontend.
 */
@Getter
@Setter
@NoArgsConstructor
public class PagoDTO {

    /** Identificador único del pago. */
    private Integer idPago;

    /** Número identificador visible del pago. */
    private int numeroPago;

    /** Monto total del pago. */
    private double cantidadPago;

    /** Fecha y hora en que se realizó el pago. */
    private Date fechaPago;
}