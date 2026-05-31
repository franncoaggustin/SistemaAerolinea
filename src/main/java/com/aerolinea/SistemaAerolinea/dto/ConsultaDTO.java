package com.aerolinea.SistemaAerolinea.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.util.Date;

/**
 * DTO de Consulta.
 * Transfiere los datos de una consulta entre el backend y el frontend.
 */
@Getter
@Setter
@NoArgsConstructor
public class ConsultaDTO {

    /** Identificador único de la consulta. */
    private Integer idConsulta;

    /** Número identificador visible de la consulta. */
    private int numeroConsulta;

    /** Fecha y hora en que se realizó la consulta. */
    private Date fechaConsulta;

    /** Tipo de consulta realizada. */
    private String tipoConsulta;

    /** Detalle o descripción de la consulta. */
    private String detalleConsulta;

    /** Estado actual de la consulta. */
    private String estadoConsulta;

    /** Identificador del usuario que realizó la consulta. */
    private Integer idUsuario;

    /** Identificador del vuelo sobre el que se realizó la consulta. */
    private Integer idVuelo;
}