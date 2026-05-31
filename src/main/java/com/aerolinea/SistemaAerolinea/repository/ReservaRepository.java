package com.aerolinea.SistemaAerolinea.repository;

import com.aerolinea.SistemaAerolinea.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repositorio para la entidad Reserva.
 * Provee operaciones CRUD automáticas sobre la tabla reserva.
 * Incluye búsqueda por usuario y por vuelo.
 */
@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Integer> {

    /**
     * Busca todas las reservas de un usuario específico.
     * @param idUsuario el id del usuario.
     * @return lista de reservas del usuario.
     */
    List<Reserva> findByUsuarioIdUsuario(Integer idUsuario);

    /**
     * Busca todas las reservas de un vuelo específico.
     * @param idVuelo el id del vuelo.
     * @return lista de reservas del vuelo.
     */
    List<Reserva> findByVueloIdVuelo(Integer idVuelo);
}