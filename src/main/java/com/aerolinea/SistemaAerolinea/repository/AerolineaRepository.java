package com.aerolinea.SistemaAerolinea.repository;

import com.aerolinea.SistemaAerolinea.model.Aerolinea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para la entidad Aerolinea.
 * Provee operaciones CRUD automáticas sobre la tabla aerolinea.
 */
@Repository
public interface AerolineaRepository extends JpaRepository<Aerolinea, Integer> {
}