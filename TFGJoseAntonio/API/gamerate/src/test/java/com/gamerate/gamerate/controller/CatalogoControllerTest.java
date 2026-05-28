package com.gamerate.gamerate.controller;

import com.gamerate.gamerate.service.Catalogo.ICatalogoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class CatalogoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ICatalogoService catalogoService;

    @Test
    @WithMockUser(roles = "ADMIN")
    void testObtenerGeneros() throws Exception {
        List<String> generos = Arrays.asList("RPG", "Acción", "Aventura");
        when(catalogoService.obtenerTodosLosGeneros()).thenReturn(generos);

        mockMvc.perform(get("/api/catalogo/generos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("RPG"))
                .andExpect(jsonPath("$[1]").value("Acción"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testObtenerPlataformas() throws Exception {
        List<String> plataformas = Arrays.asList("PC", "PS5", "Xbox");
        when(catalogoService.obtenerTodasLasPlataformas()).thenReturn(plataformas);

        mockMvc.perform(get("/api/catalogo/plataformas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("PC"))
                .andExpect(jsonPath("$[2]").value("Xbox"));
    }
}
