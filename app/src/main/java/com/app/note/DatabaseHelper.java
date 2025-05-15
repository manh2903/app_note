package com.app.note;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "noteDB";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_GHICHU = "tbl_ghichu";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NOIDUNG = "noidung";
    public static final String COLUMN_NGAYTAO = "ngaytao";

    private static final String CREATE_TABLE_GHICHU = 
            "CREATE TABLE " + TABLE_GHICHU + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_NOIDUNG + " TEXT,"
            + COLUMN_NGAYTAO + " TEXT"
            + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_GHICHU);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GHICHU);
        onCreate(db);
    }
} 