package com.example.aict.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DBName = "mycontacts.db";
    private static final int DBVer = 2;

    public DBHelper(Context context) {
        super(context, DBName, null, DBVer);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE users ( _id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, age TEXT, sex TEXT, id TEXT, pw TEXT, weight TEXT, exercise TEXT);");
        db.execSQL("CREATE TABLE calendar ( _id INTEGER PRIMARY KEY AUTOINCREMENT, id TEXT, year INTEGER, month INTEGER, day INTEGER, color TEXT )");
        db.execSQL("CREATE TABLE completestate( _id INTEGER PRIMARY KEY AUTOINCREMENT, id TEXT, year INTEGER, month INTEGER, day INTEGER, pos INTEGER, value INTEGER )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS contacts");
        onCreate(db);
    }
}
