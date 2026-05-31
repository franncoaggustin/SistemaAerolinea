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
 * Entidad que representa una consulta realizada por un usuario.
 * Un usuario puede consultar información sobre vuelos disponibles.
 * Se conecta con Usuario y Vuelo.
 */
@Entity
@Table(name = "consulta")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Consulta {

    /** Identificador único de la consulta. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_consulta")
    private Integer idConsulta;

    /**
     * Número identificador visible de la consulta.
     * Debe ser un número positivo mayor a cero.
     */
    @Min(value = 1, message = "El número de consulta debe ser un número positivo.")
    @Column(name = "numero_consulta", nullable = false)
    private int numeroConsulta;

    /**
     * Fecha y hora en que se realizó la consulta.
     * No puede ser nula.
     */
    @NotNull(message = "La fecha de la consulta no puede ser nula.")
    @Column(name = "fecha_consulta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaConsulta;

    /**
     * Tipo de consulta realizada (INFO, RECLAMO, SUGERENCIA, etc.).
     * No puede estar vacío y tiene entre 2 y 50 caracteres.
     */
    @NotBlank(message = "El tipo de consulta no puede estar vacío.")
    @Size(min = 2, max = 50, message = "El tipo de consulta debe tener entre 2 y 50 caracteres.")
    @Column(name = "tipo_consulta")
    private String tipoConsulta;

    /**
     * Detalle o descripción del contenido de la consulta.
     * No puede estar vacío y tiene hasta 500 caracteres.
     */
    @NotBlank(message = "El detalle de la consulta no puede estar vacío.")
    @Size(max = 500, message = "El detalle de la consulta no puede superar los 500 caracteres.")
    @Column(name = "detalle_consulta")
    private String detalleConsulta;

    /**
     * Estado actual de la consulta (PENDIENTE, RESUELTA, CERRADA, etc.).
     * No puede estar vacío y tiene entre 2 y 50 caracteres.
     */
    @NotBlank(message = "El estado de la consulta no puede estar vacío.")
    @Size(min = 2, max = 50, message = "El estado de la consulta debe tener entre 2 y 50 caracteres.")
    @Column(name = "estado_consulta")
    private String estadoConsulta;

    /**
     * Usuario que realizó la consulta.
     * No puede ser nulo.
     */
    @NotNull(message = "El usuario de la consulta no puede ser nulo.")
    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    /** Vuelo sobre el que se realizó la consulta. Puede ser nulo. */
    @ManyToOne
    @JoinColumn(name = "id_vuelo")
    private Vuelo vuelo;
}