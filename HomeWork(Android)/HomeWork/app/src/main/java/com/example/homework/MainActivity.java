package com.example.homework;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FoodAdapter foodAdapter;
    private List<Food> foodList;
    private String apiUrl = "https://f68yvf9m69.execute-api.eu-central-1.amazonaws.com/prod/get-foods";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // RecyclerView tanımlama
        recyclerView = findViewById(R.id.recyclerViewYemekler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Listeyi başlatma
        foodList = new ArrayList<>();

        // Adapter'i oluşturma
        foodAdapter = new FoodAdapter(this, foodList, new FoodAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // Tıklanan öğeyi al
                Food clickedFood = foodList.get(position);

                // Tıklama olayını işleme
                Toast.makeText(MainActivity.this, "Tıklanan Yemek: " + clickedFood.getFoodName(), Toast.LENGTH_SHORT).show();

                // Yeni bir aktivite başlatma örneği
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra("recipe", clickedFood.getRecipe());
                intent.putExtra("videoUrl", clickedFood.getVideoUrl());
                intent.putExtra("ingreditens", clickedFood.getIngredients());
                startActivity(intent);
            }
        });

        // RecyclerView adapter'ını ayarlama
        recyclerView.setAdapter(foodAdapter);

        // Verileri API'den çekme
        loadData();
    }

    private void loadData() {
        DatabaseHelper.getLoginLogout(apiUrl, new ICallBack<List<Food>>() {
            @Override
            public void onSuccess(List<Food> result) {
                foodList.clear(); // Önceki verileri temizle
                foodList.addAll(result); // Yeni verileri ekle

                // UI üzerinde değişiklik yapabilmek için runOnUiThread kullanıyoruz
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Adapter'e veri geldiğinde RecyclerView'u güncelle
                        foodAdapter.notifyDataSetChanged();
                    }
                });
            }

            @Override
            public void onFailure(Exception e) {
                // Hata durumunda kullanıcıyı bilgilendirebilirsiniz
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "Veri çekilirken bir hata oluştu.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
