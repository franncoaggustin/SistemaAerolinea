package com.aerolinea.SistemaAerolinea.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;
import java.util.ArrayList;

/**
 * Entidad que representa un piloto.
 * Un piloto es una persona con número de licencia.
 * Puede operar múltiples vuelos.
 * Se conecta con Persona y Vuelo.
 */
@Entity
@Table(name = "piloto")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Piloto {

    /** Identificador único del piloto. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_piloto")
    private Integer idPiloto;

    /**
     * Número de licencia del piloto.
     * Debe ser un número positivo mayor a cero.
     */
    @Min(value = 1, message = "El número de piloto debe ser un número positivo.")
    @Column(name = "numero_piloto", nullable = false)
    private int numeroPiloto;

    /**
     * Datos personales del piloto.
     * No puede ser nulo.
     */
    @NotNull(message = "La persona asociada al piloto no puede ser nula.")
    @ManyToOne
    @JoinColumn(name = "id_persona", nullable = false)
    private Persona persona;

    /** Lista de vuelos operados por este piloto. */
    @OneToMany(mappedBy = "piloto")
    private List<Vuelo> vuelos = new ArrayList<>();
}