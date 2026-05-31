package com.aerolinea.SistemaAerolinea.controller;

import com.aerolinea.SistemaAerolinea.dto.VueloDTO;
import com.aerolinea.SistemaAerolinea.model.Vuelo;
import com.aerolinea.SistemaAerolinea.service.VueloService;
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
 * Controlador REST para la entidad Vuelo.
 * Expone los endpoints HTTP para que el frontend pueda consumir
 * los servicios de vuelos via JSON.
 * Se conecta con VueloService para ejecutar la lógica de negocio.
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/vuelos")
public class VueloController {

    @Autowired
    private VueloService vueloService;

    /**
     * Retorna la lista de todos los vuelos.
     * @return lista de vuelos en formato JSON.
     */
    @GetMapping
    public ResponseEntity<?> getAll() {
        try {
            List<Vuelo> vuelos = vueloService.listarVuelos();
            return ResponseEntity.status(HttpStatus.OK).body(vuelos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\":\"Error al obtener los vuelos.\"}");
        }
    }

    /**
     * Retorna la lista de vuelos como DTOs para el frontend.
     * @return lista de VueloDTO en formato JSON.
     */
    @GetMapping("/dto")
    public ResponseEntity<?> getAllDTO() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(vueloService.listarDTO());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\":\"Error al obtener los vuelos como DTO.\"}");
        }
    }

    /**
     * Retorna un vuelo por su id como DTO.
     * Construye el DTO manualmente para evitar referencias circulares en la serialización JSON.
     * @param id el id del registro.
     * @return el registro encontrado como DTO o error 404 si no existe.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getOne(@PathVariable Integer id) {
        try {
            return vueloService.buscarPorId(id)
                    .map(v -> {
                        VueloDTO dto = new VueloDTO();
                        dto.setIdVuelo(v.getIdVuelo());
                        dto.setNumeroVuelo(v.getNumeroVuelo());
                        dto.setEstadoVuelo(v.getEstadoVuelo());
                        dto.setFechaSalida(v.getFechaSalida());
                        if (v.getAvion() != null) dto.setIdAvion(v.getAvion().getIdAvion());
                        if (v.getPiloto() != null) dto.setIdPiloto(v.getPiloto().getIdPiloto());
                        if (v.getAerolinea() != null) { dto.setIdAerolinea(v.getAerolinea().getIdAerolinea()); dto.setNombreAerolinea(v.getAerolinea().getNombreAerolinea()); }
                        if (v.getAeropuertoOrigen() != null) { dto.setIdAeropuertoOrigen(v.getAeropuertoOrigen().getIdAeropuerto()); dto.setNombreAeropuertoOrigen(v.getAeropuertoOrigen().getNombreAeropuerto()); }
                        if (v.getAeropuertoDestino() != null) { dto.setIdAeropuertoDestino(v.getAeropuertoDestino().getIdAeropuerto()); dto.setNombreAeropuertoDestino(v.getAeropuertoDestino().getNombreAeropuerto()); }
                        return ResponseEntity.status(HttpStatus.OK).body((Object) dto);
                    })
                    .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body("{\"error\":\"Vuelo no encontrado.\"}"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\":\"Error al buscar el registro.\"}");
        }
    }

    /**
     * Crea un nuevo vuelo.
     * Valida los datos recibidos antes de persistirlos.
     * @param vuelo el vuelo a crear recibido en el body.
     * @return el vuelo creado con su id generado.
     */
    @PostMapping
    public ResponseEntity<?> save(@Valid @RequestBody Vuelo vuelo) {
        try {
            Vuelo guardado = vueloService.guardar(vuelo);
            return ResponseEntity.status(HttpStatus.CREATED).body(guardado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"error\":\"Error al crear el vuelo.\"}");
        }
    }

    /**
     * Actualiza un vuelo existente.
     * Valida los datos recibidos antes de persistirlos.
     * @param id el id del vuelo a actualizar.
     * @param vuelo los nuevos datos del vuelo recibidos en el body.
     * @return el vuelo actualizado.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @Valid @RequestBody Vuelo vuelo) {
        try {
            vuelo.setIdVuelo(id);
            Vuelo actualizado = vueloService.guardar(vuelo);
            return ResponseEntity.status(HttpStatus.OK).body(actualizado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"error\":\"Error al actualizar el vuelo.\"}");
        }
    }

    /**
     * Elimina un vuelo por su id.
     * @param id el id del vuelo a eliminar.
     * @return respuesta sin contenido si se eliminó correctamente.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        try {
            vueloService.eliminar(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"error\":\"Error al eliminar el vuelo.\"}");
        }
    }

    /**
     * Captura los errores de validación cuando @Valid rechaza los datos recibidos en el body.
     * Devuelve la lista de mensajes de error al frontend en formato JSON.
     * @param excepcion la excepción lanzada por Spring al fallar la validación de Vuelo.
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