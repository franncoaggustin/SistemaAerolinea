package com.aerolinea.SistemaAerolinea.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

/**
 * DTO de Piloto.
 * Transfiere los datos de un piloto entre el backend y el frontend.
 */
@Getter
@Setter
@NoArgsConstructor
public class PilotoDTO {

    /** Identificador único del piloto. */
    private Integer idPiloto;

    /** Número de licencia del piloto. */
    private int numeroPiloto;

    /** Identificador de los datos personales del piloto. */
    private Integer idPersona;

    /** Nombre de pila del piloto. */
    private String nombrePersona;

    /** Apellido del piloto. */
    private String apellidoPersona;
}