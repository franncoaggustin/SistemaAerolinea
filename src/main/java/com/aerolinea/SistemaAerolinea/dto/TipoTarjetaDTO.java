package com.aerolinea.SistemaAerolinea.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

/**
 * DTO de TipoTarjeta.
 * Transfiere los datos de un tipo de tarjeta entre el backend y el frontend.
 */
@Getter
@Setter
@NoArgsConstructor
public class TipoTarjetaDTO {

    /** Identificador único del tipo de tarjeta. */
    private Integer idTipoTarjeta;

    /** Nombre del tipo de tarjeta (DEBITO o CREDITO). */
    private String tipoTarjeta;
}