package com.example.myapplication.services;

import static com.example.myapplication.db.DbManager.COLUMN_RECIPE_DESCRIPTION;
import static com.example.myapplication.db.DbManager.COLUMN_RECIPE_ID;
import static com.example.myapplication.db.DbManager.COLUMN_RECIPE_TITLE;
import static com.example.myapplication.db.DbManager.TABLE_RECIPE;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.myapplication.db.DbManager;
import com.example.myapplication.models.RecipeModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class RecipeService {

    DbManager manager;

    public RecipeService(Context ctx) {
        this.manager = DbManager.getInstance(ctx);
    }

    public boolean addOne(RecipeModel recipeModel) {
        if(Objects.isNull(manager)) return false;

        SQLiteDatabase db = manager.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_RECIPE_TITLE, recipeModel.getTitle());
        cv.put(COLUMN_RECIPE_DESCRIPTION, recipeModel.getDescription());

        long insert = db.insert(TABLE_RECIPE, null, cv);

        db.close();
        if (insert == -1) {
            return false;
        } else {
            return true;
        }
    }

    public List<RecipeModel> getAll() {
        List<RecipeModel> recipeList = new ArrayList<>();
        String queryString = "select * from " + TABLE_RECIPE + ";";

        try (SQLiteDatabase db = manager.getReadableDatabase();
             Cursor cursor = db.rawQuery(queryString, null)
        ) {
            // get data from the db
            if (cursor.moveToFirst()) {
                // loop through the result set
                do {
                    int recipeId = cursor.getInt(0);
                    String recipeTitle = cursor.getString(1);
                    String recipeDescription = cursor.getString(2);
                    recipeList.add(new RecipeModel(recipeId, recipeTitle, recipeDescription));
                } while (cursor.moveToNext());
            } else {
                // failure. no data
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return recipeList;
    }

    public Optional<RecipeModel> findById(int recipeId) {
        RecipeModel foundRecipe = null;
        String queryString = String.format("select * from %s where id = %s;", TABLE_RECIPE, recipeId);

        try (SQLiteDatabase db = manager.getReadableDatabase();
             Cursor cursor = db.rawQuery(queryString, null)
        ) {
            // get data from the db
            if (cursor.moveToFirst()) {
                // go through the result set
                String recipeTitle = cursor.getString(1);
                String recipeDescription = cursor.getString(2);
                foundRecipe = new RecipeModel(recipeId, recipeTitle, recipeDescription);
            } else {
                // failure. no data
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return Optional.ofNullable(foundRecipe);
    }

    public Optional<RecipeModel> findByTitle(String title) {
        RecipeModel foundRecipe = null;
        String queryString = String.format("select * from %s where title = '%s';", TABLE_RECIPE, title);

        try (SQLiteDatabase db = manager.getReadableDatabase();
             Cursor cursor = db.rawQuery(queryString, null)
        ) {
            // get data from the db
            if (cursor.moveToFirst()) {
                // go through the result set
                int recipeId = cursor.getInt(0);
                String recipeTitle = cursor.getString(1);
                String recipeDescription = cursor.getString(2);
                foundRecipe = new RecipeModel(recipeId, recipeTitle, recipeDescription);
            } else {
                // failure. no data
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return Optional.ofNullable(foundRecipe);
    }

    public int update(RecipeModel newRecipeModel) {
        SQLiteDatabase db = manager.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_RECIPE_TITLE, newRecipeModel.getTitle());
        values.put(COLUMN_RECIPE_DESCRIPTION, newRecipeModel.getDescription());

        int rowsAffected = db.update(TABLE_RECIPE, values, COLUMN_RECIPE_ID+" = ?", new String[]{String.valueOf(newRecipeModel.getId())});
        return rowsAffected;
    }

    public boolean deleteRecipeById(int recipeId) {
        String queryString = String.format("delete from %s where %s = %s ;", TABLE_RECIPE, COLUMN_RECIPE_ID, recipeId);

        try (SQLiteDatabase db = manager.getReadableDatabase();
             Cursor cursor = db.rawQuery(queryString, null)
        ) {
            // get data from the db
            if (cursor.moveToFirst()) {
                // deleted successfully
                return true;
            } else {
                // failure. can't delete
                return false;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }
}
