package com.aerolinea.SistemaAerolinea.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * Entidad base que representa un pago en el sistema.
 * Es la clase padre de Tarjeta mediante herencia JPA (InheritanceType.JOINED).
 * Puede estar asociado a múltiples reservas.
 */
@Entity
@Table(name = "pago")
@Inheritance(strategy = InheritanceType.JOINED)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pago {

    /** Identificador único del pago. Heredado por Tarjeta como idPago. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pago")
    private Integer idPago;

    /**
     * Número identificador visible del pago.
     * Debe ser un número positivo mayor a cero.
     */
    @Min(value = 1, message = "El número de pago debe ser un número positivo.")
    @Column(name = "numero_pago", nullable = false)
    private int numeroPago;

    /**
     * Monto total del pago en moneda local.
     * Debe ser un valor mayor o igual a 0.01.
     */
    @DecimalMin(value = "0.01", message = "La cantidad del pago debe ser mayor a cero.")
    @Column(name = "cantidad_pago", nullable = false)
    private double cantidadPago;

    /**
     * Fecha y hora en que se realizó el pago.
     * No puede ser nula.
     */
    @NotNull(message = "La fecha del pago no puede ser nula.")
    @Column(name = "fecha_pago")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaPago;

    /** Lista de reservas asociadas a este pago. */
    @OneToMany(mappedBy = "pago")
    private List<Reserva> reservas = new ArrayList<>();
}