package com.example.homework;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.json.JSONArray;
import org.json.JSONObject;

public class DatabaseHelper {

    private static final ExecutorService executorService = Executors.newSingleThreadExecutor();

    // İnternet bağlantısı kontrolü
    public static Future<Boolean> isInternetAvailableAsync(Context context) {
        return executorService.submit(() -> {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isConnected();
        });
    }

    // API bağlantısı kontrolü
    public static Future<Boolean> isApiConnectedAsync(String apiUrl) {
        return executorService.submit(() -> {
            try {
                URL url = new URL(apiUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(5000); // Timeout süresi
                conn.setReadTimeout(5000);    // Okuma timeout süresi

                int responseCode = conn.getResponseCode();
                return responseCode == HttpURLConnection.HTTP_OK; // HTTP 200 dönerse bağlantı başarılı
            } catch (IOException e) {
                e.printStackTrace();
                return false; // Bağlantı başarısız
            }
        });
    }

    // API'den veri çekme
    public static void getLoginLogout(String apiUrl, ICallBack<List<Food>> callback) {
        executorService.submit(() -> {
            List<Food> foodList = new ArrayList<>();
            try {
                // API'ye GET isteği yapıyoruz
                URL url = new URL(apiUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(5000); // Timeout süresi
                conn.setReadTimeout(5000);    // Okuma timeout süresi

                // Bağlantıdan gelen veriyi okuyoruz
                try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                    StringBuilder content = new StringBuilder();
                    String line;
                    while ((line = in.readLine()) != null) {
                        content.append(line);
                    }

                    // JSON yanıtını işliyoruz
                    JSONObject jsonObject = new JSONObject(content.toString());

                    // "data" kısmındaki diziye ulaşalım
                    if (jsonObject.getBoolean("success")) {
                        // "data" anahtarını alıyoruz ve bunu bir JSONArray olarak işliyoruz
                        JSONArray dataArray = jsonObject.getJSONArray("data");

                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject foodObject = dataArray.getJSONObject(i);

                            // JSON'dan verileri alıyoruz
                            int id = foodObject.getInt("id");
                            String foodName = foodObject.getString("foodName");
                            String imageUrl = foodObject.getString("imageUrl");
                            String videoUrl = foodObject.getString("videoUrl");
                            String shortInfo = foodObject.getString("shortInfo");
                            String recipe = foodObject.getString("recipe");
                            String ingredients = foodObject.getString("ingreditens");

                            // Food nesnesini oluşturuyoruz ve listeye ekliyoruz
                            Food food = new Food(id, videoUrl, recipe, imageUrl, shortInfo, ingredients, foodName);
                            foodList.add(food);
                        }

                        // Başarılı olursa callback ile sonucu döndürüyoruz
                        callback.onSuccess(foodList);
                    } else {
                        // Eğer success false ise callback'e hata bildiriyoruz
                        callback.onFailure(new Exception("API success false"));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                // Hata durumunda callback'e hatayı bildiriyoruz
                callback.onFailure(e);
            }
        });
    }
}
