package com.gamerate.gamerate.controller;

import com.gamerate.gamerate.dto.entity.Juego.JuegoDTO;
import com.gamerate.gamerate.dto.entity.Juego.JuegoStatsDTO;
import com.gamerate.gamerate.service.Juego.IJuegoService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/juegos")
public class JuegoController {

    @Autowired
    private IJuegoService juegoService;

    @GetMapping
    public ResponseEntity<List<JuegoDTO>> getAll(
            @RequestParam(required = false) String titulo,
            @RequestParam(required = false) String desarrollador
    ) {
        log.info("Petición GET recibida en /api/juegos (Lista completa). Filtros -> Título: '{}', Desarrollador: '{}'", titulo, desarrollador);

        List<JuegoDTO> juegos = juegoService.obtenerTodosFiltrados(titulo, desarrollador);

        log.info("Búsqueda completada. Elementos recuperados: {}", juegos.size());
        return ResponseEntity.ok(juegos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<JuegoDTO> obtenerPorId(@PathVariable Long id) {
        log.info("Petición GET recibida para buscar juego por ID: {}", id);

        JuegoDTO juego = juegoService.buscarPorId(id);
        if (juego == null) {
            log.warn("No se encontró ningún videojuego con el ID: {}", id);
            return ResponseEntity.notFound().build();
        }

        log.info("Juego encontrado con éxito: '{}' (ID: {})", juego.getTitulo(), id);
        return ResponseEntity.ok(juego);
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<JuegoDTO>> buscarPorTitulo(@RequestParam String titulo) {
        log.info("Petición GET recibida en /api/juegos/buscar para el título: '{}'", titulo);
        List<JuegoDTO> juegos = juegoService.buscarPorTitulo(titulo);
        log.info("Búsqueda por título completada. Coincidencias encontradas: {}", juegos.size());
        return ResponseEntity.ok(juegos);
    }

    @GetMapping("/BuscarPorDesarrollador/{nombre}")
    public ResponseEntity<List<JuegoDTO>> buscarPorDesarrollador(@PathVariable String nombre) {
        log.info("Petición GET recibida para buscar juegos del desarrollador: '{}'", nombre);
        List<JuegoDTO> juegos = juegoService.buscarPorDesarrollador(nombre);
        log.info("Búsqueda por desarrollador completada. Juegos encontrados: {}", juegos.size());
        return ResponseEntity.ok(juegos);
    }

    @GetMapping("/{id}/stats")
    public ResponseEntity<JuegoStatsDTO> obtenerEstadisticas(@PathVariable Long id) {
        log.info("Petición GET recibida para obtener estadísticas del juego con ID: {}", id);
        JuegoStatsDTO stats = juegoService.obtenerEstadisticas(id);
        return ResponseEntity.ok(stats);
    }


    @PostMapping
    public ResponseEntity<JuegoDTO> crear(@Valid @RequestBody JuegoDTO dto) {
        log.info("Petición POST recibida para registrar un nuevo juego: '{}'", dto.getTitulo());
        JuegoDTO nuevoJuego = juegoService.guardar(dto);
        log.info("Videojuego creado correctamente con ID asignado: {}", nuevoJuego.getId());
        return new ResponseEntity<>(nuevoJuego, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.info("Petición DELETE recibida para eliminar el juego con ID: {}", id);
        juegoService.eliminar(id);
        log.info("Proceso de eliminación finalizado para el juego con ID: {}", id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<JuegoDTO> actualizar(@PathVariable Long id, @Valid @RequestBody JuegoDTO dto) {
        log.info("Petición PUT recibida para actualizar el juego ID: {}", id);
        JuegoDTO juegoActualizado = juegoService.actualizar(id, dto);
        log.info("Videojuego ID {} actualizado correctamente.", id);
        return ResponseEntity.ok(juegoActualizado);
    }
}
