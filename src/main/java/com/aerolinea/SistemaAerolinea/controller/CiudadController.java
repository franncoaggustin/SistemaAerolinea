package com.aerolinea.SistemaAerolinea.controller;

import com.aerolinea.SistemaAerolinea.dto.CiudadDTO;
import com.aerolinea.SistemaAerolinea.model.Ciudad;
import com.aerolinea.SistemaAerolinea.service.CiudadService;
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
 * Controlador REST para la entidad Ciudad.
 * Expone los endpoints HTTP para que el frontend pueda consumir
 * los servicios de ciudades via JSON.
 * Se conecta con CiudadService para ejecutar la lógica de negocio.
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/ciudades")
public class CiudadController {

    @Autowired
    private CiudadService ciudadService;

    /**
     * Retorna la lista de todas las ciudades.
     * @return lista de ciudades en formato JSON.
     */
    @GetMapping
    public ResponseEntity<?> getAll() {
        try {
            List<Ciudad> ciudades = ciudadService.listarCiudades();
            return ResponseEntity.status(HttpStatus.OK).body(ciudades);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\":\"Error al obtener las ciudades.\"}");
        }
    }

    /**
     * Retorna la lista de ciudades como DTOs para el frontend.
     * @return lista de CiudadDTO en formato JSON.
     */
    @GetMapping("/dto")
    public ResponseEntity<?> getAllDTO() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(ciudadService.listarDTO());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\":\"Error al obtener las ciudades como DTO.\"}");
        }
    }

    /**
     * Retorna una ciudad por su id como DTO.
     * @param id el id del registro.
     * @return el registro encontrado como DTO o error 404 si no existe.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getOne(@PathVariable Integer id) {
        try {
            return ciudadService.buscarPorId(id)
                    .map(c -> {
                        CiudadDTO dto = new CiudadDTO();
                        dto.setIdCiudad(c.getIdCiudad());
                        dto.setNombreCiudad(c.getNombreCiudad());
                        return ResponseEntity.status(HttpStatus.OK).body((Object) dto);
                    })
                    .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body("{\"error\":\"Ciudad no encontrada.\"}"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\":\"Error al buscar el registro.\"}");
        }
    }

    /**
     * Crea una nueva ciudad.
     * Valida los datos recibidos antes de persistirlos.
     * @param ciudad la ciudad a crear recibida en el body.
     * @return la ciudad creada con su id generado.
     */
    @PostMapping
    public ResponseEntity<?> save(@Valid @RequestBody Ciudad ciudad) {
        try {
            Ciudad guardada = ciudadService.guardar(ciudad);
            return ResponseEntity.status(HttpStatus.CREATED).body(guardada);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"error\":\"Error al crear la ciudad.\"}");
        }
    }

    /**
     * Actualiza una ciudad existente.
     * Valida los datos recibidos antes de persistirlos.
     * @param id el id de la ciudad a actualizar.
     * @param ciudad los nuevos datos de la ciudad recibidos en el body.
     * @return la ciudad actualizada.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @Valid @RequestBody Ciudad ciudad) {
        try {
            ciudad.setIdCiudad(id);
            Ciudad actualizada = ciudadService.guardar(ciudad);
            return ResponseEntity.status(HttpStatus.OK).body(actualizada);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"error\":\"Error al actualizar la ciudad.\"}");
        }
    }

    /**
     * Elimina una ciudad por su id.
     * @param id el id de la ciudad a eliminar.
     * @return respuesta sin contenido si se eliminó correctamente.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        try {
            ciudadService.eliminar(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"error\":\"Error al eliminar la ciudad.\"}");
        }
    }

    /**
     * Captura los errores de validación cuando @Valid rechaza los datos recibidos en el body.
     * Devuelve la lista de mensajes de error al frontend en formato JSON.
     * @param excepcion la excepción lanzada por Spring al fallar la validación de Ciudad.
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