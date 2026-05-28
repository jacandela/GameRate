package com.example.gamerate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gamerate.network.ApiClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rvVideojuegos;
    private AdapterVideojuego adaptador;
    private List<Videojuego> listaVideojuegos = new ArrayList<>();
    private FloatingActionButton fabAddGame;
    private Videojuego videojuegoSeleccionado = null;

    private final ActivityResultLauncher<Intent> editorLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> obtenerVideojuegosDesdeApi()
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        rvVideojuegos = findViewById(R.id.rvVideojuegos);
        fabAddGame = findViewById(R.id.fabAddGame);
        rvVideojuegos.setLayoutManager(new LinearLayoutManager(this));

        comprobarRolUsuario();
        obtenerVideojuegosDesdeApi();
    }

    private void comprobarRolUsuario() {
        SharedPreferences preferences = getSharedPreferences("GameratePrefs", Context.MODE_PRIVATE);
        String rol = preferences.getString("user_role", "USER");

        if ("ROLE_ADMIN".equals(rol)) {
            fabAddGame.setVisibility(View.VISIBLE);
            fabAddGame.setOnClickListener(v -> editorLauncher.launch(new Intent(this, EditarVideojuego.class)));
        } else {
            fabAddGame.setVisibility(View.GONE);
        }
    }

    private void obtenerVideojuegosDesdeApi() {
        String token = "Bearer " + getSharedPreferences("GameratePrefs", Context.MODE_PRIVATE).getString("jwt_token", "");

        ApiClient.getApiService().obtenerTodos(token).enqueue(new Callback<List<Videojuego>>() {
            @Override
            public void onResponse(Call<List<Videojuego>> call, Response<List<Videojuego>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    adaptador = new AdapterVideojuego(response.body(), MainActivity.this, new AdapterVideojuego.OnVideojuegoClickListener() {
                        @Override
                        public void onItemClick(Videojuego v) {
                            Intent intent = new Intent(MainActivity.this, DetallesVideojuego.class);
                            intent.putExtra("JUEGO_ID", v.getId());
                            startActivity(intent);
                        }
                        @Override public void onItemLongClick(Videojuego v) { videojuegoSeleccionado = v; }
                    });
                    rvVideojuegos.setAdapter(adaptador);
                    registerForContextMenu(rvVideojuegos);
                }
            }
            @Override public void onFailure(Call<List<Videojuego>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_ajustes) {
            startActivity(new Intent(this, AjustesActivity.class));
            return true;
        } else if (id == R.id.menu_acerca) {
            startActivity(new Intent(this, AboutActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        String role = getSharedPreferences("GameratePrefs", MODE_PRIVATE).getString("user_role", "USER");

        if ("ROLE_ADMIN".equals(role)) {
            menu.add(Menu.NONE, 1, Menu.NONE, getString(R.string.ctx_menu_editar));
            menu.add(Menu.NONE, 2, Menu.NONE, getString(R.string.ctx_menu_eliminar));
        }
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if (videojuegoSeleccionado == null) return super.onContextItemSelected(item);

        if (item.getItemId() == 1) {
            editorLauncher.launch(new Intent(this, EditarVideojuego.class).putExtra("videojuego", videojuegoSeleccionado));
        } else if (item.getItemId() == 2) {
            new androidx.appcompat.app.AlertDialog.Builder(this)
                    .setTitle(getString(R.string.dialog_titulo_eliminar))
                    .setMessage(getString(R.string.dialog_mensaje_eliminar, videojuegoSeleccionado.getTitulo()))
                    .setPositiveButton(getString(R.string.dialog_boton_eliminar), (dialog, which) -> {
                        String token = "Bearer " + getSharedPreferences("GameratePrefs", Context.MODE_PRIVATE).getString("jwt_token", "");
                        ApiClient.getApiService().eliminarVideojuego(token, videojuegoSeleccionado.getId()).enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if (response.isSuccessful()) {
                                    Toast.makeText(MainActivity.this, getString(R.string.msg_delete_success), Toast.LENGTH_SHORT).show();
                                    obtenerVideojuegosDesdeApi();
                                } else {
                                    String errorMsg = getString(R.string.msg_delete_error, String.valueOf(response.code()));
                                    Toast.makeText(MainActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                String networkError = getString(R.string.msg_network_error, t.getMessage());
                                Toast.makeText(MainActivity.this, networkError, Toast.LENGTH_SHORT).show();
                            }
                        });
                    })
                    .setNegativeButton(getString(R.string.dialog_boton_cancelar), null)
                    .show();
            return true;
        }
        return true;
    }
}