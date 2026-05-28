package com.example.gamerate;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.gamerate.network.ApiClient;
import com.example.gamerate.network.dto.UsuarioDTO;

import java.util.Locale;
import android.content.res.Configuration;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AjustesActivity extends AppCompatActivity {

    private Switch switchDarkMode;
    private Spinner spinnerIdioma;
    private Button btnModificarPerfil, btnCerrarSesion;
    private SharedPreferences prefs;

    private ProgressBar progressBar;
    private static final int NOTIFICATION_PERMISSION_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        prefs = getSharedPreferences("GameratePrefs", MODE_PRIVATE);
        aplicarIdioma();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajustes);

        switchDarkMode = findViewById(R.id.switchDarkMode);
        spinnerIdioma = findViewById(R.id.spinnerIdioma);
        btnModificarPerfil = findViewById(R.id.btnModificarPerfil);
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion);
        progressBar = findViewById(R.id.progressBarPerfil);

        switchDarkMode.setChecked(prefs.getBoolean("is_dark_mode", false));
        switchDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            prefs.edit().putBoolean("is_dark_mode", isChecked).apply();
            AppCompatDelegate.setDefaultNightMode(isChecked ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.idiomas_opciones, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerIdioma.setAdapter(adapter);

        int pos = prefs.getInt("idioma_pos", 0);
        spinnerIdioma.setSelection(pos);

        spinnerIdioma.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != prefs.getInt("idioma_pos", 0)) {
                    String langCode = (position == 0) ? "es" : "en";
                    prefs.edit().putInt("idioma_pos", position).putString("app_lang", langCode).apply();

                    recreate();
                }
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });

        btnModificarPerfil.setOnClickListener(v -> mostrarDialogoPerfil());
        btnCerrarSesion.setOnClickListener(v -> cerrarSesionYRedirigir());
    }

    private void aplicarIdioma() {
        String lang = prefs.getString("app_lang", "es");
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.setLocale(locale);
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
    }

    private void mostrarDialogoPerfil() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.dialog_titulo_perfil);

        View viewInflated = LayoutInflater.from(this).inflate(R.layout.dialog_modificar_perfil, null);
        final EditText inputUsuario = viewInflated.findViewById(R.id.editUsuario);
        final EditText inputGmail = viewInflated.findViewById(R.id.editGmail);

        inputUsuario.setText(prefs.getString("usuario_nombre", ""));
        inputGmail.setText(prefs.getString("usuario_gmail", ""));
        builder.setView(viewInflated);

        builder.setPositiveButton(R.string.btn_confirmar, (dialog, which) -> {
            String nuevoNombre = inputUsuario.getText().toString();
            String nuevoEmail = inputGmail.getText().toString();

            if (!nuevoNombre.isEmpty() && !nuevoEmail.isEmpty()) {
                String token = prefs.getString("jwt_token", "");
                UsuarioDTO dto = new UsuarioDTO();
                dto.setNombreUsuario(nuevoNombre);
                dto.setEmail(nuevoEmail);

                progressBar.setVisibility(View.VISIBLE);
                btnModificarPerfil.setEnabled(false);

                ApiClient.getApiService().actualizarMiPerfil("Bearer " + token, dto)
                        .enqueue(new Callback<UsuarioDTO>() {
                            @Override
                            public void onResponse(Call<UsuarioDTO> call, Response<UsuarioDTO> response) {
                                progressBar.setVisibility(View.GONE);
                                btnModificarPerfil.setEnabled(true);
                                if (response.isSuccessful()) {
                                    lanzarNotificacionConPermisos("Gamerate", "Perfil actualizado correctamente.");
                                    ejecutarCierreSesionSilencioso();
                                } else {
                                    String errorMsg = "Error en servidor: " + response.code();
                                    try {
                                        if (response.errorBody() != null) {
                                            errorMsg += " - " + response.errorBody().string();
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    android.util.Log.e("GamerateDebug", errorMsg);
                                    Toast.makeText(AjustesActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                                }
                            }
                            @Override
                            public void onFailure(Call<UsuarioDTO> call, Throwable t) {
                                progressBar.setVisibility(View.GONE);
                                btnModificarPerfil.setEnabled(true);
                                Toast.makeText(AjustesActivity.this, "Error de red", Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {
                Toast.makeText(AjustesActivity.this, "Por favor, rellena todos los campos", Toast.LENGTH_SHORT).show();
            }
        });
        builder.show();
    }

    private void lanzarNotificacionConPermisos(String titulo, String contenido) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, NOTIFICATION_PERMISSION_CODE);
                return;
            }
        }
        lanzarNotificacion(titulo, contenido);
    }

    @SuppressLint("MissingPermission")
    private void lanzarNotificacion(String titulo, String contenido) {
        String CHANNEL_ID = "canal_perfil";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            android.app.NotificationChannel channel = new android.app.NotificationChannel(CHANNEL_ID, "Notificaciones de Perfil", android.app.NotificationManager.IMPORTANCE_DEFAULT);
            getSystemService(android.app.NotificationManager.class).createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(titulo)
                .setContentText(contenido)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        NotificationManagerCompat.from(this).notify((int) System.currentTimeMillis(), builder.build());
    }

    private void cerrarSesionYRedirigir() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.title_confirmacion)
                .setMessage(R.string.msg_confirmar_logout)
                .setPositiveButton(R.string.btn_si, (dialog, which) -> {

                    prefs.edit().remove("usuario_nombre")
                            .remove("usuario_gmail")
                            .remove("jwt_token")
                            .apply();

                    Intent intent = new Intent(this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton(R.string.btn_no, null)
                .show();
    }

    private void ejecutarCierreSesionSilencioso() {
        prefs.edit().remove("usuario_nombre")
                .remove("usuario_gmail")
                .remove("jwt_token")
                .apply();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}