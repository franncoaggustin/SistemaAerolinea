package com.aerolinea.SistemaAerolinea.controller;

import com.aerolinea.SistemaAerolinea.dto.UsuarioDTO;
import com.aerolinea.SistemaAerolinea.model.Usuario;
import com.aerolinea.SistemaAerolinea.service.UsuarioService;
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
 * Controlador REST para la entidad Usuario.
 * Expone los endpoints HTTP para que el frontend pueda consumir
 * los servicios de usuarios via JSON.
 * Se conecta con UsuarioService para ejecutar la lógica de negocio.
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    /**
     * Retorna la lista de todos los usuarios.
     * @return lista de usuarios en formato JSON.
     */
    @GetMapping
    public ResponseEntity<?> getAll() {
        try {
            List<Usuario> usuarios = usuarioService.listarUsuarios();
            return ResponseEntity.status(HttpStatus.OK).body(usuarios);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\":\"Error al obtener los usuarios.\"}");
        }
    }

    /**
     * Retorna la lista de usuarios como DTOs para el frontend.
     * @return lista de UsuarioDTO en formato JSON.
     */
    @GetMapping("/dto")
    public ResponseEntity<?> getAllDTO() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(usuarioService.listarDTO());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\":\"Error al obtener los usuarios como DTO.\"}");
        }
    }

    /**
     * Retorna un usuario por su id como DTO.
     * Construye manualmente el DTO para evitar referencias circulares en la serialización JSON.
     * @param id el id del usuario.
     * @return el UsuarioDTO encontrado o error 404 si no existe.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getOne(@PathVariable Integer id) {
        try {
            return usuarioService.buscarPorId(id)
                    .map(u -> {
                        UsuarioDTO dto = new UsuarioDTO();
                        dto.setIdUsuario(u.getIdUsuario());
                        dto.setNumeroUsuario(u.getNumeroUsuario());
                        dto.setCorreoElectronicoUsuario(u.getCorreoElectronicoUsuario());
                        if (u.getPersona() != null) {
                            dto.setIdPersona(u.getPersona().getIdPersona());
                            dto.setNombrePersona(u.getPersona().getNombrePersona());
                            dto.setApellidoPersona(u.getPersona().getApellidoPersona());
                        }
                        return ResponseEntity.status(HttpStatus.OK).body((Object) dto);
                    })
                    .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body("{\"error\":\"Usuario no encontrado.\"}"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\":\"Error al buscar el usuario.\"}");
        }
    }

    /**
     * Retorna un usuario por su correo electrónico como DTO.
     * @param correo el correo electrónico del usuario.
     * @return el UsuarioDTO encontrado o error 404 si no existe.
     */
    @GetMapping("/correo/{correo}")
    public ResponseEntity<?> getByCorreo(@PathVariable String correo) {
        try {
            return usuarioService.buscarPorCorreo(correo)
                    .map(u -> {
                        UsuarioDTO dto = new UsuarioDTO();
                        dto.setIdUsuario(u.getIdUsuario());
                        dto.setNumeroUsuario(u.getNumeroUsuario());
                        dto.setCorreoElectronicoUsuario(u.getCorreoElectronicoUsuario());
                        if (u.getPersona() != null) {
                            dto.setIdPersona(u.getPersona().getIdPersona());
                            dto.setNombrePersona(u.getPersona().getNombrePersona());
                            dto.setApellidoPersona(u.getPersona().getApellidoPersona());
                        }
                        return ResponseEntity.status(HttpStatus.OK).body((Object) dto);
                    })
                    .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body("{\"error\":\"Usuario no encontrado.\"}"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\":\"Error al buscar el usuario por correo.\"}");
        }
    }

    /**
     * Crea un nuevo usuario.
     * Valida los datos recibidos antes de persistirlos.
     * @param usuario el usuario a crear recibido en el body.
     * @return el usuario creado con su id generado.
     */
    @PostMapping
    public ResponseEntity<?> save(@Valid @RequestBody Usuario usuario) {
        try {
            Usuario guardado = usuarioService.guardar(usuario);
            return ResponseEntity.status(HttpStatus.CREATED).body(guardado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"error\":\"Error al crear el usuario.\"}");
        }
    }

    /**
     * Actualiza un usuario existente.
     * Valida los datos recibidos antes de persistirlos.
     * @param id el id del usuario a actualizar.
     * @param usuario los nuevos datos del usuario recibidos en el body.
     * @return el usuario actualizado.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @Valid @RequestBody Usuario usuario) {
        try {
            usuario.setIdUsuario(id);
            Usuario actualizado = usuarioService.guardar(usuario);
            return ResponseEntity.status(HttpStatus.OK).body(actualizado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"error\":\"Error al actualizar el usuario.\"}");
        }
    }

    /**
     * Elimina un usuario por su id.
     * @param id el id del usuario a eliminar.
     * @return respuesta sin contenido si se eliminó correctamente.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        try {
            usuarioService.eliminar(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"error\":\"Error al eliminar el usuario.\"}");
        }
    }

    /**
     * Captura los errores de validación cuando @Valid rechaza los datos recibidos en el body.
     * Devuelve la lista de mensajes de error al frontend en formato JSON.
     * @param excepcion la excepción lanzada por Spring al fallar la validación de Usuario.
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