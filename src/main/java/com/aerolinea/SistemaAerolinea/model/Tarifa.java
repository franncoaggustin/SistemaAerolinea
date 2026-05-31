package com.aerolinea.SistemaAerolinea.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Entidad que representa la tarifa de un vuelo para una clase determinada.
 * Contiene el precio base y el impuesto.
 * Se conecta con Vuelo y Clase.
 */
@Entity
@Table(name = "tarifa")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tarifa {

    /** Identificador único de la tarifa. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tarifa")
    private Integer idTarifa;

    /**
     * Número identificador visible de la tarifa.
     * Debe ser un número positivo mayor a cero.
     */
    @Min(value = 1, message = "El número de tarifa debe ser un número positivo.")
    @Column(name = "numero_tarifa", nullable = false)
    private int numeroTarifa;

    /**
     * Precio base de la tarifa sin impuestos.
     * Debe ser mayor a cero.
     */
    @DecimalMin(value = "0.01", message = "El precio de la tarifa debe ser mayor a cero.")
    @Column(name = "precio_tarifa", nullable = false)
    private double precioTarifa;

    /**
     * Porcentaje de impuesto aplicado sobre el precio de la tarifa.
     * Debe ser mayor o igual a cero.
     */
    @DecimalMin(value = "0.0", message = "El impuesto de la tarifa no puede ser negativo.")
    @Column(name = "impuesto_tarifa", nullable = false)
    private double impuestoTarifa;

    /**
     * Vuelo al que corresponde esta tarifa.
     * No puede ser nulo.
     */
    @NotNull(message = "El vuelo de la tarifa no puede ser nulo.")
    @ManyToOne
    @JoinColumn(name = "id_vuelo", nullable = false)
    private Vuelo vuelo;

    /**
     * Clase de asiento a la que aplica esta tarifa.
     * No puede ser nula.
     */
    @NotNull(message = "La clase de la tarifa no puede ser nula.")
    @ManyToOne
    @JoinColumn(name = "id_clase", nullable = false)
    private Clase clase;
}