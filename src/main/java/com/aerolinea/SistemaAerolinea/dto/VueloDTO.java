package com.aerolinea.SistemaAerolinea.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.util.Date;

/**
 * DTO de Vuelo.
 * Transfiere los datos de un vuelo entre el backend y el frontend.
 */
@Getter
@Setter
@NoArgsConstructor
public class VueloDTO {

    /** Identificador único del vuelo. */
    private Integer idVuelo;

    /** Número de vuelo visible al pasajero. */
    private int numeroVuelo;

    /** Estado actual del vuelo. */
    private String estadoVuelo;

    /** Fecha y hora de salida del vuelo. */
    private Date fechaSalida;

    /** Identificador del avión asignado al vuelo. */
    private Integer idAvion;

    /** Identificador del piloto asignado al vuelo. */
    private Integer idPiloto;

    /** Identificador de la aerolínea que opera el vuelo. */
    private Integer idAerolinea;

    /** Nombre de la aerolínea que opera el vuelo. */
    private String nombreAerolinea;

    /** Identificador del aeropuerto de origen. */
    private Integer idAeropuertoOrigen;

    /** Nombre del aeropuerto de origen. */
    private String nombreAeropuertoOrigen;

    /** Identificador del aeropuerto de destino. */
    private Integer idAeropuertoDestino;

    /** Nombre del aeropuerto de destino. */
    private String nombreAeropuertoDestino;
}