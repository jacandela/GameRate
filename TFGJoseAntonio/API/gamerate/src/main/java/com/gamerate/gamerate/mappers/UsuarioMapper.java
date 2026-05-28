package com.gamerate.gamerate.mappers;

import com.gamerate.gamerate.dto.entity.Usuario.UsuarioDTO;
import com.gamerate.gamerate.entity.Usuario;
import com.gamerate.gamerate.enums.Rol;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    @Mapping(target = "rol", source = "rol")
    UsuarioDTO toDTO(Usuario usuario);

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "fechaCreacion", ignore = true)
    Usuario toEntity(UsuarioDTO usuarioDTO);

    List<UsuarioDTO> toDTOList(List<Usuario> usuarios);

    default String mapRol(Enum<?> rol) {
        if (rol == null) return null;
        return rol.name();
    }

    default Rol mapStringToRol(String rol) {
        if (rol == null) return Rol.USER;
        try {
            return Rol.valueOf(rol);
        } catch (IllegalArgumentException e) {
            return Rol.USER;
        }
    }
}