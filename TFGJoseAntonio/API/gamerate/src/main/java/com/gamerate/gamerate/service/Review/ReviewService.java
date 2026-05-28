package com.gamerate.gamerate.service.Review;

import com.gamerate.gamerate.dto.entity.Review.ReviewDTO;
import com.gamerate.gamerate.entity.Juego;
import com.gamerate.gamerate.entity.Review;
import com.gamerate.gamerate.entity.Usuario;
import com.gamerate.gamerate.exceptions.DuplicateEntityException;
import com.gamerate.gamerate.exceptions.NotFoundEntityException;
import com.gamerate.gamerate.repository.IReviewRepository;
import com.gamerate.gamerate.repository.IJuegoRepository;
import com.gamerate.gamerate.repository.IUsuarioRepository;
import com.gamerate.gamerate.mappers.ReviewMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
;
import java.util.List;

@Slf4j
@Service
public class ReviewService implements IReviewService {

    @Autowired private IReviewRepository reviewRepository;
    @Autowired private IJuegoRepository juegoRepository;
    @Autowired private IUsuarioRepository usuarioRepository;
    @Autowired private ReviewMapper reviewMapper;

    @Override
    @Transactional
    public ReviewDTO crearReview(ReviewDTO dto, String nombreUsuario) {
        log.debug("Procesando negocio para crear reseña del usuario: '{}'", nombreUsuario);

        Usuario usuario = usuarioRepository.findByNombreUsuario(nombreUsuario)
                .orElseThrow(() -> {
                    log.error("Error crítico: El usuario autenticado '{}' no existe en la BD.", nombreUsuario);
                    return new NotFoundEntityException("Usuario no encontrado con nombre: " + nombreUsuario);
                });

        Long userIdReal = usuario.getId();

        if (reviewRepository.existsByUsuarioIdAndJuegoId(userIdReal, dto.getGameId())) {
            log.warn("Registro denegado: El usuario ID {} ya ha calificado el juego ID {}", userIdReal, dto.getGameId());
            throw new DuplicateEntityException("Ya has enviado una reseña para este videojuego.");
        }

        Juego juego = juegoRepository.findById(dto.getGameId())
                .orElseThrow(() -> {
                    log.warn("Registro denegado: El videojuego con ID {} no existe.", dto.getGameId());
                    return new NotFoundEntityException("Juego no encontrado con ID: " + dto.getGameId());
                });

        Review review = reviewMapper.toEntity(dto);
        review.setJuego(juego);
        review.setUsuario(usuario);

        log.info("Persistiendo nueva reseña en BD. Usuario: '{}' -> Juego: '{}' [Nota: {}]",
                nombreUsuario, juego.getTitulo(), dto.getScore());

        Review reviewGuardada = reviewRepository.save(review);
        return reviewMapper.toDTO(reviewGuardada);
    }

    @Override
    public List<ReviewDTO> obtenerPorJuego(Long gameId) {
        log.debug("Consultando reseñas del juego ID {} en base de datos.", gameId);
        List<Review> reviews = reviewRepository.findByJuegoId(gameId);
        return reviewMapper.toDTOList(reviews);
    }

    @Override
    @Transactional
    public void eliminarReview(Long id, String nombreUsuario) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Borrado denegado: La reseña con ID {} no existe.", id);
                    return new NotFoundEntityException("No se puede eliminar la reseña: ID " + id + " no existe.");
                });

        Usuario usuarioActante = usuarioRepository.findByNombreUsuario(nombreUsuario)
                .orElseThrow(() -> new NotFoundEntityException("Usuario no encontrado con nombre: " + nombreUsuario));

        boolean esElPropietario = review.getUsuario().getNombreUsuario().equals(nombreUsuario);

        log.info("Verificando permisos de borrado. Operador: '{}' | Rol en BD: '{}'", nombreUsuario, usuarioActante.getRol());

        boolean esAdmin = false;
        if (usuarioActante.getRol() != null) {
            String rolLimpio = usuarioActante.getRol().toString().toUpperCase().trim();
            esAdmin = rolLimpio.contains("ADMIN");
        }

        if (!esElPropietario && !esAdmin) {
            log.warn("ACCESO DENEGADO: El usuario '{}' sin privilegios intentó borrar la reseña ID {}", nombreUsuario, id);
            throw new IllegalArgumentException("No tienes permisos para eliminar esta reseña. Solo el autor o un administrador pueden hacerlo.");
        }

        reviewRepository.delete(review);
        log.info("Reseña con ID {} eliminada con éxito del sistema por '{}'", id, nombreUsuario);
    }

    @Override
    public List<ReviewDTO> obtenerMisReviews(String nombreUsuario) {
        Usuario usuario = usuarioRepository.findByNombreUsuario(nombreUsuario)
                .orElseThrow(() -> new NotFoundEntityException("Usuario no encontrado con nombre: " + nombreUsuario));

        List<Review> misReviews = reviewRepository.findByUsuarioId(usuario.getId());
        log.info("Historial cargado con éxito: {} reseñas encontradas para el usuario '{}'", misReviews.size(), nombreUsuario);

        return reviewMapper.toDTOList(misReviews);
    }

    @Override
    @Transactional
    public ReviewDTO actualizarReview(Long id, ReviewDTO dto, String nombreUsuario) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Actualización fallida: Reseña ID {} inexistente.", id);
                    return new NotFoundEntityException("Reseña no encontrada con ID: " + id);
                });

        if (!review.getUsuario().getNombreUsuario().equals(nombreUsuario)) {
            log.warn("ACCESO DENEGADO: El usuario '{}' intentó modificar la reseña ID {}, propiedad de '{}'",
                    nombreUsuario, id, review.getUsuario().getNombreUsuario());
            throw new IllegalArgumentException("No tienes permisos para modificar esta reseña.");
        }

        log.info("Modificando campos de reseña ID {}. Nota anterior: {} -> Nueva nota: {}",
                id, review.getScore(), dto.getScore());

        review.setScore(dto.getScore());
        review.setComment(dto.getComment());

        Review reviewActualizada = reviewRepository.save(review);
        log.info("Reseña ID {} guardada con éxito tras la edición.", id);
        return reviewMapper.toDTO(reviewActualizada);
    }
}