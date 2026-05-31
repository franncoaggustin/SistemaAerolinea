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
 * Entidad que representa una ciudad.
 * Una ciudad puede tener múltiples aeropuertos.
 */
@Entity
@Table(name = "ciudad")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ciudad {

    /** Identificador único de la ciudad. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ciudad")
    private Integer idCiudad;

    /**
     * Nombre de la ciudad.
     * No puede estar vacío y tiene entre 2 y 100 caracteres.
     */
    @NotBlank(message = "El nombre de la ciudad no puede estar vacío.")
    @Size(min = 2, max = 100, message = "El nombre de la ciudad debe tener entre 2 y 100 caracteres.")
    @Column(name = "nombre_ciudad", nullable = false)
    private String nombreCiudad;

    /** Lista de aeropuertos ubicados en esta ciudad. */
    @OneToMany(mappedBy = "ciudad", cascade = CascadeType.ALL)
    private List<Aeropuerto> aeropuertos = new ArrayList<>();
}