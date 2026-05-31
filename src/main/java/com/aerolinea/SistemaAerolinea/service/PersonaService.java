package com.aerolinea.SistemaAerolinea.service;

import com.aerolinea.SistemaAerolinea.dto.PersonaDTO;
import com.aerolinea.SistemaAerolinea.model.Persona;
import com.aerolinea.SistemaAerolinea.repository.PersonaRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Servicio para la gestión de personas.
 * Contiene la lógica de negocio y conversión a DTO.
 * Se conecta con PersonaRepository para acceder a la base de datos.
 */
@Service
public class PersonaService {

    @Autowired
    private PersonaRepository personaRepository;

    private final ModelMapper modelMapper = new ModelMapper();

    /**
     * Retorna la lista de todas las personas registradas.
     * @return lista de personas.
     */
    public List<Persona> listarPersonas() {
        return personaRepository.findAll();
    }

    /**
     * Retorna la lista de personas como DTOs para el frontend.
     * @return lista de PersonaDTO.
     */
    public List<PersonaDTO> listarDTO() {
        return personaRepository.findAll().stream()
                .map(p -> modelMapper.map(p, PersonaDTO.class))
                .collect(Collectors.toList());
    }

    /**
     * Busca una persona por su id.
     * @param id el id de la persona.
     * @return la persona encontrada o vacío si no existe.
     */
    public Optional<Persona> buscarPorId(Integer id) {
        return personaRepository.findById(id);
    }

    /**
     * Guarda una nueva persona o actualiza una existente.
     * @param persona la persona a guardar.
     * @return la persona guardada con su id generado.
     */
    public Persona guardar(Persona persona) {
        return personaRepository.save(persona);
    }

    /**
     * Elimina una persona por su id.
     * @param id el id de la persona a eliminar.
     */
    public void eliminar(Integer id) {
        personaRepository.deleteById(id);
    }
}