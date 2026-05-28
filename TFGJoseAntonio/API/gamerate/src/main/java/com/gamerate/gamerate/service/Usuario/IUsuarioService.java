package com.gamerate.gamerate.service.Usuario;

import com.gamerate.gamerate.dto.entity.Usuario.UsuarioDTO;
import com.gamerate.gamerate.entity.Usuario;

import java.util.List;

public interface IUsuarioService {
    List<UsuarioDTO> obtenerTodos();
    UsuarioDTO buscarPorNombre(String nombreUsuario);
    UsuarioDTO buscarPorId(Long id);
    UsuarioDTO registrar(UsuarioDTO usuarioDTO, String password);
    void eliminarUsuario(Long id);
    UsuarioDTO actualizarPerfil(String nombreUsuario, UsuarioDTO usuarioDTO);
}
