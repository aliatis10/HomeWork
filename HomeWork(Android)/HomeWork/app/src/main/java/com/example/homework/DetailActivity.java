package com.example.homework;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {

    private WebView youtubeWebView;
    private TextView textViewTarif;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Layout elemanlarını tanımlama
        youtubeWebView = findViewById(R.id.youtubeWebView);
        textViewTarif = findViewById(R.id.textViewTarif);

        // Intent ile gelen video URL'sini ve tarif bilgisini alma
        String videoUrl = getIntent().getStringExtra("videoUrl");
        String recipe = getIntent().getStringExtra("recipe");
        String ingreditens=getIntent().getStringExtra("ingreditens");
        String printText="Malzemeler:\n"+ingreditens+"\nHazırlanışı:\n"+recipe;
        // Tarif metnini TextView'e yükleme
        textViewTarif.setText(printText);

        // WebView ayarlarını yapılandırma
        WebSettings webSettings = youtubeWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccess(true);

        // WebView ile YouTube videosunu embed etme
        String embedUrl = getYouTubeEmbedUrl(videoUrl);
        youtubeWebView.loadUrl(embedUrl);
    }

    private String getYouTubeEmbedUrl(String videoUrl) {
        // YouTube video URL'sinden embed URL'sini çıkarma
        String videoId = videoUrl.split("v=")[1];
        if (videoId.contains("&")) {
            videoId = videoId.split("&")[0];
        }
        return "https://www.youtube.com/embed/" + videoId;
    }
}
