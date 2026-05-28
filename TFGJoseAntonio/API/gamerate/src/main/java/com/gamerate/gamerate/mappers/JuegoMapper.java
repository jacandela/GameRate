package com.gamerate.gamerate.mappers;

import com.gamerate.gamerate.dto.entity.Juego.JuegoDTO;
import com.gamerate.gamerate.entity.Juego;
import com.gamerate.gamerate.entity.Plataforma;
import com.gamerate.gamerate.entity.Genero;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface JuegoMapper {

    @Mapping(target = "nombreDesarrollador", source = "desarrollador.nombre")
    @Mapping(target = "plataformas", source = "plataformas", qualifiedByName = "mapPlataformas")
    @Mapping(target = "generos", source = "generos", qualifiedByName = "mapGeneros")
    JuegoDTO toDTO(Juego juego);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "desarrollador", ignore = true)
    @Mapping(target = "plataformas", ignore = true)
    @Mapping(target = "generos", ignore = true)
    Juego toEntity(JuegoDTO dto);

    List<JuegoDTO> toDTOList(List<Juego> juegos);

    @Named("mapPlataformas")
    default List<String> mapPlataformas(Set<Plataforma> plataformas) {
        if (plataformas == null) return null;
        return plataformas.stream()
                .map(Plataforma::getNombre)
                .collect(Collectors.toList());
    }

    @Named("mapGeneros")
    default List<String> mapGeneros(Set<Genero> generos) {
        if (generos == null) return null;
        return generos.stream()
                .map(Genero::getNombre)
                .collect(Collectors.toList());
    }
}