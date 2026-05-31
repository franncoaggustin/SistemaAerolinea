package com.aerolinea.SistemaAerolinea.repository;

import com.aerolinea.SistemaAerolinea.model.Clase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para la entidad Clase.
 * Provee operaciones CRUD automáticas sobre la tabla clase.
 */
@Repository
public interface ClaseRepository extends JpaRepository<Clase, Integer> {
}