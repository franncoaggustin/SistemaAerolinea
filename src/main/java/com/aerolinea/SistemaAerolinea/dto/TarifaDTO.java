package com.aerolinea.SistemaAerolinea.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

/**
 * DTO de Tarifa.
 * Transfiere los datos de una tarifa entre el backend y el frontend.
 */
@Getter
@Setter
@NoArgsConstructor
public class TarifaDTO {

    /** Identificador único de la tarifa. */
    private Integer idTarifa;

    /** Número identificador visible de la tarifa. */
    private int numeroTarifa;

    /** Precio base de la tarifa sin impuestos. */
    private double precioTarifa;

    /** Porcentaje de impuesto aplicado sobre el precio. */
    private double impuestoTarifa;

    /** Identificador del vuelo al que corresponde la tarifa. */
    private Integer idVuelo;

    /** Identificador de la clase a la que aplica la tarifa. */
    private Integer idClase;

    /** Nombre de la clase a la que aplica la tarifa. */
    private String nombreClase;
}