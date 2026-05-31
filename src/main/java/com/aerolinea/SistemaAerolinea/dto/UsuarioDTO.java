package com.aerolinea.SistemaAerolinea.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

/**
 * DTO de Usuario.
 * Transfiere los datos de un usuario entre el backend y el frontend.
 */
@Getter
@Setter
@NoArgsConstructor
public class UsuarioDTO {

    /** Identificador único del usuario. */
    private Integer idUsuario;

    /** Número identificador visible del usuario en el sistema. */
    private int numeroUsuario;

    /** Correo electrónico del usuario. */
    private String correoElectronicoUsuario;

    /** Identificador de los datos personales del usuario. */
    private Integer idPersona;

    /** Nombre de pila del usuario. */
    private String nombrePersona;

    /** Apellido del usuario. */
    private String apellidoPersona;
}