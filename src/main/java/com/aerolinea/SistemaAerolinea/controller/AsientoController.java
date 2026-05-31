package com.aerolinea.SistemaAerolinea.controller;

import com.aerolinea.SistemaAerolinea.dto.AsientoDTO;
import com.aerolinea.SistemaAerolinea.model.Asiento;
import com.aerolinea.SistemaAerolinea.service.AsientoService;
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
 * Controlador REST para la entidad Asiento.
 * Expone los endpoints HTTP para que el frontend pueda consumir
 * los servicios de asientos via JSON.
 * Se conecta con AsientoService para ejecutar la lógica de negocio.
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/asientos")
public class AsientoController {

    @Autowired
    private AsientoService asientoService;

    /**
     * Retorna la lista de todos los asientos.
     * @return lista de asientos en formato JSON.
     */
    @GetMapping
    public ResponseEntity<?> getAll() {
        try {
            List<Asiento> asientos = asientoService.listarAsientos();
            return ResponseEntity.status(HttpStatus.OK).body(asientos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\":\"Error al obtener los asientos.\"}");
        }
    }

    /**
     * Retorna la lista de asientos como DTOs para el frontend.
     * @return lista de AsientoDTO en formato JSON.
     */
    @GetMapping("/dto")
    public ResponseEntity<?> getAllDTO() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(asientoService.listarDTO());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\":\"Error al obtener los asientos como DTO.\"}");
        }
    }

    /**
     * Retorna un asiento por su id como DTO.
     * @param id el id del registro.
     * @return el registro encontrado como DTO o error 404 si no existe.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getOne(@PathVariable Integer id) {
        try {
            return asientoService.buscarPorId(id)
                    .map(a -> {
                        AsientoDTO dto = new AsientoDTO();
                        dto.setIdAsiento(a.getIdAsiento());
                        dto.setFilaAsiento(a.getFilaAsiento());
                        dto.setLetraAsiento(a.getLetraAsiento());
                        dto.setEstado(a.getEstado());
                        if (a.getAvion() != null) dto.setIdAvion(a.getAvion().getIdAvion());
                        if (a.getClase() != null) { dto.setIdClase(a.getClase().getIdClase()); dto.setNombreClase(a.getClase().getNombreClase()); }
                        return ResponseEntity.status(HttpStatus.OK).body((Object) dto);
                    })
                    .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body("{\"error\":\"Asiento no encontrado.\"}"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\":\"Error al buscar el registro.\"}");
        }
    }

    /**
     * Crea un nuevo asiento.
     * Valida los datos recibidos antes de persistirlos.
     * @param asiento el asiento a crear recibido en el body.
     * @return el asiento creado con su id generado.
     */
    @PostMapping
    public ResponseEntity<?> save(@Valid @RequestBody Asiento asiento) {
        try {
            Asiento guardado = asientoService.guardar(asiento);
            return ResponseEntity.status(HttpStatus.CREATED).body(guardado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"error\":\"Error al crear el asiento.\"}");
        }
    }

    /**
     * Actualiza un asiento existente.
     * Valida los datos recibidos antes de persistirlos.
     * @param id el id del asiento a actualizar.
     * @param asiento los nuevos datos del asiento recibidos en el body.
     * @return el asiento actualizado.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @Valid @RequestBody Asiento asiento) {
        try {
            asiento.setIdAsiento(id);
            Asiento actualizado = asientoService.guardar(asiento);
            return ResponseEntity.status(HttpStatus.OK).body(actualizado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"error\":\"Error al actualizar el asiento.\"}");
        }
    }

    /**
     * Elimina un asiento por su id.
     * @param id el id del asiento a eliminar.
     * @return respuesta sin contenido si se eliminó correctamente.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        try {
            asientoService.eliminar(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"error\":\"Error al eliminar el asiento.\"}");
        }
    }

    /**
     * Captura los errores de validación cuando @Valid rechaza los datos recibidos en el body.
     * Devuelve la lista de mensajes de error al frontend en formato JSON.
     * @param excepcion la excepción lanzada por Spring al fallar la validación de Asiento.
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