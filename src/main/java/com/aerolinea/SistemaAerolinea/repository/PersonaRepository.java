package com.aerolinea.SistemaAerolinea.repository;

import com.aerolinea.SistemaAerolinea.model.Persona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para la entidad Persona.
 * Provee operaciones CRUD automáticas sobre la tabla persona.
 */
@Repository
public interface PersonaRepository extends JpaRepository<Persona, Integer> {
}