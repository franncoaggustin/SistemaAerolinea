package com.aerolinea.SistemaAerolinea.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

/**
 * DTO de Aeropuerto.
 * Transfiere los datos de un aeropuerto entre el backend y el frontend.
 */
@Getter
@Setter
@NoArgsConstructor
public class AeropuertoDTO {

    /** Identificador único del aeropuerto. */
    private Integer idAeropuerto;

    /** Nombre oficial del aeropuerto. */
    private String nombreAeropuerto;

    /** Identificador de la ciudad donde se encuentra el aeropuerto. */
    private Integer idCiudad;

    /** Nombre de la ciudad donde se encuentra el aeropuerto. */
    private String nombreCiudad;
}