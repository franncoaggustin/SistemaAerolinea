package com.aerolinea.SistemaAerolinea.controller;

import com.aerolinea.SistemaAerolinea.dto.TipoTarjetaDTO;
import com.aerolinea.SistemaAerolinea.model.TipoTarjeta;
import com.aerolinea.SistemaAerolinea.service.TipoTarjetaService;
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
 * Controlador REST para la entidad TipoTarjeta.
 * Expone los endpoints HTTP para que el frontend pueda consumir
 * los servicios de tipos de tarjeta via JSON.
 * Se conecta con TipoTarjetaService para ejecutar la lógica de negocio.
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/tipos-tarjeta")
public class TipoTarjetaController {

    @Autowired
    private TipoTarjetaService tipoTarjetaService;

    /**
     * Retorna la lista de todos los tipos de tarjeta.
     * @return lista de tipos de tarjeta en formato JSON.
     */
    @GetMapping
    public ResponseEntity<?> getAll() {
        try {
            List<TipoTarjeta> tipos = tipoTarjetaService.listarTiposTarjeta();
            return ResponseEntity.status(HttpStatus.OK).body(tipos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\":\"Error al obtener los tipos de tarjeta.\"}");
        }
    }

    /**
     * Retorna la lista de tipos de tarjeta como DTOs para el frontend.
     * @return lista de TipoTarjetaDTO en formato JSON.
     */
    @GetMapping("/dto")
    public ResponseEntity<?> getAllDTO() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(tipoTarjetaService.listarDTO());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\":\"Error al obtener los tipos de tarjeta como DTO.\"}");
        }
    }

    /**
     * Retorna un tipo de tarjeta por su id como DTO.
     * @param id el id del registro.
     * @return el registro encontrado como DTO o error 404 si no existe.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getOne(@PathVariable Integer id) {
        try {
            return tipoTarjetaService.buscarPorId(id)
                    .map(t -> {
                        TipoTarjetaDTO dto = new TipoTarjetaDTO();
                        dto.setIdTipoTarjeta(t.getIdTipoTarjeta());
                        dto.setTipoTarjeta(t.getTipoTarjeta());
                        return ResponseEntity.status(HttpStatus.OK).body((Object) dto);
                    })
                    .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body("{\"error\":\"Tipo de tarjeta no encontrado.\"}"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\":\"Error al buscar el registro.\"}");
        }
    }

    /**
     * Crea un nuevo tipo de tarjeta.
     * Valida los datos recibidos antes de persistirlos.
     * @param tipoTarjeta el tipo de tarjeta a crear recibido en el body.
     * @return el tipo de tarjeta creado con su id generado.
     */
    @PostMapping
    public ResponseEntity<?> save(@Valid @RequestBody TipoTarjeta tipoTarjeta) {
        try {
            TipoTarjeta guardado = tipoTarjetaService.guardar(tipoTarjeta);
            return ResponseEntity.status(HttpStatus.CREATED).body(guardado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"error\":\"Error al crear el tipo de tarjeta.\"}");
        }
    }

    /**
     * Actualiza un tipo de tarjeta existente.
     * Valida los datos recibidos antes de persistirlos.
     * @param id el id del tipo de tarjeta a actualizar.
     * @param tipoTarjeta los nuevos datos del tipo de tarjeta recibidos en el body.
     * @return el tipo de tarjeta actualizado.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @Valid @RequestBody TipoTarjeta tipoTarjeta) {
        try {
            tipoTarjeta.setIdTipoTarjeta(id);
            TipoTarjeta actualizado = tipoTarjetaService.guardar(tipoTarjeta);
            return ResponseEntity.status(HttpStatus.OK).body(actualizado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"error\":\"Error al actualizar el tipo de tarjeta.\"}");
        }
    }

    /**
     * Elimina un tipo de tarjeta por su id.
     * @param id el id del tipo de tarjeta a eliminar.
     * @return respuesta sin contenido si se eliminó correctamente.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        try {
            tipoTarjetaService.eliminar(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"error\":\"Error al eliminar el tipo de tarjeta.\"}");
        }
    }

    /**
     * Captura los errores de validación cuando @Valid rechaza los datos recibidos en el body.
     * Devuelve la lista de mensajes de error al frontend en formato JSON.
     * @param excepcion la excepción lanzada por Spring al fallar la validación de TipoTarjeta.
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