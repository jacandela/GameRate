package com.gamerate.gamerate.service;

import com.gamerate.gamerate.dto.entity.Review.ReviewDTO;
import com.gamerate.gamerate.entity.*;
import com.gamerate.gamerate.enums.Rol;
import com.gamerate.gamerate.exceptions.DuplicateEntityException;
import com.gamerate.gamerate.mappers.ReviewMapper;
import com.gamerate.gamerate.repository.*;
import com.gamerate.gamerate.service.Review.ReviewService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceTest {

    @Mock private IReviewRepository reviewRepository;
    @Mock private IJuegoRepository juegoRepository;
    @Mock private IUsuarioRepository usuarioRepository;
    @Mock private ReviewMapper reviewMapper;

    @InjectMocks
    private ReviewService reviewService;

    @Test
    void testCrearReview_CuandoYaExiste_LanzaExcepcion() {
        String username = "usuarioTest";
        Usuario usuario = new Usuario(); usuario.setId(1L);
        ReviewDTO dto = new ReviewDTO(); dto.setGameId(10L);

        when(usuarioRepository.findByNombreUsuario(username)).thenReturn(Optional.of(usuario));
        when(reviewRepository.existsByUsuarioIdAndJuegoId(1L, 10L)).thenReturn(true);

        assertThrows(DuplicateEntityException.class, () -> reviewService.crearReview(dto, username));
    }

    @Test
    void testEliminarReview_CuandoNoEsPropietario_LanzaExcepcion() {
        Long reviewId = 1L;
        String usuarioActual = "intruso";
        Usuario dueno = new Usuario(); dueno.setNombreUsuario("dueño");
        Usuario intruso = new Usuario(); intruso.setRol(Rol.USER);

        Review review = new Review(); review.setUsuario(dueno);

        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));
        when(usuarioRepository.findByNombreUsuario(usuarioActual)).thenReturn(Optional.of(intruso));

        assertThrows(IllegalArgumentException.class, () -> reviewService.eliminarReview(reviewId, usuarioActual));
    }
}
