package com.gamerate.gamerate.controller;

import com.gamerate.gamerate.dto.entity.Review.ReviewDTO;
import com.gamerate.gamerate.service.Review.IReviewService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private IReviewService reviewService;

    @Test
    @WithMockUser(username = "usuario1")
    void testCrearReview() throws Exception {
        ReviewDTO dto = new ReviewDTO();
        dto.setGameId(1L);
        dto.setComment("Juego increíble");

        when(reviewService.crearReview(any(ReviewDTO.class), eq("usuario1"))).thenReturn(dto);

        mockMvc.perform(post("/api/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"gameId\":1, \"comentario\":\"Juego increíble\"}"))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser
    void testGetReviewsByGame() throws Exception {
        mockMvc.perform(get("/api/reviews/game/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "usuario1")
    void testListarMisReviews() throws Exception {
        mockMvc.perform(get("/api/reviews/MisReviews"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "usuario1")
    void testDeleteReview() throws Exception {
        mockMvc.perform(delete("/api/reviews/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "usuario1")
    void testActualizarReview() throws Exception {
        ReviewDTO dto = new ReviewDTO();
        dto.setComment("Nueva opinión");

        when(reviewService.actualizarReview(eq(1L), any(ReviewDTO.class), eq("usuario1"))).thenReturn(dto);

        mockMvc.perform(put("/api/reviews/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"comentario\":\"Nueva opinión\"}"))
                .andExpect(status().isOk());
    }
}