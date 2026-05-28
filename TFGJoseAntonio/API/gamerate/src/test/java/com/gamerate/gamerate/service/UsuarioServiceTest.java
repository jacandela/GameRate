package com.gamerate.gamerate.service;

import com.gamerate.gamerate.dto.entity.Usuario.UsuarioDTO;
import com.gamerate.gamerate.entity.Usuario;
import com.gamerate.gamerate.exceptions.DuplicateEntityException;
import com.gamerate.gamerate.mappers.UsuarioMapper;
import com.gamerate.gamerate.repository.IUsuarioRepository;
import com.gamerate.gamerate.service.Usuario.UsuarioService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {

    @Mock private IUsuarioRepository usuarioRepository;
    @Mock private UsuarioMapper usuarioMapper;
    @Mock private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioService usuarioService;

    @Test
    void testRegistrar_CuandoNombreYaExiste_LanzaExcepcion() {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setNombreUsuario("usuarioExistente");

        when(usuarioRepository.findByNombreUsuario("usuarioExistente"))
                .thenReturn(Optional.of(new Usuario()));

        assertThrows(DuplicateEntityException.class, () -> usuarioService.registrar(dto, "password123"));
    }

    @Test
    void testRegistrar_CuandoEsValido_EncriptaYGuarda() {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setNombreUsuario("nuevoUsuario");
        Usuario usuarioEntidad = new Usuario();

        when(usuarioRepository.findByNombreUsuario("nuevoUsuario")).thenReturn(Optional.empty());
        when(usuarioMapper.toEntity(dto)).thenReturn(usuarioEntidad);
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioEntidad);

        usuarioService.registrar(dto, "password123");

        verify(passwordEncoder, times(1)).encode("password123");
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }
}
