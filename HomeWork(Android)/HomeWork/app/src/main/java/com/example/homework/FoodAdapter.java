package com.example.homework;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {


    private final Context context;
    private final List<Food> foodList;
    private final OnItemClickListener listener;



    // Tıklama olayları için arayüz
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    // Yapıcı metod
    public FoodAdapter(Context context, List<Food> foodList, OnItemClickListener listener) {
        this.context = context;
        this.foodList = foodList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // LayoutInflater ile öğe layout'unu bağlama
        View view = LayoutInflater.from(context).inflate(R.layout.food_list_item, parent, false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        // Şu anki yemek nesnesini al
        Food food = foodList.get(position);

        // Yemek adını ve kısa bilgisini View'a bağla
        holder.foodNameTextView.setText(food.getFoodName());
        holder.shortInfoTextView.setText(food.getShortInfo());

        // Glide kullanarak görseli yükle
        Glide.with(context)
                .load(food.getImageUrl()) // Görsel URL'si
                .apply(new RequestOptions().override(48,48)
                        .placeholder(R.drawable.ic_launcher_background) // Yüklenirken gösterilecek varsayılan görsel
                        .error(R.drawable.ic_launcher_background)) // Hata durumunda gösterilecek görsel
                .into(holder.foodImageView); // Görseli ImageView'e yükle

        // Tıklama olayını ayarla
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return foodList.size(); // Listenin boyutunu döndür
    }

    // ViewHolder: Her bir öğenin View referanslarını tutar
    public static class FoodViewHolder extends RecyclerView.ViewHolder {

        ImageView foodImageView;
        TextView foodNameTextView;
        TextView shortInfoTextView;

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);

            // View öğelerini tanımlama
            foodImageView = itemView.findViewById(R.id.food_image);
            foodNameTextView = itemView.findViewById(R.id.food_name);
            shortInfoTextView = itemView.findViewById(R.id.short_info);
        }
    }
}
