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
 * Entidad que representa el tipo de tarjeta de pago.
 * Puede ser DEBITO o CREDITO.
 * Se conecta con Tarjeta.
 */
@Entity
@Table(name = "tipo_tarjeta")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TipoTarjeta {

    /** Identificador único del tipo de tarjeta. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo_tarjeta")
    private Integer idTipoTarjeta;

    /**
     * Nombre del tipo de tarjeta (DEBITO o CREDITO).
     * No puede estar vacío y tiene entre 2 y 50 caracteres.
     */
    @NotBlank(message = "El tipo de tarjeta no puede estar vacío.")
    @Size(min = 2, max = 50, message = "El tipo de tarjeta debe tener entre 2 y 50 caracteres.")
    @Column(name = "tipo_tarjeta", nullable = false)
    private String tipoTarjeta;

    /** Lista de tarjetas que son de este tipo. */
    @OneToMany(mappedBy = "tipoTarjeta")
    private List<Tarjeta> tarjetas = new ArrayList<>();
}