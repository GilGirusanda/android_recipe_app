package com.example.myapplication.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class RecipeModel {

    private int id;
    private String title;
    private String description;

    // Constructors

    public RecipeModel(int id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }

    public RecipeModel() {
    }

    // Getters
    public int getId() {
        return this.id;
    }

    public String getTitle() {
        return this.title;
    }

    public String getDescription() {
        return this.description;
    }

    // Setters

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @NonNull
    @Override
    public String toString() {
        return String.format("Recipe(\nid=%s, \ntitle=%s, \ndescription=%s\n)", this.id, this.title, this.description);
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + title.hashCode();
        result = 31 * result + description.hashCode();
        return result;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (this.getClass() != obj.getClass()) return false;
        RecipeModel recipeModel = (RecipeModel) obj;
        return id == recipeModel.id
            && (title.equals(recipeModel.title)
            && description.equals(recipeModel.description));
    }
}
