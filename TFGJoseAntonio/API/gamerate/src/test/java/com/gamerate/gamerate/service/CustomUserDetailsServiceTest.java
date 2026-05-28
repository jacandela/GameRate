package com.gamerate.gamerate.service;

import com.gamerate.gamerate.entity.Usuario;
import com.gamerate.gamerate.enums.Rol;
import com.gamerate.gamerate.repository.IUsuarioRepository;
import com.gamerate.gamerate.service.Usuario.CustomUserDetailsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomUserDetailsServiceTest {

    @Mock
    private IUsuarioRepository usuarioRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    @Test
    void testLoadUserByUsername_CuandoUsuarioExiste_RetornaUserDetails() {
        Usuario usuario = new Usuario();
        usuario.setNombreUsuario("testUser");
        usuario.setPassword("hashSeguro");
        usuario.setRol(Rol.USER);

        when(usuarioRepository.findByNombreUsuario("testUser")).thenReturn(Optional.of(usuario));

        UserDetails userDetails = customUserDetailsService.loadUserByUsername("testUser");

        assertNotNull(userDetails);
        assertEquals("testUser", userDetails.getUsername());
        assertEquals("hashSeguro", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_USER")));

        verify(usuarioRepository, times(1)).findByNombreUsuario("testUser");
    }

    @Test
    void testLoadUserByUsername_CuandoUsuarioNoExiste_LanzaExcepcion() {
        when(usuarioRepository.findByNombreUsuario("inexistente")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () ->
                customUserDetailsService.loadUserByUsername("inexistente"));
    }
}