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
 * Entidad que representa una aerolínea.
 * Una aerolínea puede tener múltiples aviones y vuelos.
 * Se conecta con Avion y Vuelo.
 */
@Entity
@Table(name = "aerolinea")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Aerolinea {

    /** Identificador único de la aerolínea. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_aerolinea")
    private Integer idAerolinea;

    /**
     * Nombre comercial de la aerolínea.
     * No puede estar vacío y tiene entre 2 y 100 caracteres.
     */
    @NotBlank(message = "El nombre de la aerolínea no puede estar vacío.")
    @Size(min = 2, max = 100, message = "El nombre de la aerolínea debe tener entre 2 y 100 caracteres.")
    @Column(name = "nombre_aerolinea", nullable = false)
    private String nombreAerolinea;

    /** Lista de aviones que pertenecen a esta aerolínea. */
    @OneToMany(mappedBy = "aerolinea", cascade = CascadeType.ALL)
    private List<Avion> aviones = new ArrayList<>();

    /** Lista de vuelos operados por esta aerolínea. */
    @OneToMany(mappedBy = "aerolinea")
    private List<Vuelo> vuelos = new ArrayList<>();
}