package com.gamerate.gamerate.controller;

import com.gamerate.gamerate.dto.entity.Usuario.UsuarioDTO;
import com.gamerate.gamerate.service.Usuario.IUsuarioService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private IUsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> getAll() {
        log.info("Petición GET recibida en /api/usuarios para listar todas las cuentas.");
        List<UsuarioDTO> usuarios = usuarioService.obtenerTodos();
        log.info("Se han listado con éxito {} usuarios.", usuarios.size());
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> getById(@PathVariable Long id) {
        log.info("Petición GET recibida para buscar el usuario con ID: {}", id);
        UsuarioDTO usuario = usuarioService.buscarPorId(id);
        return ResponseEntity.ok(usuario);
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<UsuarioDTO> getByUsername(@PathVariable String username) {
        log.info("Petición GET recibida para buscar el perfil del username: '{}'", username);
        UsuarioDTO usuario = usuarioService.buscarPorNombre(username);
        if (usuario == null) {
            log.warn("Búsqueda por username fallida: El alias '{}' no existe.", username);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(usuario);
    }

    @PostMapping("/registro")
    public ResponseEntity<UsuarioDTO> registrar(@RequestBody UsuarioDTO usuarioDTO, @RequestParam String password) {
        log.info("Petición POST recibida en /api/usuarios/registro para crear el usuario: '{}'", usuarioDTO.getNombreUsuario());

        UsuarioDTO nuevoUsuario = usuarioService.registrar(usuarioDTO, password);

        log.info("Usuario '{}' dado de alta correctamente en el sistema con ID: {}", nuevoUsuario.getNombreUsuario(), nuevoUsuario.getId());
        return new ResponseEntity<>(nuevoUsuario, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
        log.info("Petición DELETE recibida en /api/usuarios/{} (Acceso restringido a Administradores)", id);
        usuarioService.eliminarUsuario(id);
        return ResponseEntity.noContent().build(); // Devuelve 204
    }

    @PutMapping("/MiPerfil")
    public ResponseEntity<UsuarioDTO> actualizarMiPerfil(@RequestBody UsuarioDTO usuarioDTO, java.security.Principal principal) {
        String usernameAutenticado = principal.getName();
        log.info("Petición PUT recibida en /api/usuarios/MiPerfil por el usuario: '{}'", usernameAutenticado);

        UsuarioDTO perfilActualizado = usuarioService.actualizarPerfil(usernameAutenticado, usuarioDTO);

        return ResponseEntity.ok(perfilActualizado); // Devuelve 200 + el DTO actualizado
    }

}