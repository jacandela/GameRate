package com.gamerate.gamerate.service;

import com.gamerate.gamerate.entity.Genero;
import com.gamerate.gamerate.entity.Plataforma;
import com.gamerate.gamerate.repository.IGeneroRepository;
import com.gamerate.gamerate.repository.IPlataformaRepository;
import com.gamerate.gamerate.service.Catalogo.CatalogoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CatalogoServiceTest {

    @Mock
    private IGeneroRepository generoRepository;

    @Mock
    private IPlataformaRepository plataformaRepository;

    @InjectMocks
    private CatalogoService catalogoService;

    @Test
    void testObtenerTodosLosGeneros() {
        Genero g1 = new Genero();
        g1.setNombre("RPG");
        Genero g2 = new Genero();
        g2.setNombre("Accion");
        when(generoRepository.findAll()).thenReturn(List.of(g1, g2));

        List<String> resultados = catalogoService.obtenerTodosLosGeneros();

        assertEquals(2, resultados.size());
        assertTrue(resultados.contains("RPG"));
        assertTrue(resultados.contains("Accion"));
        verify(generoRepository, times(1)).findAll();
    }

    @Test
    void testObtenerTodasLasPlataformas() {
        Plataforma p1 = new Plataforma();
        p1.setNombre("PC");
        when(plataformaRepository.findAll()).thenReturn(List.of(p1));

        List<String> resultados = catalogoService.obtenerTodasLasPlataformas();

        assertEquals(1, resultados.size());
        assertEquals("PC", resultados.get(0));
        verify(plataformaRepository, times(1)).findAll();
    }
}
