package com.aerolinea.SistemaAerolinea.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

/**
 * DTO de Clase.
 * Transfiere los datos de una clase de asiento entre el backend y el frontend.
 */
@Getter
@Setter
@NoArgsConstructor
public class ClaseDTO {

    /** Identificador único de la clase. */
    private Integer idClase;

    /** Nombre de la clase (ECONOMY, TURISTA, BUSINESS). */
    private String nombreClase;
}