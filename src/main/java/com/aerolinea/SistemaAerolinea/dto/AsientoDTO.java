package com.aerolinea.SistemaAerolinea.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

/**
 * DTO de Asiento.
 * Transfiere los datos de un asiento entre el backend y el frontend.
 */
@Getter
@Setter
@NoArgsConstructor
public class AsientoDTO {

    /** Identificador único del asiento. */
    private Integer idAsiento;

    /** Número de fila del asiento. */
    private int filaAsiento;

    /** Letra identificadora del asiento dentro de la fila. */
    private String letraAsiento;

    /** Estado del asiento (DISPONIBLE, OCUPADO, etc.). */
    private String estado;

    /** Identificador del avión al que pertenece el asiento. */
    private Integer idAvion;

    /** Identificador de la clase del asiento. */
    private Integer idClase;

    /** Nombre de la clase del asiento (ECONOMY, TURISTA, BUSINESS). */
    private String nombreClase;
}