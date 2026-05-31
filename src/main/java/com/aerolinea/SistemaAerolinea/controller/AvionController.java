package com.aerolinea.SistemaAerolinea.controller;

import com.aerolinea.SistemaAerolinea.dto.AvionDTO;
import com.aerolinea.SistemaAerolinea.model.Avion;
import com.aerolinea.SistemaAerolinea.service.AvionService;
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
 * Controlador REST para la entidad Avion.
 * Expone los endpoints HTTP para que el frontend pueda consumir
 * los servicios de aviones via JSON.
 * Se conecta con AvionService para ejecutar la lógica de negocio.
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/aviones")
public class AvionController {

    @Autowired
    private AvionService avionService;

    /**
     * Retorna la lista de todos los aviones.
     * @return lista de aviones en formato JSON.
     */
    @GetMapping
    public ResponseEntity<?> getAll() {
        try {
            List<Avion> aviones = avionService.listarAviones();
            return ResponseEntity.status(HttpStatus.OK).body(aviones);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\":\"Error al obtener los aviones.\"}");
        }
    }

    /**
     * Retorna la lista de aviones como DTOs para el frontend.
     * @return lista de AvionDTO en formato JSON.
     */
    @GetMapping("/dto")
    public ResponseEntity<?> getAllDTO() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(avionService.listarDTO());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\":\"Error al obtener los aviones como DTO.\"}");
        }
    }

    /**
     * Retorna un avión por su id como DTO.
     * @param id el id del registro.
     * @return el registro encontrado como DTO o error 404 si no existe.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getOne(@PathVariable Integer id) {
        try {
            return avionService.buscarPorId(id)
                    .map(a -> {
                        AvionDTO dto = new AvionDTO();
                        dto.setIdAvion(a.getIdAvion());
                        dto.setNumeroAvion(a.getNumeroAvion());
                        dto.setTipoAvion(a.getTipoAvion());
                        dto.setTipoTurbina(a.getTipoTurbina());
                        if (a.getAerolinea() != null) { dto.setIdAerolinea(a.getAerolinea().getIdAerolinea()); dto.setNombreAerolinea(a.getAerolinea().getNombreAerolinea()); }
                        return ResponseEntity.status(HttpStatus.OK).body((Object) dto);
                    })
                    .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body("{\"error\":\"Avión no encontrado.\"}"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\":\"Error al buscar el registro.\"}");
        }
    }

    /**
     * Crea un nuevo avión.
     * Valida los datos recibidos antes de persistirlos.
     * @param avion el avión a crear recibido en el body.
     * @return el avión creado con su id generado.
     */
    @PostMapping
    public ResponseEntity<?> save(@Valid @RequestBody Avion avion) {
        try {
            Avion guardado = avionService.guardar(avion);
            return ResponseEntity.status(HttpStatus.CREATED).body(guardado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"error\":\"Error al crear el avión.\"}");
        }
    }

    /**
     * Actualiza un avión existente.
     * Valida los datos recibidos antes de persistirlos.
     * @param id el id del avión a actualizar.
     * @param avion los nuevos datos del avión recibidos en el body.
     * @return el avión actualizado.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @Valid @RequestBody Avion avion) {
        try {
            avion.setIdAvion(id);
            Avion actualizado = avionService.guardar(avion);
            return ResponseEntity.status(HttpStatus.OK).body(actualizado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"error\":\"Error al actualizar el avión.\"}");
        }
    }

    /**
     * Elimina un avión por su id.
     * @param id el id del avión a eliminar.
     * @return respuesta sin contenido si se eliminó correctamente.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        try {
            avionService.eliminar(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"error\":\"Error al eliminar el avión.\"}");
        }
    }

    /**
     * Captura los errores de validación cuando @Valid rechaza los datos recibidos en el body.
     * Devuelve la lista de mensajes de error al frontend en formato JSON.
     * @param excepcion la excepción lanzada por Spring al fallar la validación de Avion.
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