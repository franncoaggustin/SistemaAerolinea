package com.aerolinea.SistemaAerolinea.service;

import com.aerolinea.SistemaAerolinea.dto.ConsultaDTO;
import com.aerolinea.SistemaAerolinea.model.Consulta;
import com.aerolinea.SistemaAerolinea.repository.ConsultaRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Servicio para la gestión de consultas.
 * Contiene la lógica de negocio y conversión a DTO.
 * Se conecta con ConsultaRepository para acceder a la base de datos.
 */
@Service
public class ConsultaService {

    @Autowired
    private ConsultaRepository consultaRepository;

    private final ModelMapper modelMapper = new ModelMapper();

    /**
     * Retorna la lista de todas las consultas registradas.
     * @return lista de consultas.
     */
    public List<Consulta> listarConsultas() {
        return consultaRepository.findAll();
    }

    /**
     * Retorna la lista de consultas como DTOs para el frontend.
     * Incluye el id del usuario y del vuelo asociados.
     * @return lista de ConsultaDTO.
     */
    public List<ConsultaDTO> listarDTO() {
        return consultaRepository.findAll().stream()
                .map(c -> {
                    ConsultaDTO dto = modelMapper.map(c, ConsultaDTO.class);
                    if (c.getUsuario() != null) dto.setIdUsuario(c.getUsuario().getIdUsuario());
                    if (c.getVuelo() != null) dto.setIdVuelo(c.getVuelo().getIdVuelo());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    /**
     * Busca una consulta por su id.
     * @param id el id de la consulta.
     * @return la consulta encontrada o vacío si no existe.
     */
    public Optional<Consulta> buscarPorId(Integer id) {
        return consultaRepository.findById(id);
    }

    /**
     * Busca todas las consultas realizadas por un usuario.
     * @param idUsuario el id del usuario.
     * @return lista de consultas del usuario.
     */
    public List<Consulta> buscarPorUsuario(Integer idUsuario) {
        return consultaRepository.findByUsuarioIdUsuario(idUsuario);
    }

    /**
     * Guarda una nueva consulta o actualiza una existente.
     * @param consulta la consulta a guardar.
     * @return la consulta guardada con su id generado.
     */
    public Consulta guardar(Consulta consulta) {
        return consultaRepository.save(consulta);
    }

    /**
     * Elimina una consulta por su id.
     * @param id el id de la consulta a eliminar.
     */
    public void eliminar(Integer id) {
        consultaRepository.deleteById(id);
    }
}