package com.aerolinea.SistemaAerolinea.controller;

import com.aerolinea.SistemaAerolinea.dto.AerolineaDTO;
import com.aerolinea.SistemaAerolinea.model.Aerolinea;
import com.aerolinea.SistemaAerolinea.service.AerolineaService;
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
 * Controlador REST para la entidad Aerolinea.
 * Expone los endpoints HTTP para que el frontend pueda consumir
 * los servicios de aerolíneas via JSON.
 * Se conecta con AerolineaService para ejecutar la lógica de negocio.
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/aerolineas")
public class AerolineaController {

    @Autowired
    private AerolineaService aerolineaService;

    /**
     * Retorna la lista de todas las aerolíneas.
     * @return lista de aerolíneas en formato JSON.
     */
    @GetMapping
    public ResponseEntity<?> getAll() {
        try {
            List<Aerolinea> aerolineas = aerolineaService.listarAerolineas();
            return ResponseEntity.status(HttpStatus.OK).body(aerolineas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\":\"Error al obtener las aerolíneas.\"}");
        }
    }

    /**
     * Retorna la lista de aerolíneas como DTOs para el frontend.
     * @return lista de AerolineaDTO en formato JSON.
     */
    @GetMapping("/dto")
    public ResponseEntity<?> getAllDTO() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(aerolineaService.listarDTO());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\":\"Error al obtener las aerolíneas como DTO.\"}");
        }
    }

    /**
     * Retorna una aerolínea por su id como DTO.
     * @param id el id del registro.
     * @return el registro encontrado como DTO o error 404 si no existe.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getOne(@PathVariable Integer id) {
        try {
            return aerolineaService.buscarPorId(id)
                    .map(a -> {
                        AerolineaDTO dto = new AerolineaDTO();
                        dto.setIdAerolinea(a.getIdAerolinea());
                        dto.setNombreAerolinea(a.getNombreAerolinea());
                        return ResponseEntity.status(HttpStatus.OK).body((Object) dto);
                    })
                    .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body("{\"error\":\"Aerolínea no encontrada.\"}"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\":\"Error al buscar el registro.\"}");
        }
    }

    /**
     * Crea una nueva aerolínea.
     * Valida los datos recibidos antes de persistirlos.
     * @param aerolinea la aerolínea a crear recibida en el body.
     * @return la aerolínea creada con su id generado.
     */
    @PostMapping
    public ResponseEntity<?> save(@Valid @RequestBody Aerolinea aerolinea) {
        try {
            Aerolinea guardada = aerolineaService.guardar(aerolinea);
            return ResponseEntity.status(HttpStatus.CREATED).body(guardada);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"error\":\"Error al crear la aerolínea.\"}");
        }
    }

    /**
     * Actualiza una aerolínea existente.
     * Valida los datos recibidos antes de persistirlos.
     * @param id el id de la aerolínea a actualizar.
     * @param aerolinea los nuevos datos de la aerolínea recibidos en el body.
     * @return la aerolínea actualizada.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @Valid @RequestBody Aerolinea aerolinea) {
        try {
            aerolinea.setIdAerolinea(id);
            Aerolinea actualizada = aerolineaService.guardar(aerolinea);
            return ResponseEntity.status(HttpStatus.OK).body(actualizada);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"error\":\"Error al actualizar la aerolínea.\"}");
        }
    }

    /**
     * Elimina una aerolínea por su id.
     * @param id el id de la aerolínea a eliminar.
     * @return respuesta sin contenido si se eliminó correctamente.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        try {
            aerolineaService.eliminar(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"error\":\"Error al eliminar la aerolínea.\"}");
        }
    }

    /**
     * Captura los errores de validación cuando @Valid rechaza los datos recibidos en el body.
     * Devuelve la lista de mensajes de error al frontend en formato JSON.
     * @param excepcion la excepción lanzada por Spring al fallar la validación de Aerolinea.
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