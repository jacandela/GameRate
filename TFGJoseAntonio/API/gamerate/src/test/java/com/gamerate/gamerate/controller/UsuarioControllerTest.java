package com.gamerate.gamerate.controller;

import com.gamerate.gamerate.entity.Usuario;
import com.gamerate.gamerate.repository.IUsuarioRepository;
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
@Transactional
public class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private IUsuarioRepository usuarioRepository; // Inyecta el repositorio

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetAllUsuarios() throws Exception {
        mockMvc.perform(get("/api/usuarios"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetById() throws Exception {
        mockMvc.perform(get("/api/usuarios/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetByUsername() throws Exception {
        mockMvc.perform(get("/api/usuarios/username/usuario1"))
                .andExpect(status().isOk());
    }

    @Test
    void testRegistrarUsuario() throws Exception {
        String uniqueName = "user_" + java.util.UUID.randomUUID().toString().substring(0, 8);
        String json = "{\"nombreUsuario\":\"" + uniqueName + "\", \"email\":\"" + uniqueName + "@test.com\"}";

        mockMvc.perform(post("/api/usuarios/registro")
                        .param("password", "123456") // <--- Aquí el parámetro
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(username = "usuario1", roles = "USER")
    void testActualizarMiPerfil() throws Exception {
        String json = "{\"nombreUsuario\":\"miUsuario\", \"email\":\"nuevo@email.com\"}";
        mockMvc.perform(put("/api/usuarios/MiPerfil")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testEliminarUsuarioComoAdmin() throws Exception {
        // 1. Preparamos el usuario que vamos a eliminar
        Usuario user = new Usuario();
        user.setNombreUsuario("usuarioParaBorrar");
        user.setEmail("borrar@test.com");
        user.setPassword("12345");
        Usuario guardado = usuarioRepository.save(user);

        // 2. Eliminamos usando el ID real que acabamos de crear
        mockMvc.perform(delete("/api/usuarios/" + guardado.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "USER")
    void testEliminarUsuarioComoUser_DeberiaFallar() throws Exception {
        mockMvc.perform(delete("/api/usuarios/1"))
                .andExpect(status().isForbidden());
    }
}