package com.gamerate.gamerate.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional // Protege la base de datos haciendo rollback tras cada test
public class JuegoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(roles = "USER")
    void testGetAll() throws Exception {
        mockMvc.perform(get("/api/juegos"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    void testObtenerPorId() throws Exception {
        // Asegúrate de que el ID 1 exista en tu base de datos
        mockMvc.perform(get("/api/juegos/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    void testBuscarPorTitulo() throws Exception {
        mockMvc.perform(get("/api/juegos/buscar").param("titulo", "Elden"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    void testBuscarPorDesarrollador() throws Exception {
        mockMvc.perform(get("/api/juegos/BuscarPorDesarrollador/FromSoftware"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    void testObtenerStats() throws Exception {
        mockMvc.perform(get("/api/juegos/1/stats"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testCrearJuego() throws Exception {
        String json = "{\"titulo\":\"Nuevo Juego\", \"descripcion\":\"Descripcion de prueba\", \"nombreDesarrollador\":\"TestDev\"}";
        mockMvc.perform(post("/api/juegos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testActualizarJuego() throws Exception {
        String json = "{\"titulo\":\"Juego Actualizado\", \"descripcion\":\"Nueva descripcion\"}";
        mockMvc.perform(put("/api/juegos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testEliminarJuego() throws Exception {
        mockMvc.perform(delete("/api/juegos/1"))
                .andExpect(status().isNoContent());
    }
}