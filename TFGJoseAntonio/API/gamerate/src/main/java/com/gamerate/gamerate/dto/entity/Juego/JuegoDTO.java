package com.gamerate.gamerate.dto.entity.Juego;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class JuegoDTO {
    private Long id;

    @NotBlank(message = "El título es obligatorio")
    private String titulo;

    @Size(max = 500, message = "La descripción es demasiado larga")
    private String descripcion;

    private String imagenUrl;

    private String nombreDesarrollador;

    private List<String> plataformas;
    private List<String> generos;
}
