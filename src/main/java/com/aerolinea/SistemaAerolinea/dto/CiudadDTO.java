package com.aerolinea.SistemaAerolinea.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

/**
 * DTO de Ciudad.
 * Transfiere los datos de una ciudad entre el backend y el frontend.
 */
@Getter
@Setter
@NoArgsConstructor
public class CiudadDTO {

    /** Identificador único de la ciudad. */
    private Integer idCiudad;

    /** Nombre de la ciudad. */
    private String nombreCiudad;
}