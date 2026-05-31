package com.aerolinea.SistemaAerolinea.repository;

import com.aerolinea.SistemaAerolinea.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio para la entidad Usuario.
 * Provee operaciones CRUD automáticas sobre la tabla usuario.
 * Incluye búsqueda por correo electrónico para autenticación.
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    /**
     * Busca un usuario por su correo electrónico.
     * @param correoElectronicoUsuario el correo del usuario a buscar.
     * @return el Usuario encontrado o null si no existe.
     */
    Optional<Usuario> findByCorreoElectronicoUsuario(String correoElectronicoUsuario);
}