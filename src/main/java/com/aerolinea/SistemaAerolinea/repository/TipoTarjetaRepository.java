package com.aerolinea.SistemaAerolinea.repository;

import com.aerolinea.SistemaAerolinea.model.TipoTarjeta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para la entidad TipoTarjeta.
 * Provee operaciones CRUD automáticas sobre la tabla tipo_tarjeta.
 */
@Repository
public interface TipoTarjetaRepository extends JpaRepository<TipoTarjeta, Integer> {
}