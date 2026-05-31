package com.aerolinea.SistemaAerolinea.repository;

import com.aerolinea.SistemaAerolinea.model.Vuelo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para la entidad Vuelo.
 * Provee operaciones CRUD automáticas sobre la tabla vuelo.
 */
@Repository
public interface VueloRepository extends JpaRepository<Vuelo, Integer> {
}