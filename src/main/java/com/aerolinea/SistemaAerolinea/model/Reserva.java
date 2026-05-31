package com.aerolinea.SistemaAerolinea.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.Date;

/**
 * Entidad que representa una reserva de vuelo.
 * Una reserva conecta un usuario con un vuelo, asiento y pago.
 * Es la entidad central del proceso de compra de pasajes.
 * Se conecta con Usuario, Vuelo, Asiento y Pago.
 */
@Entity
@Table(name = "reserva")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reserva {

    /** Identificador único de la reserva. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reserva")
    private Integer idReserva;

    /**
     * Número identificador visible de la reserva.
     * Debe ser un número positivo mayor a cero.
     */
    @Min(value = 1, message = "El número de reserva debe ser un número positivo.")
    @Column(name = "numero_reserva", nullable = false)
    private int numeroReserva;

    /**
     * Estado actual de la reserva (CONFIRMADA, CANCELADA, PENDIENTE, etc.).
     * No puede estar vacío y tiene entre 2 y 50 caracteres.
     */
    @NotBlank(message = "El estado de la reserva no puede estar vacío.")
    @Size(min = 2, max = 50, message = "El estado de la reserva debe tener entre 2 y 50 caracteres.")
    @Column(name = "estado_reserva")
    private String estadoReserva;

    /**
     * Fecha y hora en que se realizó la reserva.
     * No puede ser nula.
     */
    @NotNull(message = "La fecha de la reserva no puede ser nula.")
    @Column(name = "fecha_reserva")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaReserva;

    /**
     * Usuario que realizó la reserva.
     * No puede ser nulo.
     */
    @NotNull(message = "El usuario de la reserva no puede ser nulo.")
    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    /**
     * Vuelo reservado.
     * No puede ser nulo.
     */
    @NotNull(message = "El vuelo de la reserva no puede ser nulo.")
    @ManyToOne
    @JoinColumn(name = "id_vuelo", nullable = false)
    private Vuelo vuelo;

    /**
     * Asiento reservado en el vuelo.
     * No puede ser nulo.
     */
    @NotNull(message = "El asiento de la reserva no puede ser nulo.")
    @ManyToOne
    @JoinColumn(name = "id_asiento", nullable = false)
    private Asiento asiento;

    /**
     * Pago asociado a esta reserva.
     * No puede ser nulo.
     */
    @NotNull(message = "El pago de la reserva no puede ser nulo.")
    @ManyToOne
    @JoinColumn(name = "id_pago", nullable = false)
    private Pago pago;
}