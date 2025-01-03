package com.example.homework;

public class Food {
    private String videoUrl;
    private String recipe;
    private String imageUrl;
    private String shortInfo;
    private String ingredients;
    private String foodName;
    private int id;

    // Yapıcı metod (constructor)
    public Food(int id,String videoUrl, String recipe, String imageUrl, String shortInfo, String ingredients, String foodName) {
        this.id=id;
        this.videoUrl = videoUrl;
        this.recipe = recipe;
        this.imageUrl = imageUrl;
        this.shortInfo = shortInfo;
        this.ingredients = ingredients;
        this.foodName = foodName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Getter ve Setter metodları
    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getRecipe() {
        return recipe;
    }

    public void setRecipe(String recipe) {
        this.recipe = recipe;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getShortInfo() {
        return shortInfo;
    }

    public void setShortInfo(String shortInfo) {
        this.shortInfo = shortInfo;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }
}
