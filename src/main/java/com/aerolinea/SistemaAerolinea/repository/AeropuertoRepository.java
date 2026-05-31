package com.aerolinea.SistemaAerolinea.repository;

import com.aerolinea.SistemaAerolinea.model.Aeropuerto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para la entidad Aeropuerto.
 * Provee operaciones CRUD automáticas sobre la tabla aeropuerto.
 */
@Repository
public interface AeropuertoRepository extends JpaRepository<Aeropuerto, Integer> {
}