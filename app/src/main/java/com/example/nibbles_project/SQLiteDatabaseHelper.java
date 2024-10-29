package com.example.nibbles_project;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class SQLiteDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Nibbles.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_DAILY_INTAKE = "daily_intake";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_USERNAME = "username"; // New column for username
    private static final String COLUMN_NICOTINE = "nicotine";
    private static final String COLUMN_PROTEIN = "protein";
    private static final String COLUMN_CALORIES = "calories";
    private static final String COLUMN_WATER = "water";

    public SQLiteDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_DAILY_INTAKE + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USERNAME + " TEXT, " + // Include username in table creation
                COLUMN_NICOTINE + " TEXT, " +
                COLUMN_PROTEIN + " TEXT, " +
                COLUMN_CALORIES + " TEXT, " +
                COLUMN_WATER + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DAILY_INTAKE);
        onCreate(db);
    }

    public boolean insertDailyIntake(String username, String nicotine, String protein, String calories, String water) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_USERNAME, username); // Include username
        contentValues.put(COLUMN_NICOTINE, nicotine);
        contentValues.put(COLUMN_PROTEIN, protein);
        contentValues.put(COLUMN_CALORIES, calories);
        contentValues.put(COLUMN_WATER, water);

        long result = db.insert(TABLE_DAILY_INTAKE, null, contentValues);
        return result != -1;  // Return true if data was inserted
    }

    public List<DailyIntake> getAllDailyIntakes() {
        List<DailyIntake> dailyIntakes = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_DAILY_INTAKE, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
                String username = cursor.getString(cursor.getColumnIndex(COLUMN_USERNAME)); // Retrieve username
                String nicotine = cursor.getString(cursor.getColumnIndex(COLUMN_NICOTINE));
                String protein = cursor.getString(cursor.getColumnIndex(COLUMN_PROTEIN));
                String calories = cursor.getString(cursor.getColumnIndex(COLUMN_CALORIES));
                String water = cursor.getString(cursor.getColumnIndex(COLUMN_WATER));

                DailyIntake intake = new DailyIntake(id, username, nicotine, protein, calories, water); // Pass username
                dailyIntakes.add(intake);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return dailyIntakes;
    }

    public List<DailyIntake> getDailyIntakesByUsername(String username) {
        List<DailyIntake> dailyIntakes = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_DAILY_INTAKE + " WHERE " + COLUMN_USERNAME + " = ?", new String[]{username});

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
                String nicotine = cursor.getString(cursor.getColumnIndex(COLUMN_NICOTINE));
                String protein = cursor.getString(cursor.getColumnIndex(COLUMN_PROTEIN));
                String calories = cursor.getString(cursor.getColumnIndex(COLUMN_CALORIES));
                String water = cursor.getString(cursor.getColumnIndex(COLUMN_WATER));

                DailyIntake intake = new DailyIntake(id, username, nicotine, protein, calories, water); // Pass username
                dailyIntakes.add(intake);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return dailyIntakes;
    }
}