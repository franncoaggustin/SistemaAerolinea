package com.aerolinea.SistemaAerolinea.controller;

import com.aerolinea.SistemaAerolinea.dto.AeropuertoDTO;
import com.aerolinea.SistemaAerolinea.model.Aeropuerto;
import com.aerolinea.SistemaAerolinea.service.AeropuertoService;
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
 * Controlador REST para la entidad Aeropuerto.
 * Expone los endpoints HTTP para que el frontend pueda consumir
 * los servicios de aeropuertos via JSON.
 * Se conecta con AeropuertoService para ejecutar la lógica de negocio.
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/aeropuertos")
public class AeropuertoController {

    @Autowired
    private AeropuertoService aeropuertoService;

    /**
     * Retorna la lista de todos los aeropuertos.
     * @return lista de aeropuertos en formato JSON.
     */
    @GetMapping
    public ResponseEntity<?> getAll() {
        try {
            List<Aeropuerto> aeropuertos = aeropuertoService.listarAeropuertos();
            return ResponseEntity.status(HttpStatus.OK).body(aeropuertos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\":\"Error al obtener los aeropuertos.\"}");
        }
    }

    /**
     * Retorna la lista de aeropuertos como DTOs para el frontend.
     * @return lista de AeropuertoDTO en formato JSON.
     */
    @GetMapping("/dto")
    public ResponseEntity<?> getAllDTO() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(aeropuertoService.listarDTO());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\":\"Error al obtener los aeropuertos como DTO.\"}");
        }
    }

    /**
     * Retorna un aeropuerto por su id como DTO.
     * @param id el id del registro.
     * @return el registro encontrado como DTO o error 404 si no existe.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getOne(@PathVariable Integer id) {
        try {
            return aeropuertoService.buscarPorId(id)
                    .map(a -> {
                        AeropuertoDTO dto = new AeropuertoDTO();
                        dto.setIdAeropuerto(a.getIdAeropuerto());
                        dto.setNombreAeropuerto(a.getNombreAeropuerto());
                        if (a.getCiudad() != null) { dto.setIdCiudad(a.getCiudad().getIdCiudad()); dto.setNombreCiudad(a.getCiudad().getNombreCiudad()); }
                        return ResponseEntity.status(HttpStatus.OK).body((Object) dto);
                    })
                    .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body("{\"error\":\"Aeropuerto no encontrado.\"}"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\":\"Error al buscar el registro.\"}");
        }
    }

    /**
     * Crea un nuevo aeropuerto.
     * Valida los datos recibidos antes de persistirlos.
     * @param aeropuerto el aeropuerto a crear recibido en el body.
     * @return el aeropuerto creado con su id generado.
     */
    @PostMapping
    public ResponseEntity<?> save(@Valid @RequestBody Aeropuerto aeropuerto) {
        try {
            Aeropuerto guardado = aeropuertoService.guardar(aeropuerto);
            return ResponseEntity.status(HttpStatus.CREATED).body(guardado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"error\":\"Error al crear el aeropuerto.\"}");
        }
    }

    /**
     * Actualiza un aeropuerto existente.
     * Valida los datos recibidos antes de persistirlos.
     * @param id el id del aeropuerto a actualizar.
     * @param aeropuerto los nuevos datos del aeropuerto recibidos en el body.
     * @return el aeropuerto actualizado.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @Valid @RequestBody Aeropuerto aeropuerto) {
        try {
            aeropuerto.setIdAeropuerto(id);
            Aeropuerto actualizado = aeropuertoService.guardar(aeropuerto);
            return ResponseEntity.status(HttpStatus.OK).body(actualizado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"error\":\"Error al actualizar el aeropuerto.\"}");
        }
    }

    /**
     * Elimina un aeropuerto por su id.
     * @param id el id del aeropuerto a eliminar.
     * @return respuesta sin contenido si se eliminó correctamente.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        try {
            aeropuertoService.eliminar(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"error\":\"Error al eliminar el aeropuerto.\"}");
        }
    }

    /**
     * Captura los errores de validación cuando @Valid rechaza los datos recibidos en el body.
     * Devuelve la lista de mensajes de error al frontend en formato JSON.
     * @param excepcion la excepción lanzada por Spring al fallar la validación de Aeropuerto.
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