package com.aerolinea.SistemaAerolinea.repository;

import com.aerolinea.SistemaAerolinea.model.Piloto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para la entidad Piloto.
 * Provee operaciones CRUD automáticas sobre la tabla piloto.
 */
@Repository
public interface PilotoRepository extends JpaRepository<Piloto, Integer> {
}