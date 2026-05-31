package com.aerolinea.SistemaAerolinea.repository;

import com.aerolinea.SistemaAerolinea.model.Consulta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repositorio para la entidad Consulta.
 * Provee operaciones CRUD automáticas sobre la tabla consulta.
 * Incluye búsqueda por usuario y por vuelo.
 */
@Repository
public interface ConsultaRepository extends JpaRepository<Consulta, Integer> {

    /**
     * Busca todas las consultas de un usuario específico.
     * @param idUsuario el id del usuario.
     * @return lista de consultas del usuario.
     */
    List<Consulta> findByUsuarioIdUsuario(Integer idUsuario);
}