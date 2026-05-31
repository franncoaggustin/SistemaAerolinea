package com.aerolinea.SistemaAerolinea.controller;

import com.aerolinea.SistemaAerolinea.dto.ConsultaDTO;
import com.aerolinea.SistemaAerolinea.model.Consulta;
import com.aerolinea.SistemaAerolinea.service.ConsultaService;
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
 * Controlador REST para la entidad Consulta.
 * Expone los endpoints HTTP para que el frontend pueda consumir
 * los servicios de consultas via JSON.
 * Se conecta con ConsultaService para ejecutar la lógica de negocio.
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/consultas")
public class ConsultaController {

    @Autowired
    private ConsultaService consultaService;

    /**
     * Retorna la lista de todas las consultas.
     * @return lista de consultas en formato JSON.
     */
    @GetMapping
    public ResponseEntity<?> getAll() {
        try {
            List<Consulta> consultas = consultaService.listarConsultas();
            return ResponseEntity.status(HttpStatus.OK).body(consultas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\":\"Error al obtener las consultas.\"}");
        }
    }

    /**
     * Retorna la lista de consultas como DTOs para el frontend.
     * @return lista de ConsultaDTO en formato JSON.
     */
    @GetMapping("/dto")
    public ResponseEntity<?> getAllDTO() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(consultaService.listarDTO());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\":\"Error al obtener las consultas como DTO.\"}");
        }
    }

    /**
     * Retorna una consulta por su id como DTO.
     * @param id el id de la consulta.
     * @return la consulta encontrada como DTO o error 404 si no existe.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getOne(@PathVariable Integer id) {
        try {
            return consultaService.buscarPorId(id)
                    .map(c -> {
                        ConsultaDTO dto = new ConsultaDTO();
                        dto.setIdConsulta(c.getIdConsulta());
                        dto.setNumeroConsulta(c.getNumeroConsulta());
                        dto.setFechaConsulta(c.getFechaConsulta());
                        dto.setTipoConsulta(c.getTipoConsulta());
                        dto.setDetalleConsulta(c.getDetalleConsulta());
                        dto.setEstadoConsulta(c.getEstadoConsulta());
                        if (c.getUsuario() != null) dto.setIdUsuario(c.getUsuario().getIdUsuario());
                        if (c.getVuelo() != null) dto.setIdVuelo(c.getVuelo().getIdVuelo());
                        return ResponseEntity.status(HttpStatus.OK).body((Object) dto);
                    })
                    .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body("{\"error\":\"Consulta no encontrada.\"}"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\":\"Error al buscar la consulta.\"}");
        }
    }

    /**
     * Retorna las consultas de un usuario por su id como DTOs.
     * @param idUsuario el id del usuario.
     * @return lista de ConsultaDTO del usuario.
     */
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<?> getByUsuario(@PathVariable Integer idUsuario) {
        try {
            List<ConsultaDTO> dtos = consultaService.buscarPorUsuario(idUsuario).stream()
                    .map(c -> {
                        ConsultaDTO dto = new ConsultaDTO();
                        dto.setIdConsulta(c.getIdConsulta());
                        dto.setNumeroConsulta(c.getNumeroConsulta());
                        dto.setFechaConsulta(c.getFechaConsulta());
                        dto.setTipoConsulta(c.getTipoConsulta());
                        dto.setDetalleConsulta(c.getDetalleConsulta());
                        dto.setEstadoConsulta(c.getEstadoConsulta());
                        if (c.getUsuario() != null) dto.setIdUsuario(c.getUsuario().getIdUsuario());
                        if (c.getVuelo() != null) dto.setIdVuelo(c.getVuelo().getIdVuelo());
                        return dto;
                    })
                    .collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.OK).body(dtos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\":\"Error al obtener las consultas del usuario.\"}");
        }
    }

    /**
     * Crea una nueva consulta.
     * Valida los datos recibidos antes de persistirlos.
     * @param consulta la consulta a crear recibida en el body.
     * @return la consulta creada con su id generado.
     */
    @PostMapping
    public ResponseEntity<?> save(@Valid @RequestBody Consulta consulta) {
        try {
            Consulta guardada = consultaService.guardar(consulta);
            return ResponseEntity.status(HttpStatus.CREATED).body(guardada);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"error\":\"Error al crear la consulta.\"}");
        }
    }

    /**
     * Actualiza una consulta existente.
     * Valida los datos recibidos antes de persistirlos.
     * @param id el id de la consulta a actualizar.
     * @param consulta los nuevos datos de la consulta recibidos en el body.
     * @return la consulta actualizada.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @Valid @RequestBody Consulta consulta) {
        try {
            consulta.setIdConsulta(id);
            Consulta actualizada = consultaService.guardar(consulta);
            return ResponseEntity.status(HttpStatus.OK).body(actualizada);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"error\":\"Error al actualizar la consulta.\"}");
        }
    }

    /**
     * Elimina una consulta por su id.
     * @param id el id de la consulta a eliminar.
     * @return respuesta sin contenido si se eliminó correctamente.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        try {
            consultaService.eliminar(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"error\":\"Error al eliminar la consulta.\"}");
        }
    }

    /**
     * Captura los errores de validación cuando @Valid rechaza los datos recibidos en el body.
     * Devuelve la lista de mensajes de error al frontend en formato JSON.
     * @param excepcion la excepción lanzada por Spring al fallar la validación de Consulta.
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