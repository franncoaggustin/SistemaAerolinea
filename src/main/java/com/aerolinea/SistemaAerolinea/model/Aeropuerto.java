package com.aerolinea.SistemaAerolinea.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;
import java.util.ArrayList;

/**
 * Entidad que representa un aeropuerto.
 * Un aeropuerto pertenece a una ciudad.
 * Puede ser origen o destino de múltiples vuelos.
 * Se conecta con Ciudad y Vuelo.
 */
@Entity
@Table(name = "aeropuerto")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Aeropuerto {

    /** Identificador único del aeropuerto. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_aeropuerto")
    private Integer idAeropuerto;

    /**
     * Nombre oficial del aeropuerto.
     * No puede estar vacío y tiene entre 2 y 100 caracteres.
     */
    @NotBlank(message = "El nombre del aeropuerto no puede estar vacío.")
    @Size(min = 2, max = 100, message = "El nombre del aeropuerto debe tener entre 2 y 100 caracteres.")
    @Column(name = "nombre_aeropuerto", nullable = false)
    private String nombreAeropuerto;

    /**
     * Ciudad donde se encuentra el aeropuerto.
     * No puede ser nula.
     */
    @NotNull(message = "La ciudad del aeropuerto no puede ser nula.")
    @ManyToOne
    @JoinColumn(name = "id_ciudad", nullable = false)
    private Ciudad ciudad;

    /** Lista de vuelos que tienen este aeropuerto como origen. */
    @OneToMany(mappedBy = "aeropuertoOrigen")
    private List<Vuelo> vuelosOrigen = new ArrayList<>();

    /** Lista de vuelos que tienen este aeropuerto como destino. */
    @OneToMany(mappedBy = "aeropuertoDestino")
    private List<Vuelo> vuelosDestino = new ArrayList<>();
}