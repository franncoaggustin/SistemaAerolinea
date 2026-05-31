package com.aerolinea.SistemaAerolinea.repository;

import com.aerolinea.SistemaAerolinea.model.Asiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para la entidad Asiento.
 * Provee operaciones CRUD automáticas sobre la tabla asiento.
 */
@Repository
public interface AsientoRepository extends JpaRepository<Asiento, Integer> {
}