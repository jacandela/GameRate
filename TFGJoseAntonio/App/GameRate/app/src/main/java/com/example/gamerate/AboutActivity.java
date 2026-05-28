package com.example.gamerate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import java.util.Locale;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

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

        VideoView videoView = findViewById(R.id.videoViewAbout);
        findViewById(R.id.btnSoporte).setOnClickListener(v -> enviarEmailSoporte());
        findViewById(R.id.btnCompartir).setOnClickListener(v -> compartirApp());

        String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.videomuestra;
        videoView.setVideoURI(Uri.parse(videoPath));

        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);

        videoView.start();
    }

    private void enviarEmailSoporte() {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:soporte@gamerate.com"));
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.subject_soporte));
        startActivity(Intent.createChooser(intent, getString(R.string.chooser_email)));
    }

    private void compartirApp() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.text_compartir));
        startActivity(Intent.createChooser(intent, getString(R.string.chooser_compartir)));
    }
}