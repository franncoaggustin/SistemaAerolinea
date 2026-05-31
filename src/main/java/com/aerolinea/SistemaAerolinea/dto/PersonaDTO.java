package com.aerolinea.SistemaAerolinea.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

/**
 * DTO de Persona.
 * Transfiere los datos de una persona entre el backend y el frontend.
 */
@Getter
@Setter
@NoArgsConstructor
public class PersonaDTO {

    /** Identificador único de la persona. */
    private Integer idPersona;

    /** Documento Nacional de Identidad de la persona. */
    private int dniPersona;

    /** Nombre de pila de la persona. */
    private String nombrePersona;

    /** Apellido de la persona. */
    private String apellidoPersona;
}