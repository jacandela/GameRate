package com.gamerate.gamerate.service.Juego;

import com.gamerate.gamerate.dto.entity.Juego.JuegoDTO;
import com.gamerate.gamerate.dto.entity.Juego.JuegoStatsDTO;
import com.gamerate.gamerate.entity.*;
import com.gamerate.gamerate.exceptions.NotFoundEntityException;
import com.gamerate.gamerate.repository.*;
import com.gamerate.gamerate.mappers.JuegoMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@Service
public class JuegoService implements IJuegoService {

    @Autowired private IJuegoRepository juegoRepository;
    @Autowired private JuegoMapper juegoMapper;
    @Autowired private IReviewRepository reviewRepository;
    @Autowired private IDesarrolladorRepository desarrolladorRepository;
    @Autowired private IPlataformaRepository plataformaRepository;
    @Autowired private IGeneroRepository generoRepository;

    @Override
    public List<JuegoDTO> obtenerTodos() {
        log.debug("Cargando todos los juegos.");
        Iterable<Juego> juegosIterable = juegoRepository.findAll();
        List<Juego> juegosList = StreamSupport
                .stream(juegosIterable.spliterator(), false)
                .collect(Collectors.toList());
        return juegoMapper.toDTOList(juegosList);
    }

    @Override
    public List<JuegoDTO> obtenerTodosFiltrados(String titulo, String desarrollador) {
        String filtroTitulo = (titulo == null) ? "" : titulo;
        String filtroDesarrollador = (desarrollador == null) ? "" : desarrollador;
        List<Juego> juegos = juegoRepository.findByTituloContainingIgnoreCaseAndDesarrolladorNombreContainingIgnoreCase(
                filtroTitulo, filtroDesarrollador);
        return juegoMapper.toDTOList(juegos);
    }

    @Override
    public JuegoStatsDTO obtenerEstadisticas(Long juegoId) {
        if (!juegoRepository.existsById(juegoId)) {
            throw new NotFoundEntityException("Juego con ID " + juegoId + " no existe.");
        }
        return reviewRepository.obtenerStatsDeJuego(juegoId);
    }

    @Override
    public JuegoDTO buscarPorId(Long id) {
        return juegoRepository.findById(id)
                .map(juegoMapper::toDTO)
                .orElseThrow(() -> new NotFoundEntityException("Juego con ID " + id + " no encontrado."));
    }

    @Override
    public List<JuegoDTO> buscarPorTitulo(String titulo) {
        return juegoMapper.toDTOList(juegoRepository.findByTituloContainingIgnoreCase(titulo));
    }

    @Override
    public List<JuegoDTO> buscarPorDesarrollador(String nombre) {
        List<Juego> juegos = juegoRepository.buscarPorNombreDesarrollador(nombre);
        if (juegos.isEmpty()) throw new NotFoundEntityException("Sin juegos para: " + nombre);
        return juegoMapper.toDTOList(juegos);
    }

    @Override
    @Transactional
    public JuegoDTO guardar(JuegoDTO dto) {
        Juego juego = juegoMapper.toEntity(dto);
        sincronizarRelaciones(juego, dto);
        return juegoMapper.toDTO(juegoRepository.save(juego));
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        if (!juegoRepository.existsById(id)) {
            throw new NotFoundEntityException("No se puede eliminar: ID " + id + " no existe.");
        }
        reviewRepository.deleteByJuegoId(id);
        juegoRepository.deleteById(id);
        log.info("Juego ID {} y sus reseñas eliminados.", id);
    }

    @Override
    @Transactional
    public JuegoDTO actualizar(Long id, JuegoDTO dto) {
        return juegoRepository.findById(id).map(juego -> {
            juego.setTitulo(dto.getTitulo());
            juego.setDescripcion(dto.getDescripcion());
            juego.setImagenUrl(dto.getImagenUrl());
            sincronizarRelaciones(juego, dto);
            return juegoMapper.toDTO(juegoRepository.save(juego));
        }).orElseThrow(() -> new NotFoundEntityException("Juego no encontrado para actualizar."));
    }

    private void sincronizarRelaciones(Juego juego, JuegoDTO dto) {
        if (dto.getNombreDesarrollador() != null) {
            juego.setDesarrollador(desarrolladorRepository.findByNombre(dto.getNombreDesarrollador())
                    .orElseGet(() -> {
                        Desarrollador nuevo = new Desarrollador();
                        nuevo.setNombre(dto.getNombreDesarrollador());
                        return desarrolladorRepository.save(nuevo);
                    }));
        }

        if (dto.getGeneros() != null) {
            juego.setGeneros(dto.getGeneros().stream()
                    .map(nombre -> generoRepository.findByNombre(nombre)
                            .orElseGet(() -> {
                                Genero nuevo = new Genero();
                                nuevo.setNombre(nombre);
                                return generoRepository.save(nuevo);
                            }))
                    .collect(Collectors.toSet()));
        }

        if (dto.getPlataformas() != null) {
            juego.setPlataformas(dto.getPlataformas().stream()
                    .map(nombre -> plataformaRepository.findByNombre(nombre)
                            .orElseGet(() -> {
                                Plataforma nuevo = new Plataforma();
                                nuevo.setNombre(nombre);
                                return plataformaRepository.save(nuevo);
                            }))
                    .collect(Collectors.toSet()));
        }
    }
}