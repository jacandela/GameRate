package com.gamerate.gamerate.service.Review;

import com.gamerate.gamerate.dto.entity.Review.ReviewDTO;
import com.gamerate.gamerate.entity.Review;

import java.util.List;

public interface IReviewService {
    ReviewDTO crearReview(ReviewDTO dto, String nombreUsuario);
    List<ReviewDTO> obtenerPorJuego(Long gameId);
    void eliminarReview(Long id, String nombreUsuario);
    List<ReviewDTO> obtenerMisReviews(String nombreUsuario);
    ReviewDTO actualizarReview(Long id, ReviewDTO dto, String nombreUsuario);

}
