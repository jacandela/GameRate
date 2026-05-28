package com.gamerate.gamerate.dto.entity.Usuario;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UsuarioDTO {
    private Long id;
    private String nombreUsuario;
    private String email;
    private String rol;
    private LocalDateTime fechaCreacion;
}
