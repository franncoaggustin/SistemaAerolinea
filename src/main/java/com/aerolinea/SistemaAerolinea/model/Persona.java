package com.aerolinea.SistemaAerolinea.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Entidad que representa una persona física.
 * Es la base de Usuario y Piloto.
 * Contiene los datos personales básicos.
 */
@Entity
@Table(name = "persona")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Persona {

    /** Identificador único de la persona. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_persona")
    private Integer idPersona;

    /**
     * Documento Nacional de Identidad de la persona.
     * Debe ser un número positivo mayor a cero.
     */
    @Min(value = 1, message = "El DNI debe ser un número positivo.")
    @Column(name = "dni_persona", nullable = false)
    private int dniPersona;

    /**
     * Nombre de pila de la persona.
     * No puede estar vacío y tiene entre 2 y 50 caracteres.
     */
    @NotBlank(message = "El nombre de la persona no puede estar vacío.")
    @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres.")
    @Column(name = "nombre_persona", nullable = false)
    private String nombrePersona;

    /**
     * Apellido de la persona.
     * No puede estar vacío y tiene entre 2 y 50 caracteres.
     */
    @NotBlank(message = "El apellido de la persona no puede estar vacío.")
    @Size(min = 2, max = 50, message = "El apellido debe tener entre 2 y 50 caracteres.")
    @Column(name = "apellido_persona", nullable = false)
    private String apellidoPersona;
}