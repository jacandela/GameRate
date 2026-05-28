package com.gamerate.gamerate.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.gamerate.gamerate.dto.entity.Juego.JuegoDTO;
import com.gamerate.gamerate.entity.Juego;
import com.gamerate.gamerate.exceptions.NotFoundEntityException;
import com.gamerate.gamerate.mappers.JuegoMapper;
import com.gamerate.gamerate.repository.IJuegoRepository;
import com.gamerate.gamerate.service.Juego.JuegoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class JuegoServiceTest {

    @Mock
    private IJuegoRepository juegoRepository;

    @Mock
    private JuegoMapper juegoMapper;

    @InjectMocks
    private JuegoService juegoService;

    @Test
    void testBuscarPorId_CuandoExiste_DebeRetornarDTO() {
        Long id = 1L;
        Juego juego = new Juego();
        JuegoDTO dto = new JuegoDTO();
        when(juegoRepository.findById(id)).thenReturn(Optional.of(juego));
        when(juegoMapper.toDTO(juego)).thenReturn(dto);

        JuegoDTO resultado = juegoService.buscarPorId(id);

        assertNotNull(resultado);
        verify(juegoRepository, times(1)).findById(id); // Verificamos que se llamó al repo
    }

    @Test
    void testBuscarPorId_CuandoNoExiste_DebeLanzarExcepcion() {
        Long id = 99L;
        when(juegoRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundEntityException.class, () -> juegoService.buscarPorId(id));
    }
}