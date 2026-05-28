package com.gamerate.gamerate.service.Usuario;

import com.gamerate.gamerate.dto.entity.Usuario.UsuarioDTO;
import com.gamerate.gamerate.entity.Usuario;
import com.gamerate.gamerate.enums.Rol;
import com.gamerate.gamerate.exceptions.DuplicateEntityException;
import com.gamerate.gamerate.exceptions.NotFoundEntityException;
import com.gamerate.gamerate.repository.IUsuarioRepository;
import com.gamerate.gamerate.mappers.UsuarioMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class UsuarioService implements IUsuarioService {

    @Autowired
    private IUsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioMapper usuarioMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<UsuarioDTO> obtenerTodos() {
        log.debug("Extrayendo todos los usuarios desde la base de datos.");
        Iterable<Usuario> usuarios = usuarioRepository.findAll();
        List<Usuario> lista = new ArrayList<>();
        usuarios.forEach(lista::add);
        return usuarioMapper.toDTOList(lista);
    }

    @Override
    public UsuarioDTO buscarPorNombre(String nombreUsuario) {
        log.debug("Ejecutando consulta en BD para encontrar el username: '{}'", nombreUsuario);
        return usuarioRepository.findByNombreUsuario(nombreUsuario)
                .map(usuario -> {
                    log.info("Usuario '{}' localizado con éxito por coincidencia de nombre.", nombreUsuario);
                    return usuarioMapper.toDTO(usuario);
                })
                .orElse(null);
    }

    @Override
    public UsuarioDTO buscarPorId(Long id) {
        log.info("Buscando usuario por ID: {}", id);

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Fallo de consulta: El usuario con ID {} no existe.", id);
                    return new NotFoundEntityException("Usuario no encontrado con ID: " + id);
                });

        return usuarioMapper.toDTO(usuario);
    }

    @Override
    @Transactional
    public UsuarioDTO registrar(UsuarioDTO usuarioDTO, String password) {
        log.info("Validando credenciales únicas para el registro de: '{}'", usuarioDTO.getNombreUsuario());

        if (usuarioRepository.findByNombreUsuario(usuarioDTO.getNombreUsuario()).isPresent()) {
            log.warn("Registro rechazado: El nombre de usuario '{}' ya se encuentra duplicado en BD.", usuarioDTO.getNombreUsuario());
            throw new DuplicateEntityException("El nombre de usuario ya está registrado");
        }

        Usuario usuario = usuarioMapper.toEntity(usuarioDTO);

        log.debug("Encriptando contraseña con PasswordEncoder para el usuario '{}'", usuarioDTO.getNombreUsuario());
        usuario.setPassword(passwordEncoder.encode(password));

        usuario.setRol(Rol.USER);
        usuario.setFechaCreacion(LocalDateTime.now());

        Usuario usuarioGuardado = usuarioRepository.save(usuario);
        log.info("Persistencia completada: Cuenta asignada al ID {} con rol inicial {}", usuarioGuardado.getId(), usuarioGuardado.getRol());

        return usuarioMapper.toDTO(usuarioGuardado);
    }

    @Override
    @Transactional
    public void eliminarUsuario(Long id) {
        log.info("Comprobando existencia del usuario ID {} para su eliminación por parte del Administrador.", id);

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Intento de borrado fallido: El usuario con ID {} no existe.", id);
                    return new NotFoundEntityException("No se puede eliminar: El usuario no existe.");
                });

        usuarioRepository.delete(usuario);
        log.info("El usuario '{}' (ID: {}) ha sido eliminado permanentemente del sistema.", usuario.getNombreUsuario(), id);
    }

    @Override
    @Transactional
    public UsuarioDTO actualizarPerfil(String usernameActante, UsuarioDTO dto) {
        log.info("Iniciando actualización de perfil para: '{}'", usernameActante);

        Usuario usuario = usuarioRepository.findByNombreUsuario(usernameActante)
                .orElseThrow(() -> new NotFoundEntityException("Usuario no encontrado: " + usernameActante));

        String nuevoNombre = dto.getNombreUsuario();
        if (nuevoNombre != null && !nuevoNombre.equals(usuario.getNombreUsuario())) {
            if (usuarioRepository.findByNombreUsuario(nuevoNombre).isPresent()) {
                log.warn("Actualización denegada: El alias '{}' ya está en uso.", nuevoNombre);
                throw new DuplicateEntityException("El nombre de usuario '" + nuevoNombre + "' ya está registrado.");
            }
            usuario.setNombreUsuario(nuevoNombre);
        }

        if (dto.getEmail() != null && !dto.getEmail().isEmpty()) {
            usuario.setEmail(dto.getEmail());
        }

        Usuario usuarioActualizado = usuarioRepository.save(usuario);
        log.info("Perfil actualizado con éxito para: '{}'", usuarioActualizado.getNombreUsuario());

        return usuarioMapper.toDTO(usuarioActualizado);
    }

}
