package com.gamerate.gamerate.service.Juego;

import com.gamerate.gamerate.dto.entity.Juego.JuegoDTO;
import com.gamerate.gamerate.dto.entity.Juego.JuegoStatsDTO;
import java.util.List;

public interface IJuegoService {
    List<JuegoDTO> obtenerTodos();
    List<JuegoDTO> obtenerTodosFiltrados(String titulo, String desarrollador);
    JuegoStatsDTO obtenerEstadisticas(Long juegoId);
    JuegoDTO buscarPorId(Long id);
    List<JuegoDTO> buscarPorTitulo(String titulo);
    List<JuegoDTO> buscarPorDesarrollador(String nombre);

    JuegoDTO guardar(JuegoDTO juegoDTO);
    void eliminar(Long id);

    JuegoDTO actualizar(Long id, JuegoDTO juegoDTO);

}
