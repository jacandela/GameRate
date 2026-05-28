package com.gamerate.gamerate.controller;

import com.gamerate.gamerate.entity.Usuario;
import com.gamerate.gamerate.repository.IUsuarioRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional // <--- Esto garantiza que cualquier cambio se deshaga al terminar
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private IUsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void testLoginExitoso() throws Exception {
        // 1. Generamos nombre único
        String uniqueUsername = "user_" + UUID.randomUUID().toString().substring(0, 8);

        Usuario user = new Usuario();
        user.setNombreUsuario(uniqueUsername);
        user.setEmail(uniqueUsername + "@gamerate.com");
        user.setPassword(passwordEncoder.encode("password123"));
        usuarioRepository.save(user);

        // 2. Login con ese mismo nombre
        String loginJson = "{\"username\":\"" + uniqueUsername + "\", \"password\":\"password123\"}";

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                // 3. Comparamos contra la variable, no contra un texto fijo
                .andExpect(jsonPath("$.username").value(uniqueUsername));
    }

    @Test
    void testLoginFallido() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"usuarioQueNoExiste\", \"password\":\"1234\"}"))
                .andExpect(status().isUnauthorized());
    }
}