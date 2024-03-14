package com.example.myapplication.services;

import static com.example.myapplication.db.DbManager.COLUMN_IMAGE;
import static com.example.myapplication.db.DbManager.COLUMN_IMAGE_ID;
import static com.example.myapplication.db.DbManager.COLUMN_IMAGE_NAME;
import static com.example.myapplication.db.DbManager.COLUMN_RECIPE_DESCRIPTION;
import static com.example.myapplication.db.DbManager.COLUMN_RECIPE_ID;
import static com.example.myapplication.db.DbManager.COLUMN_RECIPE_TITLE;
import static com.example.myapplication.db.DbManager.TABLE_IMAGE;
import static com.example.myapplication.db.DbManager.TABLE_RECIPE;
import static com.example.myapplication.db.DbManager.COLUMN_IMAGE_RECIPE_ID;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.myapplication.db.DbManager;
import com.example.myapplication.models.RecipeImageModel;
import com.example.myapplication.models.RecipeModel;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class ImageService {

    DbManager manager;
    ByteArrayOutputStream imageOutputStream;
    byte[] imageInBytes;

    public ImageService(Context ctx) {
        this.manager = DbManager.getInstance(ctx);
    }

    public boolean addOne(RecipeImageModel imageModel) {
        if(Objects.isNull(manager)) return false;

        SQLiteDatabase db = manager.getWritableDatabase();
        ContentValues cv = new ContentValues();

        Bitmap imageToSave = imageModel.getImage();
        imageToSave.compress(Bitmap.CompressFormat.PNG, 100, imageOutputStream);
        imageInBytes = imageOutputStream.toByteArray();
        int recipeId = imageModel.getRecipeId();

        cv.put(COLUMN_IMAGE_NAME, imageModel.getName());
        cv.put(COLUMN_IMAGE, imageInBytes);
        cv.put(COLUMN_IMAGE_RECIPE_ID, recipeId);

        long insert = db.insert(TABLE_IMAGE, null, cv);

        db.close();
        if (insert == -1) {
            return false;
        } else {
            return true;
        }
    }

    public List<RecipeImageModel> getAll() {
        List<RecipeImageModel> recipeList = new ArrayList<>();
        String queryString = "select * from " + TABLE_IMAGE + ";";

        try (SQLiteDatabase db = manager.getReadableDatabase();
             Cursor cursor = db.rawQuery(queryString, null)
        ) {
            // get data from the db
            if (cursor.moveToFirst()) {
                // loop through the result set
                do {
                    int imageId = cursor.getInt(0);
                    String imageName = cursor.getString(1);
                    byte[] imageBytes = cursor.getBlob(2);
                    int recipeId = cursor.getInt(3);
                    Bitmap imageValue = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                    recipeList.add(new RecipeImageModel(imageId, imageName, imageValue, recipeId));
                } while (cursor.moveToNext());
            } else {
                // failure. no data
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return recipeList;
    }

    public List<RecipeImageModel> getAllWithLimit(int limit) {
        if(limit <= 0) limit = 1;
        List<RecipeImageModel> recipeList = new ArrayList<>();
        String queryString = "select * from " + TABLE_IMAGE + " limit " + limit + ";";

        try (SQLiteDatabase db = manager.getReadableDatabase();
             Cursor cursor = db.rawQuery(queryString, null)
        ) {
            // get data from the db
            if (cursor.moveToFirst()) {
                // loop through the result set
                do {
                    int imageId = cursor.getInt(0);
                    String imageName = cursor.getString(1);
                    byte[] imageBytes = cursor.getBlob(2);
                    int recipeId = cursor.getInt(3);
                    Bitmap imageValue = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                    recipeList.add(new RecipeImageModel(imageId, imageName, imageValue, recipeId));
                } while (cursor.moveToNext());
            } else {
                // failure. no data
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return recipeList;
    }

    public Optional<RecipeImageModel> findById(int imageId) {
        RecipeImageModel foundImage = null;
        String queryString = String.format("select * from %s where id = %s;", TABLE_IMAGE, imageId);

        try (SQLiteDatabase db = manager.getReadableDatabase();
             Cursor cursor = db.rawQuery(queryString, null)
        ) {
            // get data from the db
            if (cursor.moveToFirst()) {
                // go through the result set
                String imageName = cursor.getString(1);
                byte[] imageBytes = cursor.getBlob(2);
                int recipeId = cursor.getInt(3);
                Bitmap imageValue = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                foundImage = new RecipeImageModel(imageId, imageName, imageValue, recipeId);
            } else {
                // failure. no data
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return Optional.ofNullable(foundImage);
    }

    public int update(RecipeImageModel newImageModel) {
        SQLiteDatabase db = manager.getWritableDatabase();

        Bitmap imageToSave = newImageModel.getImage();
        imageToSave.compress(Bitmap.CompressFormat.PNG, 100, imageOutputStream);
        imageInBytes = imageOutputStream.toByteArray();

        ContentValues values = new ContentValues();
        values.put(COLUMN_IMAGE_NAME, newImageModel.getName());
        values.put(COLUMN_IMAGE, imageInBytes);

        int rowsAffected = db.update(TABLE_RECIPE, values, COLUMN_RECIPE_ID+" = ?", new String[]{String.valueOf(newImageModel.getId())});
        return rowsAffected;
    }

    public int updateName(int imgId, String newName) {
        SQLiteDatabase db = manager.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_IMAGE_NAME, newName);

        int rowsAffected = db.update(TABLE_RECIPE, values, COLUMN_RECIPE_ID+" = ?", new String[]{String.valueOf(imgId)});
        return rowsAffected;
    }

    public int updateImage(int imgId, Bitmap img) {
        SQLiteDatabase db = manager.getWritableDatabase();

        Bitmap imageToSave = img;
        imageToSave.compress(Bitmap.CompressFormat.PNG, 100, imageOutputStream);
        imageInBytes = imageOutputStream.toByteArray();
        
        ContentValues values = new ContentValues();
        values.put(COLUMN_IMAGE, imageInBytes);

        int rowsAffected = db.update(TABLE_RECIPE, values, COLUMN_RECIPE_ID+" = ?", new String[]{String.valueOf(imgId)});
        return rowsAffected;
    }

    public boolean deleteImageById(int imageId) {
        String queryString = String.format("delete from %s where %s = %s ;", TABLE_IMAGE, COLUMN_IMAGE_ID, imageId);

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
