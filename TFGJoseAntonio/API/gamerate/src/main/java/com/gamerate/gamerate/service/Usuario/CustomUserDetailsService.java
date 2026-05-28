package com.gamerate.gamerate.service.Usuario;

import com.gamerate.gamerate.entity.Usuario;
import com.gamerate.gamerate.repository.IUsuarioRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private IUsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("[Spring Security] Intentando cargar detalles de autenticación para el usuario: '{}'", username);

        Usuario usuario = usuarioRepository.findByNombreUsuario(username)
                .orElseThrow(() -> {
                    log.error("[Spring Security] Autenticación denegada: El usuario '{}' no existe en MariaDB.", username);
                    return new UsernameNotFoundException("Usuario no encontrado en la DB: " + username);
                });

        log.info("[Spring Security] Usuario '{}' localizado. Extrayendo rol criptográfico: '{}' para el contexto de sesión.",
                username, usuario.getRol().name());

        return User.builder()
                .username(usuario.getNombreUsuario())
                .password(usuario.getPassword())
                .roles(usuario.getRol().name())
                .build();
    }
}