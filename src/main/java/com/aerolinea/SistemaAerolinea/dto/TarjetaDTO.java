package com.aerolinea.SistemaAerolinea.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

/**
 * DTO de Tarjeta.
 * Transfiere los datos de una tarjeta de pago entre el backend y el frontend.
 */
@Getter
@Setter
@NoArgsConstructor
public class TarjetaDTO {

    /** Identificador único de la tarjeta (heredado de Pago). */
    private Integer idPago;

    /** Número único de la tarjeta de pago. */
    private long numeroTarjeta;

    /** Identificador del tipo de tarjeta. */
    private Integer idTipoTarjeta;

    /** Nombre del tipo de tarjeta (DEBITO o CREDITO). */
    private String tipoTarjeta;
}