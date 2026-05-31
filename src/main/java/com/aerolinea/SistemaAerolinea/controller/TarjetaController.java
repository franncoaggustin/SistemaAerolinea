package com.aerolinea.SistemaAerolinea.controller;

import com.aerolinea.SistemaAerolinea.dto.TarjetaDTO;
import com.aerolinea.SistemaAerolinea.model.Tarjeta;
import com.aerolinea.SistemaAerolinea.service.TarjetaService;
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
 * Controlador REST para la entidad Tarjeta.
 * Expone los endpoints HTTP para que el frontend pueda consumir
 * los servicios de tarjetas via JSON.
 * Tarjeta hereda de Pago mediante InheritanceType.JOINED.
 * Se conecta con TarjetaService para ejecutar la lógica de negocio.
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/tarjetas")
public class TarjetaController {

    @Autowired
    private TarjetaService tarjetaService;

    /**
     * Retorna la lista de todas las tarjetas.
     * @return lista de tarjetas en formato JSON.
     */
    @GetMapping
    public ResponseEntity<?> getAll() {
        try {
            List<Tarjeta> tarjetas = tarjetaService.listarTarjetas();
            return ResponseEntity.status(HttpStatus.OK).body(tarjetas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\":\"Error al obtener las tarjetas.\"}");
        }
    }

    /**
     * Retorna la lista de tarjetas como DTOs para el frontend.
     * @return lista de TarjetaDTO en formato JSON.
     */
    @GetMapping("/dto")
    public ResponseEntity<?> getAllDTO() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(tarjetaService.listarDTO());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\":\"Error al obtener las tarjetas como DTO.\"}");
        }
    }

    /**
     * Retorna una tarjeta por su id como DTO.
     * Usa idPago como identificador al ser Tarjeta hija de Pago.
     * @param id el id del registro (idPago heredado).
     * @return el registro encontrado como DTO o error 404 si no existe.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getOne(@PathVariable Integer id) {
        try {
            return tarjetaService.buscarPorId(id)
                    .map(t -> {
                        TarjetaDTO dto = new TarjetaDTO();
                        dto.setIdPago(t.getIdPago());
                        dto.setNumeroTarjeta(t.getNumeroTarjeta());
                        if (t.getTipoTarjeta() != null) { dto.setIdTipoTarjeta(t.getTipoTarjeta().getIdTipoTarjeta()); dto.setTipoTarjeta(t.getTipoTarjeta().getTipoTarjeta()); }
                        return ResponseEntity.status(HttpStatus.OK).body((Object) dto);
                    })
                    .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body("{\"error\":\"Tarjeta no encontrada.\"}"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\":\"Error al buscar el registro.\"}");
        }
    }

    /**
     * Crea una nueva tarjeta.
     * Valida los datos recibidos antes de persistirlos.
     * @param tarjeta la tarjeta a crear recibida en el body.
     * @return la tarjeta creada con su id generado.
     */
    @PostMapping
    public ResponseEntity<?> save(@Valid @RequestBody Tarjeta tarjeta) {
        try {
            Tarjeta guardada = tarjetaService.guardar(tarjeta);
            return ResponseEntity.status(HttpStatus.CREATED).body(guardada);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"error\":\"Error al crear la tarjeta.\"}");
        }
    }

    /**
     * Actualiza una tarjeta existente.
     * Usa setIdPago por herencia JPA con InheritanceType.JOINED.
     * Valida los datos recibidos antes de persistirlos.
     * @param id el id de la tarjeta a actualizar (idPago heredado).
     * @param tarjeta los nuevos datos de la tarjeta recibidos en el body.
     * @return la tarjeta actualizada.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @Valid @RequestBody Tarjeta tarjeta) {
        try {
            tarjeta.setIdPago(id);
            Tarjeta actualizada = tarjetaService.guardar(tarjeta);
            return ResponseEntity.status(HttpStatus.OK).body(actualizada);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"error\":\"Error al actualizar la tarjeta.\"}");
        }
    }

    /**
     * Elimina una tarjeta por su id.
     * @param id el id de la tarjeta a eliminar.
     * @return respuesta sin contenido si se eliminó correctamente.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        try {
            tarjetaService.eliminar(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"error\":\"Error al eliminar la tarjeta.\"}");
        }
    }

    /**
     * Captura los errores de validación cuando @Valid rechaza los datos recibidos en el body.
     * Devuelve la lista de mensajes de error al frontend en formato JSON.
     * @param excepcion la excepción lanzada por Spring al fallar la validación de Tarjeta.
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