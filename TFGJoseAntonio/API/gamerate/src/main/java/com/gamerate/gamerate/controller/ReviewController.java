package com.gamerate.gamerate.controller;

import com.gamerate.gamerate.dto.entity.Review.ReviewDTO;
import com.gamerate.gamerate.service.Review.IReviewService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    @Autowired
    private IReviewService reviewService;

    @PostMapping
    public ResponseEntity<ReviewDTO> crearReview(@Valid @RequestBody ReviewDTO reviewDTO, Principal principal) {
        log.info("Petición POST en /api/reviews -> El usuario '{}' intenta valorar el juego ID: {}",
                principal.getName(), reviewDTO.getGameId());

        ReviewDTO nuevaReview = reviewService.crearReview(reviewDTO, principal.getName());

        log.info("Reseña creada con éxito. ID generado: {}", nuevaReview.getId());
        return new ResponseEntity<>(nuevaReview, HttpStatus.CREATED);
    }

    @GetMapping("/game/{gameId}")
    public ResponseEntity<List<ReviewDTO>> getReviewsByGame(@PathVariable Long gameId) {
        log.info("Petición GET en /api/reviews/game/{} para listar reseñas del juego.", gameId);
        List<ReviewDTO> reviews = reviewService.obtenerPorJuego(gameId);
        log.info("Reseñas recuperadas para el juego ID {}: {}", gameId, reviews.size());
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/MisReviews")
    public ResponseEntity<List<ReviewDTO>> listarMisReviews(Principal principal) {
        log.info("Petición GET en /api/reviews/MisReviews solicitada por el usuario: '{}'", principal.getName());
        List<ReviewDTO> reviews = reviewService.obtenerMisReviews(principal.getName());
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id, Principal principal) {
        log.info("Petición DELETE en /api/reviews/{} solicitada por el usuario: '{}'", id, principal.getName());
        reviewService.eliminarReview(id, principal.getName());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReviewDTO> actualizarReview(
            @PathVariable Long id,
            @Valid @RequestBody ReviewDTO reviewDTO,
            Principal principal) {
        log.info("Petición PUT en /api/reviews/{} para actualizar reseña. Usuario: '{}'", id, principal.getName());
        ReviewDTO reviewActualizada = reviewService.actualizarReview(id, reviewDTO, principal.getName());
        return new ResponseEntity<>(reviewActualizada, HttpStatus.OK);
    }
}
