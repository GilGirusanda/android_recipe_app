package com.example.myapplication.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.myapplication.models.RecipeModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DbManager extends SQLiteOpenHelper {

    public final static String DB_NAME = "rec_database.db";

    // RECIPE TABLE DECLARATION
    public final static String TABLE_RECIPE = "recipe";
    public final static String COLUMN_RECIPE_ID = "id";
    public final static String COLUMN_RECIPE_TITLE = "title";
    public final static String COLUMN_RECIPE_DESCRIPTION = "description";

    // IMAGE TABLE DECLARATION
    public final static String TABLE_IMAGE = "recipe_image";
    public final static String COLUMN_IMAGE_ID = "id";
    public final static String COLUMN_IMAGE = "image";
    public final static String COLUMN_IMAGE_RECIPE_ID = "recipe_id";

    public DbManager(@Nullable Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String statementCreateImageTable =
                String.format("""
                CREATE TABLE %s (
                    %s integer primary key autoincrement,
                    %s blob,
                    %s int references %s(%s)
                );
                """, TABLE_IMAGE, COLUMN_IMAGE_ID, COLUMN_IMAGE, COLUMN_IMAGE_RECIPE_ID, TABLE_RECIPE, COLUMN_RECIPE_ID);

        String statementCreateRecipeTable =
                String.format("""
                CREATE TABLE %s (
                    %s integer primary key autoincrement,
                    %s text not null,
                    %s text not null
                );
                """, TABLE_RECIPE, COLUMN_RECIPE_ID, COLUMN_RECIPE_TITLE, COLUMN_RECIPE_DESCRIPTION);

        db.execSQL(statementCreateRecipeTable);
//        db.execSQL(statementCreateImageTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean addOne(RecipeModel recipeModel) {
        SQLiteDatabase db = this.getWritableDatabase();
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

        try (SQLiteDatabase db = this.getReadableDatabase();
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

        try (SQLiteDatabase db = this.getReadableDatabase();
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

    public int update(RecipeModel recipeModel) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_RECIPE_TITLE, recipeModel.getTitle());
        values.put(COLUMN_RECIPE_DESCRIPTION, recipeModel.getDescription());

        int rowsAffected = db.update(TABLE_RECIPE, values, COLUMN_RECIPE_ID+" = ?", new String[]{String.valueOf(recipeModel.getId())});
        return rowsAffected;
    }

    public boolean deleteRecipeById(int recipeId) {
        String queryString = String.format("delete from %s where %s = %s ;", TABLE_RECIPE, COLUMN_RECIPE_ID, recipeId);

        try (SQLiteDatabase db = this.getReadableDatabase();
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
