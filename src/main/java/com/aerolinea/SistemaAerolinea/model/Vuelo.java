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
import java.util.List;
import java.util.ArrayList;

/**
 * Entidad central del sistema que representa un vuelo.
 * Un vuelo tiene un avión, piloto, aerolínea, aeropuerto origen y destino.
 * Puede tener múltiples tarifas, reservas y consultas.
 * Se conecta con Avion, Piloto, Aerolinea, Aeropuerto, Tarifa, Reserva y Consulta.
 */
@Entity
@Table(name = "vuelo")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vuelo {

    /** Identificador único del vuelo. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_vuelo")
    private Integer idVuelo;

    /**
     * Número de vuelo visible al pasajero.
     * Debe ser un número positivo mayor a cero.
     */
    @Min(value = 1, message = "El número de vuelo debe ser un número positivo.")
    @Column(name = "numero_vuelo", nullable = false)
    private int numeroVuelo;

    /**
     * Estado actual del vuelo (PROGRAMADO, EN_VUELO, ATERRIZADO, CANCELADO, etc.).
     * No puede estar vacío y tiene entre 2 y 50 caracteres.
     */
    @NotBlank(message = "El estado del vuelo no puede estar vacío.")
    @Size(min = 2, max = 50, message = "El estado del vuelo debe tener entre 2 y 50 caracteres.")
    @Column(name = "estado_vuelo")
    private String estadoVuelo;

    /**
     * Fecha y hora de salida programada del vuelo.
     * No puede ser nula.
     */
    @NotNull(message = "La fecha de salida del vuelo no puede ser nula.")
    @Column(name = "fecha_salida")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaSalida;

    /**
     * Avión asignado para operar este vuelo.
     * No puede ser nulo.
     */
    @NotNull(message = "El avión del vuelo no puede ser nulo.")
    @ManyToOne
    @JoinColumn(name = "id_avion", nullable = false)
    private Avion avion;

    /**
     * Piloto asignado para operar este vuelo.
     * No puede ser nulo.
     */
    @NotNull(message = "El piloto del vuelo no puede ser nulo.")
    @ManyToOne
    @JoinColumn(name = "id_piloto", nullable = false)
    private Piloto piloto;

    /**
     * Aerolínea que opera este vuelo.
     * No puede ser nula.
     */
    @NotNull(message = "La aerolínea del vuelo no puede ser nula.")
    @ManyToOne
    @JoinColumn(name = "id_aerolinea", nullable = false)
    private Aerolinea aerolinea;

    /**
     * Aeropuerto de origen del vuelo.
     * No puede ser nulo.
     */
    @NotNull(message = "El aeropuerto de origen no puede ser nulo.")
    @ManyToOne
    @JoinColumn(name = "id_aeropuerto_origen", nullable = false)
    private Aeropuerto aeropuertoOrigen;

    /**
     * Aeropuerto de destino del vuelo.
     * No puede ser nulo.
     */
    @NotNull(message = "El aeropuerto de destino no puede ser nulo.")
    @ManyToOne
    @JoinColumn(name = "id_aeropuerto_destino", nullable = false)
    private Aeropuerto aeropuertoDestino;

    /** Lista de tarifas disponibles para este vuelo. */
    @OneToMany(mappedBy = "vuelo", cascade = CascadeType.ALL)
    private List<Tarifa> tarifas = new ArrayList<>();

    /** Lista de reservas realizadas para este vuelo. */
    @OneToMany(mappedBy = "vuelo")
    private List<Reserva> reservas = new ArrayList<>();

    /** Lista de consultas realizadas sobre este vuelo. */
    @OneToMany(mappedBy = "vuelo")
    private List<Consulta> consultas = new ArrayList<>();
}