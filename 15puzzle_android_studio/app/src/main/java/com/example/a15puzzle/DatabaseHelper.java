package com.example.a15puzzle;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "scoreDatabase.db";
    public static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "scoreTable";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_N = "n";
    public static final String COLUMN_BEST_SCORE = "bestScore";
    public static final String COLUMN_BEST_TIME = "bestTime";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_N + " INTEGER, " +
                COLUMN_BEST_SCORE + " INTEGER, " +
                COLUMN_BEST_TIME + " INTEGER)";

        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String dropTable = "DROP TABLE IF EXISTS " + TABLE_NAME;
        db.execSQL(dropTable);

        onCreate(db);
    }
    public void saveBestScoreAndTime(int n, int score, int time) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Check if a record for this n already exists
        Cursor cursor = db.query(TABLE_NAME,
                new String[]{COLUMN_ID, COLUMN_BEST_SCORE, COLUMN_BEST_TIME},
                COLUMN_N + "=?",
                new String[]{String.valueOf(n)},
                null, null, null);

        if (cursor.moveToFirst()) {
            // A record for this n exists
            int id = cursor.getInt(0);
            int bestScore = cursor.getInt(1);
            int bestTime = cursor.getInt(2);

            // Check if the current score or time is better
            if (score < bestScore || time < bestTime) {
                ContentValues values = new ContentValues();
                if (score < bestScore) {
                    values.put(COLUMN_BEST_SCORE, score);
                }
                if (time < bestTime) {
                    values.put(COLUMN_BEST_TIME, time);
                }

                // Update the record
                db.update(TABLE_NAME, values, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
            }
        } else {
            // No record for this n exists, so create a new one
            ContentValues values = new ContentValues();
            values.put(COLUMN_N, n);
            values.put(COLUMN_BEST_SCORE, score);
            values.put(COLUMN_BEST_TIME, time);

            // Insert the new record
            db.insert(TABLE_NAME, null, values);
        }

        cursor.close();
        db.close();
    }
    public int getBestScore(int n) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_BEST_SCORE + " FROM " + TABLE_NAME + " WHERE " + COLUMN_N + " = ?", new String[]{String.valueOf(n)});
        if(cursor.moveToFirst())
            return cursor.getInt(0);
        else
            return 0;
    }

    public int getBestTime(int n) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_BEST_TIME + " FROM " + TABLE_NAME + " WHERE " + COLUMN_N + " = ?", new String[]{String.valueOf(n)});
        if(cursor.moveToFirst())
            return cursor.getInt(0);
        else
            return 0;
    }

}
