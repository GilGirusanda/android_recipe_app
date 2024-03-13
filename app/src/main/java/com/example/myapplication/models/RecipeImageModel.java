package com.example.myapplication.models;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class RecipeImageModel {

    private int id;
    private String name;
    private Bitmap image;
    private int recipeId;

    // Constructors

    public RecipeImageModel(int id, String name, Bitmap image, Integer recipeId) {
        this.id = id;
        this.name = name;
        this.image = image;
        if(recipeId == null)
            this.recipeId = 0;
        else
            this.recipeId = recipeId.intValue();
    }

    public RecipeImageModel() {
    }

    // Getters
    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public Bitmap getImage() {
        return this.image;
    }

    public int getRecipeId() {
        return this.recipeId;
    }

    // Setters

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    @NonNull
    @Override
    public String toString() {
        return String.format("Recipe(\nid=%s, \nname=%s\n)", this.id, this.name);
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + name.hashCode();
        result = 31 * result + image.hashCode();
        return result;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (this.getClass() != obj.getClass()) return false;
        RecipeImageModel recipeImageModel = (RecipeImageModel) obj;
        return id == recipeImageModel.id
                && (name.equals(recipeImageModel.name)
                && image.equals(recipeImageModel.image));
    }
}
