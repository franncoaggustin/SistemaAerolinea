package com.aerolinea.SistemaAerolinea.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

/**
 * DTO de Avion.
 * Transfiere los datos de un avión entre el backend y el frontend.
 */
@Getter
@Setter
@NoArgsConstructor
public class AvionDTO {

    /** Identificador único del avión. */
    private Integer idAvion;

    /** Número identificador del avión dentro de la flota. */
    private int numeroAvion;

    /** Tipo o modelo del avión. */
    private String tipoAvion;

    /** Tipo de turbina del avión. */
    private String tipoTurbina;

    /** Identificador de la aerolínea propietaria. */
    private Integer idAerolinea;

    /** Nombre de la aerolínea propietaria. */
    private String nombreAerolinea;
}