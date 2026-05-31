package com.aerolinea.SistemaAerolinea.repository;

import com.aerolinea.SistemaAerolinea.model.Ciudad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para la entidad Ciudad.
 * Provee operaciones CRUD automáticas sobre la tabla ciudad.
 */
@Repository
public interface CiudadRepository extends JpaRepository<Ciudad, Integer> {
}