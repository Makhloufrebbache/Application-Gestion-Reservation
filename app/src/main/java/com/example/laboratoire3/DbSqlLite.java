package com.example.laboratoire3;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbSqlLite extends SQLiteOpenHelper {

    // Database name
    private static final String DATABASE_NAME = "reservations.db";
    // Database version
    private static final int DATABASE_VERSION = 1;

    // Reservation table name
    public static final String TABLE_RESERVATIONS = "reservations";
    // Table columns
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_ROOM_TYPE = "room_type";
    public static final String COLUMN_PRICE = "price";

    // SQL query to create the reservation table
    private static final String DATABASE_CREATE = "CREATE TABLE "
            + TABLE_RESERVATIONS + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_ROOM_TYPE + " TEXT, "
            + COLUMN_PRICE + " REAL);";

    public DbSqlLite(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create the reservations table when the database is created
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Space in blank
    }
}