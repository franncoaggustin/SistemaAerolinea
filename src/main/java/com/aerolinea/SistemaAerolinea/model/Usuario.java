package com.aerolinea.SistemaAerolinea.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;
import java.util.ArrayList;

/**
 * Entidad que representa un usuario del sistema.
 * Un usuario es una persona registrada que puede realizar reservas y consultas.
 * Puede tener múltiples tarjetas de pago asociadas.
 * Se conecta con Persona, Reserva, Consulta y Tarjeta.
 */
@Entity
@Table(name = "usuario")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    /** Identificador único del usuario. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Integer idUsuario;

    /**
     * Número identificador visible del usuario en el sistema.
     * Debe ser un número positivo mayor a cero.
     */
    @Min(value = 1, message = "El número de usuario debe ser un número positivo.")
    @Column(name = "numero_usuario", nullable = false)
    private int numeroUsuario;

    /**
     * Correo electrónico del usuario, usado para autenticación.
     * No puede estar vacío y debe tener formato de email válido.
     */
    @NotBlank(message = "El correo electrónico no puede estar vacío.")
    @Email(message = "El correo electrónico debe tener un formato válido.")
    @Column(name = "correo_electronico_usuario", nullable = false, unique = true)
    private String correoElectronicoUsuario;

    /**
     * Contraseña del usuario para acceder al sistema.
     * No puede estar vacía y tiene entre 4 y 100 caracteres.
     */
    @NotBlank(message = "La contraseña no puede estar vacía.")
    @Size(min = 4, max = 100, message = "La contraseña debe tener entre 4 y 100 caracteres.")
    @Column(name = "contrasenia_usuario", nullable = false)
    private String contraseniaUsuario;

    /**
     * Datos personales del usuario.
     * No puede ser nulo.
     */
    @NotNull(message = "La persona asociada al usuario no puede ser nula.")
    @ManyToOne
    @JoinColumn(name = "id_persona", nullable = false)
    private Persona persona;

    /** Lista de reservas realizadas por el usuario. */
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<Reserva> reservas = new ArrayList<>();

    /** Lista de consultas realizadas por el usuario. */
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<Consulta> consultas = new ArrayList<>();

    /** Lista de tarjetas de pago asociadas al usuario. */
    @ManyToMany
    @JoinTable(
            name = "usuario_tarjeta",
            joinColumns = @JoinColumn(name = "id_usuario"),
            inverseJoinColumns = @JoinColumn(name = "id_tarjeta")
    )
    private List<Tarjeta> tarjetas = new ArrayList<>();
}