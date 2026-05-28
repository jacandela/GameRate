package com.gamerate.gamerate.mappers;

import com.gamerate.gamerate.dto.entity.Review.ReviewDTO;
import com.gamerate.gamerate.entity.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ReviewMapper {

    @Mapping(source = "juego.id", target = "gameId")
    @Mapping(source = "usuario.id", target = "userId")
    @Mapping(source = "usuario.nombreUsuario", target = "username")
    ReviewDTO toDTO(Review entity);

    @Mapping(target = "juego", ignore = true)
    @Mapping(target = "usuario", ignore = true)
    Review toEntity(ReviewDTO dto);

    List<ReviewDTO> toDTOList(List<Review> list);
}