package com.gamerate.gamerate.repository;

import com.gamerate.gamerate.dto.entity.Juego.JuegoStatsDTO;
import com.gamerate.gamerate.entity.Review;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IReviewRepository extends CrudRepository<Review, Long> {
    List<Review> findByJuegoId(Long gameId);
    List<Review> findByUsuarioId(Long usuarioId);

    boolean existsByUsuarioIdAndJuegoId(Long userId, Long gameId);

    @Query("SELECT new com.gamerate.gamerate.dto.entity.Juego.JuegoStatsDTO(AVG(r.score), COUNT(r)) " +
            "FROM Review r WHERE r.juego.id = :juegoId")
    JuegoStatsDTO obtenerStatsDeJuego(@Param("juegoId") Long juegoId);

    void deleteByJuegoId(Long juegoId);

}
