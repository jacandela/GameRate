package com.example.gamerate;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.gamerate.network.ApiClient;
import com.example.gamerate.network.dto.*;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetallesVideojuego extends AppCompatActivity {

    private LinearLayout layoutReviews;
    private TextView txtScore;
    private JuegoDTO juego;
    private Long juegoId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_videojuego);

        SharedPreferences prefs = getSharedPreferences("GameratePrefs", MODE_PRIVATE);
        boolean isDarkMode = prefs.getBoolean("is_dark_mode", false);
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        String lang = prefs.getString("app_lang", "es");
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.setLocale(locale);
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());

        txtScore = findViewById(R.id.txtScore);
        juegoId = getIntent().getLongExtra("JUEGO_ID", -1L);

        if (juegoId != -1L) {
            cargarJuegoDesdeApi(juegoId);
            findViewById(R.id.btnAddReview).setOnClickListener(v -> mostrarDialogoResena());
        }
    }

    private void cargarJuegoDesdeApi(Long id) {
        String token = "Bearer " + getSharedPreferences("GameratePrefs", MODE_PRIVATE).getString("jwt_token", "");

        ApiClient.getApiService().getJuegoById(token, id).enqueue(new Callback<JuegoDTO>() {
            @Override
            public void onResponse(Call<JuegoDTO> call, Response<JuegoDTO> response) {
                if (response.isSuccessful() && response.body() != null) {
                    juego = response.body();
                    actualizarInterfaz();
                    cargarDatos(juego.getId());
                }
            }
            @Override public void onFailure(Call<JuegoDTO> call, Throwable t) {
                Toast.makeText(DetallesVideojuego.this, "Error de red", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void actualizarInterfaz() {
        TextView txtTitle = findViewById(R.id.txtTitle);
        TextView txtDeveloper = findViewById(R.id.txtDeveloper);
        TextView txtPlatforms = findViewById(R.id.txtPlatforms);
        TextView txtGenero = findViewById(R.id.txtGenero);
        TextView txtDescription = findViewById(R.id.txtDescription);
        ImageView imgGame = findViewById(R.id.imgGame);

        if (txtTitle != null) txtTitle.setText(juego.getTitulo());
        String na = "N/A";

        if (txtDeveloper != null) {
            String dev = (juego.getNombreDesarrollador() != null) ? juego.getNombreDesarrollador() : na;
            txtDeveloper.setText(getString(R.string.txt_developer) + dev);
        }
        if (txtPlatforms != null) {
            String plat = (juego.getPlataformas() != null) ? String.join(", ", juego.getPlataformas()) : na;
            txtPlatforms.setText(getString(R.string.txt_platforms) + plat);
        }
        if (txtGenero != null) {
            String gen = (juego.getGeneros() != null) ? String.join(", ", juego.getGeneros()) : na;
            txtGenero.setText(getString(R.string.txt_genero) + gen);
        }
        if (txtDescription != null) {
            txtDescription.setText(juego.getDescripcion() != null ?
                    juego.getDescripcion() : getString(R.string.txt_sin_desc));
        }

        if (imgGame != null) {
            String urlImagen = (juego.getImagen() != null && !juego.getImagen().isEmpty())
                    ? juego.getImagen()
                    : "https://picsum.photos/id/1/300/200";

            Glide.with(this)
                    .load(urlImagen)
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .error(R.drawable.ic_launcher_foreground)
                    .transform(new RoundedCorners(16))
                    .into(imgGame);
        }
    }

    private void cargarDatos(Long id) {
        String token = "Bearer " + getSharedPreferences("GameratePrefs", MODE_PRIVATE).getString("jwt_token", "");

        ApiClient.getApiService().obtenerEstadisticas(token, id).enqueue(new Callback<JuegoStatsDTO>() {
            @Override public void onResponse(Call<JuegoStatsDTO> call, Response<JuegoStatsDTO> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String scoreText = getString(R.string.txt_score,
                            response.body().getNotaMedia(),
                            response.body().getTotalResenas());

                    txtScore.setText(scoreText);
                }
            }
            @Override public void onFailure(Call<JuegoStatsDTO> call, Throwable t) {}
        });

        ApiClient.getApiService().getReviewsByGame(token, id).enqueue(new Callback<List<ReviewDTO>>() {
            @Override public void onResponse(Call<List<ReviewDTO>> call, Response<List<ReviewDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    renderizarReviews(response.body());
                }
            }
            @Override public void onFailure(Call<List<ReviewDTO>> call, Throwable t) {}
        });
    }

    private void renderizarReviews(List<ReviewDTO> reviews) {
        layoutReviews = findViewById(R.id.layoutReviews);
        layoutReviews.removeAllViews();
        String currentUser = getSharedPreferences("GameratePrefs", MODE_PRIVATE).getString("username", "");

        String rolUsuario = getSharedPreferences("GameratePrefs", MODE_PRIVATE).getString("user_role", "USER");
        boolean esAdmin = "ROLE_ADMIN".equalsIgnoreCase(rolUsuario);

        for (ReviewDTO review : reviews) {
            View reviewView = getLayoutInflater().inflate(R.layout.item_review, null);
            String reviewUser = review.getUsername();

            ((TextView)reviewView.findViewById(R.id.txtUser)).setText(reviewUser != null ? reviewUser : "Anónimo");
            ((TextView)reviewView.findViewById(R.id.txtScoreReview)).setText("Nota: " + review.getScore() + "/10");
            ((TextView)reviewView.findViewById(R.id.txtComment)).setText(review.getComment());

            ImageButton btnDelete = reviewView.findViewById(R.id.btnDelete);
            android.util.Log.d("DEBUG_DELETE", "Comparando: '" + reviewUser + "' vs '" + currentUser + "'");
            if (esAdmin || (reviewUser != null && reviewUser.equalsIgnoreCase(currentUser))) {
                btnDelete.setVisibility(View.VISIBLE);
                btnDelete.setOnClickListener(v -> new AlertDialog.Builder(this)
                        .setTitle("Eliminar reseña").setMessage("¿Eliminar tu reseña?")
                        .setPositiveButton("Sí", (d, w) -> eliminarReview(review.getId()))
                        .setNegativeButton("No", null).show());
            } else {
                btnDelete.setVisibility(View.GONE);
            }
            layoutReviews.addView(reviewView);
        }
    }

    private void eliminarReview(Long reviewId) {
        String token = "Bearer " + getSharedPreferences("GameratePrefs", MODE_PRIVATE).getString("jwt_token", "");
        ApiClient.getApiService().eliminarReview(token, reviewId).enqueue(new Callback<Void>() {
            @Override public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(DetallesVideojuego.this, "Reseña eliminada", Toast.LENGTH_SHORT).show();
                    cargarDatos(juegoId);
                }
            }
            @Override public void onFailure(Call<Void> call, Throwable t) {}
        });
    }

    private void mostrarDialogoResena() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_crear_review, null);
        EditText editComentario = dialogView.findViewById(R.id.editComentario);
        RatingBar ratingBar = dialogView.findViewById(R.id.ratingBar);

        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.dialog_titulo_reseña))
                .setView(dialogView)
                .setPositiveButton(getString(R.string.btn_publicar), (dialog, which) -> {
                    String comentario = editComentario.getText().toString();
                    double notaFinal = (double) ratingBar.getRating() * 2.0;
                    if (comentario.isEmpty()) Toast.makeText(this, "Comentario vacío", Toast.LENGTH_SHORT).show();
                    else enviarResenaApi(comentario, notaFinal);
                })
                .setNegativeButton(getString(R.string.btn_cancelar), null)
                .show();
    }

    private void enviarResenaApi(String comentario, double rating) {
        String token = "Bearer " + getSharedPreferences("GameratePrefs", MODE_PRIVATE).getString("jwt_token", "");
        ReviewDTO nueva = new ReviewDTO();
        nueva.setGameId(juegoId);
        nueva.setComment(comentario);
        nueva.setScore(java.math.BigDecimal.valueOf(rating));

        ApiClient.getApiService().crearReview(token, nueva).enqueue(new Callback<ReviewDTO>() {
            @Override public void onResponse(Call<ReviewDTO> call, Response<ReviewDTO> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(DetallesVideojuego.this, "¡Publicada!", Toast.LENGTH_SHORT).show();
                    cargarDatos(juegoId);
                }
            }
            @Override public void onFailure(Call<ReviewDTO> call, Throwable t) {}
        });
    }
}