package com.aerolinea.SistemaAerolinea.repository;

import com.aerolinea.SistemaAerolinea.model.Tarjeta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para la entidad Tarjeta.
 * Provee operaciones CRUD automáticas sobre la tabla tarjeta.
 */
@Repository
public interface TarjetaRepository extends JpaRepository<Tarjeta, Integer> {
}