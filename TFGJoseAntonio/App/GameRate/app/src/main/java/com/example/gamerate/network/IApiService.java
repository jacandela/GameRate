package com.example.gamerate.network;

import com.example.gamerate.Videojuego;
import com.example.gamerate.network.dto.JuegoDTO;
import com.example.gamerate.network.dto.JuegoStatsDTO;
import com.example.gamerate.network.dto.ReviewDTO;
import com.example.gamerate.network.dto.UsuarioDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IApiService {
    @POST("auth/login")
    Call<JwtResponse> login(@Body LoginRequest loginRequest);

    @POST("usuarios/registro")
    Call<UsuarioDTO> registrarUsuario(
            @Body UsuarioDTO usuarioDTO,
            @Query("password") String password
    );

    @PUT("usuarios/MiPerfil")
    Call<UsuarioDTO> actualizarMiPerfil(
            @Header("Authorization") String token,
            @Body UsuarioDTO usuarioDTO
    );

    //---------------Juegos--------------------
    @GET("juegos/{id}")
    Call<JuegoDTO> getJuegoById(@Header("Authorization") String token, @Path("id") Long id);
    @GET("juegos")
    Call<List<Videojuego>> obtenerTodos(@Header("Authorization") String token);

    // Para CREAR un videojuego nuevo
    @POST("juegos")
    Call<JuegoDTO> guardarVideojuego(
            @Header("Authorization") String token,
            @Body JuegoDTO juegoDto
    );

    @PUT("juegos/{id}")
    Call<JuegoDTO> actualizarVideojuego(
            @Header("Authorization") String token,
            @Path("id") Long id,
            @Body JuegoDTO juegoDto
    );

    @DELETE("juegos/{id}")
    Call<Void> eliminarVideojuego(
            @Header("Authorization") String token,
            @Path("id") Long id
    );

    @GET("catalogo/generos")
    Call<List<String>> obtenerGeneros(@Header("Authorization") String token);

    @GET("catalogo/plataformas")
    Call<List<String>> obtenerPlataformas(@Header("Authorization") String token);

    //-----------Review--------------

    @GET("juegos/{id}/stats")
    Call<JuegoStatsDTO> obtenerEstadisticas(@Header("Authorization") String token, @Path("id") Long id);

    @GET("reviews/game/{gameId}")
    Call<List<ReviewDTO>> getReviewsByGame(@Header("Authorization") String token, @Path("gameId") Long gameId);

    @POST("reviews")
    Call<ReviewDTO> crearReview(@Header("Authorization") String token, @Body ReviewDTO review);

    @PUT("reviews/{id}")
    Call<ReviewDTO> actualizarReview(@Header("Authorization") String token, @Path("id") Long id, @Body ReviewDTO review);

    @DELETE("reviews/{id}")
    Call<Void> eliminarReview(@Header("Authorization") String token, @Path("id") Long id);
}