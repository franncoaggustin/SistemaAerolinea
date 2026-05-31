package com.aerolinea.SistemaAerolinea.service;

import com.aerolinea.SistemaAerolinea.dto.UsuarioDTO;
import com.aerolinea.SistemaAerolinea.model.Usuario;
import com.aerolinea.SistemaAerolinea.repository.UsuarioRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Servicio para la gestión de usuarios.
 * Contiene la lógica de negocio y conversión a DTO.
 * Se conecta con UsuarioRepository para acceder a la base de datos.
 */
@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    private final ModelMapper modelMapper = new ModelMapper();

    /**
     * Retorna la lista de todos los usuarios registrados.
     * @return lista de usuarios.
     */
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    /**
     * Retorna la lista de usuarios como DTOs para el frontend.
     * Incluye el id, nombre y apellido de la persona asociada.
     * No incluye la contraseña por seguridad.
     * @return lista de UsuarioDTO.
     */
    public List<UsuarioDTO> listarDTO() {
        return usuarioRepository.findAll().stream()
                .map(u -> {
                    UsuarioDTO dto = modelMapper.map(u, UsuarioDTO.class);
                    if (u.getPersona() != null) {
                        dto.setIdPersona(u.getPersona().getIdPersona());
                        dto.setNombrePersona(u.getPersona().getNombrePersona());
                        dto.setApellidoPersona(u.getPersona().getApellidoPersona());
                    }
                    return dto;
                })
                .collect(Collectors.toList());
    }

    /**
     * Busca un usuario por su id.
     * @param id el id del usuario.
     * @return el usuario encontrado o vacío si no existe.
     */
    public Optional<Usuario> buscarPorId(Integer id) {
        return usuarioRepository.findById(id);
    }

    /**
     * Busca un usuario por su correo electrónico.
     * @param correo el correo electrónico del usuario.
     * @return el usuario encontrado o vacío si no existe.
     */
    public Optional<Usuario> buscarPorCorreo(String correo) {
        return usuarioRepository.findByCorreoElectronicoUsuario(correo);
    }

    /**
     * Guarda un nuevo usuario o actualiza uno existente.
     * @param usuario el usuario a guardar.
     * @return el usuario guardado con su id generado.
     */
    public Usuario guardar(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    /**
     * Elimina un usuario por su id.
     * @param id el id del usuario a eliminar.
     */
    public void eliminar(Integer id) {
        usuarioRepository.deleteById(id);
    }
}