package com.sofe4640u.track;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class WorkoutsDatabase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "WorkoutsDatabase.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_WORKOUTS = "workouts";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_WORKOUT_ID = "workout_id";
    private static final String COLUMN_EXERCISE_ID = "exercise_id";
    private static final String COLUMN_REPS = "reps";
    private static final String COLUMN_WEIGHT = "weight";
    private static final String COLUMN_TIMESTAMP = "timestamp";

    public WorkoutsDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createWorkoutsTable = "CREATE TABLE " + TABLE_WORKOUTS + " (" +
                COLUMN_USERNAME + " TEXT NOT NULL, " +
                COLUMN_WORKOUT_ID + " INTEGER, " +
                COLUMN_EXERCISE_ID + " INTEGER, " +
                COLUMN_REPS + " INTEGER, " +
                COLUMN_WEIGHT + " REAL, " +
                COLUMN_TIMESTAMP + " TEXT, " +
                "PRIMARY KEY(" + COLUMN_WORKOUT_ID + ", " + COLUMN_EXERCISE_ID + ", " + COLUMN_TIMESTAMP + "))";

        db.execSQL(createWorkoutsTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WORKOUTS);
        onCreate(db);
    }

    public long addWorkout(String username, int workoutId, int exerciseId, int reps, double weight, String timestamp) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_WORKOUT_ID, workoutId);
        values.put(COLUMN_EXERCISE_ID, exerciseId);
        values.put(COLUMN_REPS, reps);
        values.put(COLUMN_WEIGHT, weight);
        values.put(COLUMN_TIMESTAMP, timestamp);

        return db.insert(TABLE_WORKOUTS, null, values);
    }

    public Cursor getWorkoutLogs(String username, int workoutId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_WORKOUTS,
                null,
                COLUMN_USERNAME + " = ? AND " + COLUMN_WORKOUT_ID + " = ?",
                new String[]{username, String.valueOf(workoutId)},
                null, null, COLUMN_TIMESTAMP + " ASC");
    }
}
