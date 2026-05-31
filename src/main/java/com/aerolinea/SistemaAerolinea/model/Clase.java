package com.aerolinea.SistemaAerolinea.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;
import java.util.ArrayList;

/**
 * Entidad que representa la clase de un asiento o tarifa.
 * Puede ser ECONOMY, TURISTA o BUSINESS.
 * Se conecta con Asiento y Tarifa.
 */
@Entity
@Table(name = "clase")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Clase {

    /** Identificador único de la clase. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_clase")
    private Integer idClase;

    /**
     * Nombre de la clase (ECONOMY, TURISTA, BUSINESS).
     * No puede estar vacío y tiene entre 2 y 50 caracteres.
     */
    @NotBlank(message = "El nombre de la clase no puede estar vacío.")
    @Size(min = 2, max = 50, message = "El nombre de la clase debe tener entre 2 y 50 caracteres.")
    @Column(name = "nombre_clase", nullable = false)
    private String nombreClase;

    /** Lista de asientos que pertenecen a esta clase. */
    @OneToMany(mappedBy = "clase")
    private List<Asiento> asientos = new ArrayList<>();

    /** Lista de tarifas asociadas a esta clase. */
    @OneToMany(mappedBy = "clase")
    private List<Tarifa> tarifas = new ArrayList<>();
}