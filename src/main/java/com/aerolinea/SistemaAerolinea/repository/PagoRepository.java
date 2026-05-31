package com.aerolinea.SistemaAerolinea.repository;

import com.aerolinea.SistemaAerolinea.model.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para la entidad Pago.
 * Provee operaciones CRUD automáticas sobre la tabla pago.
 */
@Repository
public interface PagoRepository extends JpaRepository<Pago, Integer> {
}