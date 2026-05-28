package com.example.gamerate;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.gamerate.network.ApiClient;
import com.example.gamerate.network.dto.JuegoDTO;
import java.util.*;
import retrofit2.*;

public class EditarVideojuego extends AppCompatActivity {

    private EditText editTitulo, editDesarrollador, editImagenUrl, editDescripcion;
    private LinearLayout llGenerosContainer, llPlataformasContainer;
    private Button btnGuardar;
    private Videojuego juegoEdicion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_videojuego);

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

        editTitulo = findViewById(R.id.editTitulo);
        editDesarrollador = findViewById(R.id.editDesarrollador);
        editImagenUrl = findViewById(R.id.editImagenUrl);
        editDescripcion = findViewById(R.id.editDescripcion);
        llGenerosContainer = findViewById(R.id.llGenerosContainer);
        llPlataformasContainer = findViewById(R.id.llPlataformasContainer);
        btnGuardar = findViewById(R.id.btnGuardar);

        juegoEdicion = (Videojuego) getIntent().getSerializableExtra("videojuego");
        if (juegoEdicion != null) {
            editTitulo.setText(juegoEdicion.getTitulo());
            editDesarrollador.setText(juegoEdicion.getDesarrollador());
            editDescripcion.setText(juegoEdicion.getDescripcion());
            editImagenUrl.setText(juegoEdicion.getImagen());
        }

        cargarCatalogos();
        btnGuardar.setOnClickListener(v -> ejecutarGuardado());
    }

    private void cargarCatalogos() {
        String token = "Bearer " + getSharedPreferences("GameratePrefs", MODE_PRIVATE).getString("jwt_token", "");

        ApiClient.getApiService().obtenerGeneros(token).enqueue(new Callback<List<String>>() {
            @Override public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    for (String g : response.body()) { CheckBox cb = new CheckBox(EditarVideojuego.this); cb.setText(g); llGenerosContainer.addView(cb); }
                }
            }
            @Override public void onFailure(Call<List<String>> call, Throwable t) {}
        });

        ApiClient.getApiService().obtenerPlataformas(token).enqueue(new Callback<List<String>>() {
            @Override public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    for (String p : response.body()) { CheckBox cb = new CheckBox(EditarVideojuego.this); cb.setText(p); llPlataformasContainer.addView(cb); }
                }
            }
            @Override public void onFailure(Call<List<String>> call, Throwable t) {}
        });
    }

    private List<String> obtenerSeleccionados(LinearLayout container) {
        List<String> sel = new ArrayList<>();
        for (int i = 0; i < container.getChildCount(); i++) {
            CheckBox cb = (CheckBox) container.getChildAt(i);
            if (cb.isChecked()) sel.add(cb.getText().toString());
        }
        return sel;
    }

    private void ejecutarGuardado() {
        String token = "Bearer " + getSharedPreferences("GameratePrefs", MODE_PRIVATE).getString("jwt_token", "");
        JuegoDTO dto = new JuegoDTO();
        dto.setTitulo(editTitulo.getText().toString());
        dto.setNombreDesarrollador(editDesarrollador.getText().toString());
        dto.setImagen(editImagenUrl.getText().toString());
        dto.setDescripcion(editDescripcion.getText().toString());
        dto.setGeneros(obtenerSeleccionados(llGenerosContainer));
        dto.setPlataformas(obtenerSeleccionados(llPlataformasContainer));

        Callback<JuegoDTO> callback = new Callback<JuegoDTO>() {
            @Override public void onResponse(Call<JuegoDTO> call, Response<JuegoDTO> response) {
                if (response.isSuccessful()) { finish(); }
            }
            @Override public void onFailure(Call<JuegoDTO> call, Throwable t) {}
        };

        if (juegoEdicion != null) {
            ApiClient.getApiService().actualizarVideojuego(token, juegoEdicion.getId(), dto).enqueue(callback);
        } else {
            ApiClient.getApiService().guardarVideojuego(token, dto).enqueue(callback);
        }
    }
}