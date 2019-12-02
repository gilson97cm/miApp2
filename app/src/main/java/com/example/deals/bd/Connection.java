package com.example.deals.bd;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class Connection extends SQLiteOpenHelper {

    public Connection(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE deal (id INTEGER PRIMARY KEY, ruc TEXT, name TEXT, direction TEXT, logo BLOG)");
        db.execSQL("CREATE TABLE category (id INTEGER PRIMARY KEY  , name TEXT, direction TEXT, idDeal INT)");
        db.execSQL("CREATE TABLE product (id INTEGER PRIMARY KEY , name TEXT, description TEXT, price FLOAT, stock INT, idCategory)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}