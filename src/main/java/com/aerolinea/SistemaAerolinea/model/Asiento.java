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
 * Entidad que representa un asiento de un avión.
 * Un asiento pertenece a un avión y tiene una clase (ECONOMY, TURISTA, BUSINESS).
 * Puede estar asociado a múltiples reservas.
 * Se conecta con Avion, Clase y Reserva.
 */
@Entity
@Table(name = "asiento")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Asiento {

    /** Identificador único del asiento. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_asiento")
    private Integer idAsiento;

    /**
     * Número de fila del asiento dentro del avión.
     * Debe ser un número positivo mayor a cero.
     */
    @Min(value = 1, message = "La fila del asiento debe ser un número positivo.")
    @Column(name = "fila_asiento", nullable = false)
    private int filaAsiento;

    /**
     * Letra identificadora del asiento dentro de la fila (A, B, C...).
     * No puede estar vacía y tiene entre 1 y 2 caracteres.
     */
    @NotBlank(message = "La letra del asiento no puede estar vacía.")
    @Size(min = 1, max = 2, message = "La letra del asiento debe tener 1 o 2 caracteres.")
    @Column(name = "letra_asiento", nullable = false)
    private String letraAsiento;

    /**
     * Estado del asiento (DISPONIBLE, OCUPADO, RESERVADO).
     * No puede estar vacío.
     */
    @NotBlank(message = "El estado del asiento no puede estar vacío.")
    @Column(name = "estado")
    private String estado;

    /**
     * Avión al que pertenece este asiento.
     * No puede ser nulo.
     */
    @NotNull(message = "El avión del asiento no puede ser nulo.")
    @ManyToOne
    @JoinColumn(name = "id_avion", nullable = false)
    private Avion avion;

    /**
     * Clase del asiento (ECONOMY, TURISTA, BUSINESS).
     * No puede ser nula.
     */
    @NotNull(message = "La clase del asiento no puede ser nula.")
    @ManyToOne
    @JoinColumn(name = "id_clase", nullable = false)
    private Clase clase;

    /** Lista de reservas asociadas a este asiento. */
    @OneToMany(mappedBy = "asiento")
    private List<Reserva> reservas = new ArrayList<>();
}