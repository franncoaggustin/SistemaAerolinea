package com.aerolinea.SistemaAerolinea.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

/**
 * DTO de Aerolinea.
 * Transfiere los datos de una aerolínea entre el backend y el frontend.
 */
@Getter
@Setter
@NoArgsConstructor
public class AerolineaDTO {

    /** Identificador único de la aerolínea. */
    private Integer idAerolinea;

    /** Nombre comercial de la aerolínea. */
    private String nombreAerolinea;
}