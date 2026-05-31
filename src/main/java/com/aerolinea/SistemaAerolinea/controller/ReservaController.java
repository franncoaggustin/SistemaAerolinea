package com.aerolinea.SistemaAerolinea.controller;

import com.aerolinea.SistemaAerolinea.dto.ReservaDTO;
import com.aerolinea.SistemaAerolinea.model.Reserva;
import com.aerolinea.SistemaAerolinea.service.ReservaService;
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
 * Controlador REST para la entidad Reserva.
 * Expone los endpoints HTTP para que el frontend pueda consumir
 * los servicios de reservas via JSON.
 * Se conecta con ReservaService para ejecutar la lógica de negocio.
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/reservas")
public class ReservaController {

    @Autowired
    private ReservaService reservaService;

    /**
     * Retorna la lista de todas las reservas.
     * @return lista de reservas en formato JSON.
     */
    @GetMapping
    public ResponseEntity<?> getAll() {
        try {
            List<Reserva> reservas = reservaService.listarReservas();
            return ResponseEntity.status(HttpStatus.OK).body(reservas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\":\"Error al obtener las reservas.\"}");
        }
    }

    /**
     * Retorna la lista de reservas como DTOs para el frontend.
     * @return lista de ReservaDTO en formato JSON.
     */
    @GetMapping("/dto")
    public ResponseEntity<?> getAllDTO() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(reservaService.listarDTO());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\":\"Error al obtener las reservas como DTO.\"}");
        }
    }

    /**
     * Retorna una reserva por su id como DTO.
     * @param id el id de la reserva.
     * @return la reserva encontrada como DTO o error 404 si no existe.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getOne(@PathVariable Integer id) {
        try {
            return reservaService.buscarPorId(id)
                    .map(r -> {
                        ReservaDTO dto = new ReservaDTO();
                        dto.setIdReserva(r.getIdReserva());
                        dto.setNumeroReserva(r.getNumeroReserva());
                        dto.setEstadoReserva(r.getEstadoReserva());
                        dto.setFechaReserva(r.getFechaReserva());
                        if (r.getUsuario() != null) dto.setIdUsuario(r.getUsuario().getIdUsuario());
                        if (r.getVuelo() != null) dto.setIdVuelo(r.getVuelo().getIdVuelo());
                        if (r.getAsiento() != null) dto.setIdAsiento(r.getAsiento().getIdAsiento());
                        if (r.getPago() != null) dto.setIdPago(r.getPago().getIdPago());
                        return ResponseEntity.status(HttpStatus.OK).body((Object) dto);
                    })
                    .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body("{\"error\":\"Reserva no encontrada.\"}"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\":\"Error al buscar la reserva.\"}");
        }
    }

    /**
     * Retorna las reservas de un usuario por su id como DTOs.
     * @param id el id del usuario.
     * @return lista de ReservaDTO del usuario.
     */
    @GetMapping("/usuario/{id}")
    public ResponseEntity<?> getByUsuario(@PathVariable Integer id) {
        try {
            List<ReservaDTO> dtos = reservaService.buscarPorUsuario(id).stream()
                    .map(r -> {
                        ReservaDTO dto = new ReservaDTO();
                        dto.setIdReserva(r.getIdReserva());
                        dto.setNumeroReserva(r.getNumeroReserva());
                        dto.setEstadoReserva(r.getEstadoReserva());
                        dto.setFechaReserva(r.getFechaReserva());
                        if (r.getUsuario() != null) dto.setIdUsuario(r.getUsuario().getIdUsuario());
                        if (r.getVuelo() != null) dto.setIdVuelo(r.getVuelo().getIdVuelo());
                        if (r.getAsiento() != null) dto.setIdAsiento(r.getAsiento().getIdAsiento());
                        if (r.getPago() != null) dto.setIdPago(r.getPago().getIdPago());
                        return dto;
                    })
                    .collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.OK).body(dtos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\":\"Error al buscar reservas por usuario.\"}");
        }
    }

    /**
     * Retorna las reservas de un vuelo por su id como DTOs.
     * @param idVuelo el id del vuelo.
     * @return lista de ReservaDTO del vuelo.
     */
    @GetMapping("/vuelo/{idVuelo}")
    public ResponseEntity<?> getByVuelo(@PathVariable Integer idVuelo) {
        try {
            List<ReservaDTO> dtos = reservaService.buscarPorVuelo(idVuelo).stream()
                    .map(r -> {
                        ReservaDTO dto = new ReservaDTO();
                        dto.setIdReserva(r.getIdReserva());
                        dto.setNumeroReserva(r.getNumeroReserva());
                        dto.setEstadoReserva(r.getEstadoReserva());
                        dto.setFechaReserva(r.getFechaReserva());
                        if (r.getUsuario() != null) dto.setIdUsuario(r.getUsuario().getIdUsuario());
                        if (r.getVuelo() != null) dto.setIdVuelo(r.getVuelo().getIdVuelo());
                        if (r.getAsiento() != null) dto.setIdAsiento(r.getAsiento().getIdAsiento());
                        if (r.getPago() != null) dto.setIdPago(r.getPago().getIdPago());
                        return dto;
                    })
                    .collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.OK).body(dtos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\":\"Error al obtener las reservas del vuelo.\"}");
        }
    }

    /**
     * Crea una nueva reserva.
     * Valida los datos recibidos antes de persistirlos.
     * @param reserva la reserva a crear recibida en el body.
     * @return la reserva creada con su id generado.
     */
    @PostMapping
    public ResponseEntity<?> save(@Valid @RequestBody Reserva reserva) {
        try {
            Reserva guardada = reservaService.guardar(reserva);
            return ResponseEntity.status(HttpStatus.CREATED).body(guardada);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"error\":\"Error al crear la reserva.\"}");
        }
    }

    /**
     * Actualiza una reserva existente.
     * Valida los datos recibidos antes de persistirlos.
     * @param id el id de la reserva a actualizar.
     * @param reserva los nuevos datos de la reserva recibidos en el body.
     * @return la reserva actualizada.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @Valid @RequestBody Reserva reserva) {
        try {
            reserva.setIdReserva(id);
            Reserva actualizada = reservaService.guardar(reserva);
            return ResponseEntity.status(HttpStatus.OK).body(actualizada);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"error\":\"Error al actualizar la reserva.\"}");
        }
    }

    /**
     * Elimina una reserva por su id.
     * @param id el id de la reserva a eliminar.
     * @return respuesta sin contenido si se eliminó correctamente.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        try {
            reservaService.eliminar(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"error\":\"Error al eliminar la reserva.\"}");
        }
    }

    /**
     * Captura los errores de validación cuando @Valid rechaza los datos recibidos en el body.
     * Devuelve la lista de mensajes de error al frontend en formato JSON.
     * @param excepcion la excepción lanzada por Spring al fallar la validación de Reserva.
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