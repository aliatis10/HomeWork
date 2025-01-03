package com.example.homework;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.ExecutionException;

public class LoadingScreenActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private ImageView imageView;
    private String apiUrl = "https://f68yvf9m69.execute-api.eu-central-1.amazonaws.com/prod/get-foods";
    private Handler handler = new Handler();
    private Runnable checkConnectionRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_screen);

        // Layout öğelerini buluyoruz
        progressBar = findViewById(R.id.progressBar);
        imageView = findViewById(R.id.imageView);

        // Sürekli bağlantı kontrolü yapacak olan Runnable tanımlıyoruz
        checkConnectionRunnable = new Runnable() {
            @Override
            public void run() {
                checkConnection();
            }
        };

        // Bağlantı kontrolüne başlıyoruz
        handler.post(checkConnectionRunnable);
    }

    private void checkConnection() {
        new Thread(() -> {
            try {
                // İnternet bağlantısının var olup olmadığını kontrol et
                boolean isInternetAvailable = DatabaseHelper.isInternetAvailableAsync(LoadingScreenActivity.this).get();

                // API bağlantısının var olup olmadığını kontrol et
                boolean isApiConnected = DatabaseHelper.isApiConnectedAsync(apiUrl).get();

                runOnUiThread(() -> {
                    if (isApiConnected && isInternetAvailable) {
                        // Bağlantılar başarılı, ana aktiviteye geçiş
                        Intent intent = new Intent(LoadingScreenActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // Bağlantı başarısız, 1 saniye sonra tekrar kontrol yapalım
                        Toast.makeText(LoadingScreenActivity.this, "Bağlantı sağlanamadı, yeniden deneniyor...", Toast.LENGTH_SHORT).show();
                        handler.postDelayed(checkConnectionRunnable, 1000); // 1 saniye sonra yeniden kontrol et
                    }
                });

            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    Toast.makeText(LoadingScreenActivity.this, "Bağlantı Kontrolü Yapılamadı", Toast.LENGTH_SHORT).show();
                    handler.postDelayed(checkConnectionRunnable, 1000); // Hata durumunda da 1 saniye sonra yeniden kontrol et
                });
            }
        }).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Activity yok olduğunda handler'ın sürekli çalışmasını engellemek için Runnable'ı kaldırıyoruz
        handler.removeCallbacks(checkConnectionRunnable);
    }
}
