package com.example.myapplication.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.Objects;

public class DbManager extends SQLiteOpenHelper {

    private static DbManager managerInstance;

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
    public final static String COLUMN_IMAGE_NAME = "name";
    public final static String COLUMN_IMAGE_RECIPE_ID = "recipe_id";

    private DbManager(@Nullable Context context) {
        super(context, DB_NAME, null, 1);
    }

    public static DbManager getInstance(@Nullable Context context) {
        if(Objects.isNull(managerInstance))
            return new DbManager(context);
        else
            return managerInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String statementCreateImageTable =
                String.format("""
                CREATE TABLE %s (
                    %s integer primary key autoincrement,
                    %s text,
                    %s blob,
                    %s int references %s(%s)
                );
                """, TABLE_IMAGE, COLUMN_IMAGE_ID, COLUMN_IMAGE_NAME, COLUMN_IMAGE, COLUMN_IMAGE_RECIPE_ID, TABLE_RECIPE, COLUMN_RECIPE_ID);

        String statementCreateRecipeTable =
                String.format("""
                CREATE TABLE %s (
                    %s integer primary key autoincrement,
                    %s text not null,
                    %s text not null
                );
                """, TABLE_RECIPE, COLUMN_RECIPE_ID, COLUMN_RECIPE_TITLE, COLUMN_RECIPE_DESCRIPTION);

        db.execSQL(statementCreateRecipeTable);
        db.execSQL(statementCreateImageTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
