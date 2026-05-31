package com.aerolinea.SistemaAerolinea.repository;

import com.aerolinea.SistemaAerolinea.model.Tarifa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para la entidad Tarifa.
 * Provee operaciones CRUD automáticas sobre la tabla tarifa.
 */
@Repository
public interface TarifaRepository extends JpaRepository<Tarifa, Integer> {
}