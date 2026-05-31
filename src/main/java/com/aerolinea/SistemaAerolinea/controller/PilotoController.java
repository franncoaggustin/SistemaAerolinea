package com.aerolinea.SistemaAerolinea.controller;

import com.aerolinea.SistemaAerolinea.dto.PilotoDTO;
import com.aerolinea.SistemaAerolinea.model.Piloto;
import com.aerolinea.SistemaAerolinea.service.PilotoService;
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
 * Controlador REST para la entidad Piloto.
 * Expone los endpoints HTTP para que el frontend pueda consumir
 * los servicios de pilotos via JSON.
 * Se conecta con PilotoService para ejecutar la lógica de negocio.
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/pilotos")
public class PilotoController {

    @Autowired
    private PilotoService pilotoService;

    /**
     * Retorna la lista de todos los pilotos.
     * @return lista de pilotos en formato JSON.
     */
    @GetMapping
    public ResponseEntity<?> getAll() {
        try {
            List<Piloto> pilotos = pilotoService.listarPilotos();
            return ResponseEntity.status(HttpStatus.OK).body(pilotos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\":\"Error al obtener los pilotos.\"}");
        }
    }

    /**
     * Retorna la lista de pilotos como DTOs para el frontend.
     * @return lista de PilotoDTO en formato JSON.
     */
    @GetMapping("/dto")
    public ResponseEntity<?> getAllDTO() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(pilotoService.listarDTO());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\":\"Error al obtener los pilotos como DTO.\"}");
        }
    }

    /**
     * Retorna un piloto por su id como DTO.
     * @param id el id del registro.
     * @return el registro encontrado como DTO o error 404 si no existe.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getOne(@PathVariable Integer id) {
        try {
            return pilotoService.buscarPorId(id)
                    .map(p -> {
                        PilotoDTO dto = new PilotoDTO();
                        dto.setIdPiloto(p.getIdPiloto());
                        dto.setNumeroPiloto(p.getNumeroPiloto());
                        if (p.getPersona() != null) { dto.setIdPersona(p.getPersona().getIdPersona()); dto.setNombrePersona(p.getPersona().getNombrePersona()); dto.setApellidoPersona(p.getPersona().getApellidoPersona()); }
                        return ResponseEntity.status(HttpStatus.OK).body((Object) dto);
                    })
                    .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body("{\"error\":\"Piloto no encontrado.\"}"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\":\"Error al buscar el registro.\"}");
        }
    }

    /**
     * Crea un nuevo piloto.
     * Valida los datos recibidos antes de persistirlos.
     * @param piloto el piloto a crear recibido en el body.
     * @return el piloto creado con su id generado.
     */
    @PostMapping
    public ResponseEntity<?> save(@Valid @RequestBody Piloto piloto) {
        try {
            Piloto guardado = pilotoService.guardar(piloto);
            return ResponseEntity.status(HttpStatus.CREATED).body(guardado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"error\":\"Error al crear el piloto.\"}");
        }
    }

    /**
     * Actualiza un piloto existente.
     * Valida los datos recibidos antes de persistirlos.
     * @param id el id del piloto a actualizar.
     * @param piloto los nuevos datos del piloto recibidos en el body.
     * @return el piloto actualizado.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @Valid @RequestBody Piloto piloto) {
        try {
            piloto.setIdPiloto(id);
            Piloto actualizado = pilotoService.guardar(piloto);
            return ResponseEntity.status(HttpStatus.OK).body(actualizado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"error\":\"Error al actualizar el piloto.\"}");
        }
    }

    /**
     * Elimina un piloto por su id.
     * @param id el id del piloto a eliminar.
     * @return respuesta sin contenido si se eliminó correctamente.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        try {
            pilotoService.eliminar(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"error\":\"Error al eliminar el piloto.\"}");
        }
    }

    /**
     * Captura los errores de validación cuando @Valid rechaza los datos recibidos en el body.
     * Devuelve la lista de mensajes de error al frontend en formato JSON.
     * @param excepcion la excepción lanzada por Spring al fallar la validación de Piloto.
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