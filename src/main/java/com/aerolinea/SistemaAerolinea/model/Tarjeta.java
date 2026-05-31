package com.aerolinea.SistemaAerolinea.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;
import java.util.ArrayList;

/**
 * Entidad que representa una tarjeta de pago.
 * Hereda de Pago mediante InheritanceType.JOINED según el diagrama de clases.
 * El identificador de Tarjeta es idPago heredado de la clase padre.
 * Una tarjeta tiene un tipo (DEBITO o CREDITO).
 * Puede estar asociada a múltiples usuarios.
 * Se conecta con TipoTarjeta y Usuario.
 */
@Entity
@Table(name = "tarjeta")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Tarjeta extends Pago {

    /**
     * Número único de la tarjeta de pago.
     * Debe ser un número positivo mayor a cero.
     */
    @Min(value = 1, message = "El número de tarjeta debe ser un número positivo.")
    @Column(name = "numero_tarjeta", nullable = false)
    private long numeroTarjeta;

    /**
     * Tipo de tarjeta (DEBITO o CREDITO).
     * No puede ser nulo.
     */
    @NotNull(message = "El tipo de tarjeta no puede ser nulo.")
    @ManyToOne
    @JoinColumn(name = "id_tipo_tarjeta", nullable = false)
    private TipoTarjeta tipoTarjeta;

    /** Lista de usuarios que tienen esta tarjeta asociada. */
    @ManyToMany(mappedBy = "tarjetas")
    private List<Usuario> usuarios = new ArrayList<>();
}