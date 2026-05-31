package com.aerolinea.SistemaAerolinea.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;
import java.util.ArrayList;

/**
 * Entidad que representa un avión.
 * Un avión pertenece a una aerolínea.
 * Tiene múltiples asientos y puede operar múltiples vuelos.
 * Se conecta con Aerolinea, Asiento y Vuelo.
 */
@Entity
@Table(name = "avion")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Avion {

    /** Identificador único del avión. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_avion")
    private Integer idAvion;

    /**
     * Número identificador del avión dentro de la flota.
     * Debe ser un número positivo mayor a cero.
     */
    @Min(value = 1, message = "El número del avión debe ser un número positivo.")
    @Column(name = "numero_avion", nullable = false)
    private int numeroAvion;

    /**
     * Tipo o modelo del avión (Boeing 737, Airbus A320, etc.).
     * No puede estar vacío y tiene entre 2 y 100 caracteres.
     */
    @NotBlank(message = "El tipo de avión no puede estar vacío.")
    @Size(min = 2, max = 100, message = "El tipo de avión debe tener entre 2 y 100 caracteres.")
    @Column(name = "tipo_avion")
    private String tipoAvion;

    /**
     * Tipo de turbina que utiliza el avión.
     * No puede estar vacío y tiene entre 2 y 50 caracteres.
     */
    @NotBlank(message = "El tipo de turbina no puede estar vacío.")
    @Size(min = 2, max = 50, message = "El tipo de turbina debe tener entre 2 y 50 caracteres.")
    @Column(name = "tipo_turbina")
    private String tipoTurbina;

    /**
     * Aerolínea propietaria del avión.
     * No puede ser nula.
     */
    @NotNull(message = "La aerolínea del avión no puede ser nula.")
    @ManyToOne
    @JoinColumn(name = "id_aerolinea", nullable = false)
    private Aerolinea aerolinea;

    /** Lista de asientos que tiene este avión. */
    @OneToMany(mappedBy = "avion", cascade = CascadeType.ALL)
    private List<Asiento> asientos = new ArrayList<>();

    /** Lista de vuelos operados por este avión. */
    @OneToMany(mappedBy = "avion")
    private List<Vuelo> vuelos = new ArrayList<>();
}