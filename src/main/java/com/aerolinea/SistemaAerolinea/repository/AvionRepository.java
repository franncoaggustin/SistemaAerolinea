package com.aerolinea.SistemaAerolinea.repository;

import com.aerolinea.SistemaAerolinea.model.Avion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para la entidad Avion.
 * Provee operaciones CRUD automáticas sobre la tabla avion.
 */
@Repository
public interface AvionRepository extends JpaRepository<Avion, Integer> {
}