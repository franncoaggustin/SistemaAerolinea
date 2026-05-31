package com.aerolinea.SistemaAerolinea.controller;

import com.aerolinea.SistemaAerolinea.dto.TarifaDTO;
import com.aerolinea.SistemaAerolinea.model.Tarifa;
import com.aerolinea.SistemaAerolinea.service.TarifaService;
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
 * Controlador REST para la entidad Tarifa.
 * Expone los endpoints HTTP para que el frontend pueda consumir
 * los servicios de tarifas via JSON.
 * Se conecta con TarifaService para ejecutar la lógica de negocio.
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/tarifas")
public class TarifaController {

    @Autowired
    private TarifaService tarifaService;

    /**
     * Retorna la lista de todas las tarifas.
     * @return lista de tarifas en formato JSON.
     */
    @GetMapping
    public ResponseEntity<?> getAll() {
        try {
            List<Tarifa> tarifas = tarifaService.listarTarifas();
            return ResponseEntity.status(HttpStatus.OK).body(tarifas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\":\"Error al obtener las tarifas.\"}");
        }
    }

    /**
     * Retorna la lista de tarifas como DTOs para el frontend.
     * @return lista de TarifaDTO en formato JSON.
     */
    @GetMapping("/dto")
    public ResponseEntity<?> getAllDTO() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(tarifaService.listarDTO());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\":\"Error al obtener las tarifas como DTO.\"}");
        }
    }

    /**
     * Retorna una tarifa por su id como DTO.
     * @param id el id del registro.
     * @return el registro encontrado como DTO o error 404 si no existe.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getOne(@PathVariable Integer id) {
        try {
            return tarifaService.buscarPorId(id)
                    .map(t -> {
                        TarifaDTO dto = new TarifaDTO();
                        dto.setIdTarifa(t.getIdTarifa());
                        dto.setNumeroTarifa(t.getNumeroTarifa());
                        dto.setPrecioTarifa(t.getPrecioTarifa());
                        dto.setImpuestoTarifa(t.getImpuestoTarifa());
                        if (t.getVuelo() != null) dto.setIdVuelo(t.getVuelo().getIdVuelo());
                        if (t.getClase() != null) { dto.setIdClase(t.getClase().getIdClase()); dto.setNombreClase(t.getClase().getNombreClase()); }
                        return ResponseEntity.status(HttpStatus.OK).body((Object) dto);
                    })
                    .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body("{\"error\":\"Tarifa no encontrada.\"}"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\":\"Error al buscar el registro.\"}");
        }
    }

    /**
     * Crea una nueva tarifa.
     * Valida los datos recibidos antes de persistirlos.
     * @param tarifa la tarifa a crear recibida en el body.
     * @return la tarifa creada con su id generado.
     */
    @PostMapping
    public ResponseEntity<?> save(@Valid @RequestBody Tarifa tarifa) {
        try {
            Tarifa guardada = tarifaService.guardar(tarifa);
            return ResponseEntity.status(HttpStatus.CREATED).body(guardada);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"error\":\"Error al crear la tarifa.\"}");
        }
    }

    /**
     * Actualiza una tarifa existente.
     * Valida los datos recibidos antes de persistirlos.
     * @param id el id de la tarifa a actualizar.
     * @param tarifa los nuevos datos de la tarifa recibidos en el body.
     * @return la tarifa actualizada.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @Valid @RequestBody Tarifa tarifa) {
        try {
            tarifa.setIdTarifa(id);
            Tarifa actualizada = tarifaService.guardar(tarifa);
            return ResponseEntity.status(HttpStatus.OK).body(actualizada);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"error\":\"Error al actualizar la tarifa.\"}");
        }
    }

    /**
     * Elimina una tarifa por su id.
     * @param id el id de la tarifa a eliminar.
     * @return respuesta sin contenido si se eliminó correctamente.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        try {
            tarifaService.eliminar(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"error\":\"Error al eliminar la tarifa.\"}");
        }
    }

    /**
     * Captura los errores de validación cuando @Valid rechaza los datos recibidos en el body.
     * Devuelve la lista de mensajes de error al frontend en formato JSON.
     * @param excepcion la excepción lanzada por Spring al fallar la validación de Tarifa.
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