package com.aerolinea.SistemaAerolinea.controller;

import com.aerolinea.SistemaAerolinea.dto.PersonaDTO;
import com.aerolinea.SistemaAerolinea.model.Persona;
import com.aerolinea.SistemaAerolinea.service.PersonaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controlador REST para la entidad Persona.
 * Expone los endpoints HTTP para que el frontend pueda consumir
 * los servicios de personas via JSON.
 * Se conecta con PersonaService para ejecutar la lógica de negocio.
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/personas")
public class PersonaController {

    @Autowired
    private PersonaService personaService;

    /**
     * Retorna la lista de todas los personas.
     * @return lista de personas en formato JSON.
     */
    @GetMapping
    public ResponseEntity<?> getAll() {
        try {
            List<Persona> personas = personaService.listarPersonas();
            return ResponseEntity.status(HttpStatus.OK).body(personas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\":\"Error al obtener los personas.\"}");
        }
    }

    /**
     * Retorna la lista de personas como DTOs para el frontend.
     * @return lista de PersonaDTO en formato JSON.
     */
    @GetMapping("/dto")
    public ResponseEntity<?> getAllDTO() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(personaService.listarDTO());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\":\"Error al obtener los personas como DTO.\"}");
        }
    }

    /**
     * Retorna una persona por su id como DTO.
     * @param id el id del registro.
     * @return el registro encontrado como DTO o error 404 si no existe.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getOne(@PathVariable Integer id) {
        try {
            return personaService.buscarPorId(id)
                    .map(p -> {
                        PersonaDTO dto = new PersonaDTO();
                        dto.setIdPersona(p.getIdPersona());
                        dto.setDniPersona(p.getDniPersona());
                        dto.setNombrePersona(p.getNombrePersona());
                        dto.setApellidoPersona(p.getApellidoPersona());
                        return ResponseEntity.status(HttpStatus.OK).body((Object) dto);
                    })
                    .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body("{\"error\":\"Persona no encontrada.\"}"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\":\"Error al buscar el registro.\"}");
        }
    }

    /**
     * Crea una nueva persona.
     * Valida los datos recibidos antes de persistirlos.
     * @param persona el persona a crear recibido en el body.
     * @return el persona creado con su id generado.
     */
    @PostMapping
    public ResponseEntity<?> save(@Valid @RequestBody Persona persona) {
        try {
            Persona guardada = personaService.guardar(persona);
            return ResponseEntity.status(HttpStatus.CREATED).body(guardada);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"error\":\"Error al crear el persona.\"}");
        }
    }

    /**
     * Actualiza una persona existente.
     * Valida los datos recibidos antes de persistirlos.
     * @param id el id del persona a actualizar.
     * @param persona los nuevos datos del persona recibidos en el body.
     * @return el persona actualizado.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @Valid @RequestBody Persona persona) {
        try {
            persona.setIdPersona(id);
            Persona actualizada = personaService.guardar(persona);
            return ResponseEntity.status(HttpStatus.OK).body(actualizada);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"error\":\"Error al actualizar el persona.\"}");
        }
    }

    /**
     * Elimina una persona por su id.
     * @param id el id del persona a eliminar.
     * @return respuesta sin contenido si se eliminó correctamente.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        try {
            personaService.eliminar(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"error\":\"Error al eliminar el persona.\"}");
        }
    }

    /**
     * Captura los errores de validación cuando @Valid rechaza los datos recibidos en el body.
     * Devuelve la lista de mensajes de error al frontend en formato JSON.
     * @param excepcion la excepción lanzada por Spring al fallar la validación de Persona.
     * @return lista de mensajes de error con HTTP 400.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> manejarErroresDeValidacion(MethodArgumentNotValidException excepcion) {
        List<String> errores = excepcion.getBindingResult().getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errores);
    }
}