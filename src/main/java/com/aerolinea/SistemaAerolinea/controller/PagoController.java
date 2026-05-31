package com.aerolinea.SistemaAerolinea.controller;

import com.aerolinea.SistemaAerolinea.dto.PagoDTO;
import com.aerolinea.SistemaAerolinea.model.Pago;
import com.aerolinea.SistemaAerolinea.service.PagoService;
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
 * Controlador REST para la entidad Pago.
 * Expone los endpoints HTTP para que el frontend pueda consumir
 * los servicios de pagos via JSON.
 * Se conecta con PagoService para ejecutar la lógica de negocio.
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/pagos")
public class PagoController {

    @Autowired
    private PagoService pagoService;

    /**
     * Retorna la lista de todos los pagos.
     * @return lista de pagos en formato JSON.
     */
    @GetMapping
    public ResponseEntity<?> getAll() {
        try {
            List<Pago> pagos = pagoService.listarPagos();
            return ResponseEntity.status(HttpStatus.OK).body(pagos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\":\"Error al obtener los pagos.\"}");
        }
    }

    /**
     * Retorna la lista de pagos como DTOs para el frontend.
     * @return lista de PagoDTO en formato JSON.
     */
    @GetMapping("/dto")
    public ResponseEntity<?> getAllDTO() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(pagoService.listarDTO());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\":\"Error al obtener los pagos como DTO.\"}");
        }
    }

    /**
     * Retorna un pago por su id como DTO.
     * @param id el id del registro.
     * @return el registro encontrado como DTO o error 404 si no existe.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getOne(@PathVariable Integer id) {
        try {
            return pagoService.buscarPorId(id)
                    .map(p -> {
                        PagoDTO dto = new PagoDTO();
                        dto.setIdPago(p.getIdPago());
                        dto.setNumeroPago(p.getNumeroPago());
                        dto.setCantidadPago(p.getCantidadPago());
                        dto.setFechaPago(p.getFechaPago());
                        return ResponseEntity.status(HttpStatus.OK).body((Object) dto);
                    })
                    .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body("{\"error\":\"Pago no encontrado.\"}"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\":\"Error al buscar el registro.\"}");
        }
    }

    /**
     * Crea un nuevo pago.
     * Valida los datos recibidos antes de persistirlos.
     * @param pago el pago a crear recibido en el body.
     * @return el pago creado con su id generado.
     */
    @PostMapping
    public ResponseEntity<?> save(@Valid @RequestBody Pago pago) {
        try {
            Pago guardado = pagoService.guardar(pago);
            return ResponseEntity.status(HttpStatus.CREATED).body(guardado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"error\":\"Error al crear el pago.\"}");
        }
    }

    /**
     * Actualiza un pago existente.
     * Valida los datos recibidos antes de persistirlos.
     * @param id el id del pago a actualizar.
     * @param pago los nuevos datos del pago recibidos en el body.
     * @return el pago actualizado.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @Valid @RequestBody Pago pago) {
        try {
            pago.setIdPago(id);
            Pago actualizado = pagoService.guardar(pago);
            return ResponseEntity.status(HttpStatus.OK).body(actualizado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"error\":\"Error al actualizar el pago.\"}");
        }
    }

    /**
     * Elimina un pago por su id.
     * @param id el id del pago a eliminar.
     * @return respuesta sin contenido si se eliminó correctamente.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        try {
            pagoService.eliminar(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"error\":\"Error al eliminar el pago.\"}");
        }
    }

    /**
     * Captura los errores de validación cuando @Valid rechaza los datos recibidos en el body.
     * Devuelve la lista de mensajes de error al frontend en formato JSON.
     * @param excepcion la excepción lanzada por Spring al fallar la validación de Pago.
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