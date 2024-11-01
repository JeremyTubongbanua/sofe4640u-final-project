package com.sofe4640u.track;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class WorkoutsDatabase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "WorkoutsDatabase.db";
    private static final int DATABASE_VERSION = 1;

    // Workouts table
    private static final String TABLE_WORKOUTS = "workouts";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_WORKOUT_REF_ID = "workout_ref_id";

    // Workout table
    private static final String TABLE_WORKOUT = "workout";
    private static final String COLUMN_WORKOUT_ID = "workout_id";
    private static final String COLUMN_SET_REF_ID = "set_ref_id";

    // Set table
    private static final String TABLE_SET = "set";
    private static final String COLUMN_SET_ID = "set_id";
    private static final String COLUMN_EXERCISE_REF_ID = "exercise_ref_id";
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
                COLUMN_WORKOUT_REF_ID + " INTEGER PRIMARY KEY AUTOINCREMENT)";

        String createWorkoutTable = "CREATE TABLE " + TABLE_WORKOUT + " (" +
                COLUMN_WORKOUT_ID + " INTEGER, " +
                COLUMN_SET_REF_ID + " INTEGER, " +
                "PRIMARY KEY(" + COLUMN_WORKOUT_ID + ", " + COLUMN_SET_REF_ID + "))";

        String createSetTable = "CREATE TABLE " + TABLE_SET + " (" +
                COLUMN_SET_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_EXERCISE_REF_ID + " INTEGER, " +
                COLUMN_REPS + " INTEGER, " +
                COLUMN_WEIGHT + " REAL, " +
                COLUMN_TIMESTAMP + " TEXT)";

        db.execSQL(createWorkoutsTable);
        db.execSQL(createWorkoutTable);
        db.execSQL(createSetTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WORKOUTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WORKOUT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SET);
        onCreate(db);
    }

    public long addWorkout(String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        return db.insert(TABLE_WORKOUTS, null, values);
    }

    public long addSet(int workoutId, int exerciseRefId, int reps, double weight, String timestamp) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues setValues = new ContentValues();
        setValues.put(COLUMN_EXERCISE_REF_ID, exerciseRefId);
        setValues.put(COLUMN_REPS, reps);
        setValues.put(COLUMN_WEIGHT, weight);
        setValues.put(COLUMN_TIMESTAMP, timestamp);

        long setId = db.insert(TABLE_SET, null, setValues);

        ContentValues workoutValues = new ContentValues();
        workoutValues.put(COLUMN_WORKOUT_ID, workoutId);
        workoutValues.put(COLUMN_SET_REF_ID, setId);
        db.insert(TABLE_WORKOUT, null, workoutValues);

        return setId;
    }

    public Cursor getWorkoutLogs(String username, int workoutRefId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT s.reps, s.weight, s.timestamp " +
                "FROM " + TABLE_WORKOUTS + " w " +
                "JOIN " + TABLE_WORKOUT + " wk ON w." + COLUMN_WORKOUT_REF_ID + " = wk." + COLUMN_WORKOUT_ID + " " +
                "JOIN " + TABLE_SET + " s ON wk." + COLUMN_SET_REF_ID + " = s." + COLUMN_SET_ID + " " +
                "WHERE w." + COLUMN_USERNAME + " = ? AND w." + COLUMN_WORKOUT_REF_ID + " = ? " +
                "ORDER BY s." + COLUMN_TIMESTAMP + " ASC";
        return db.rawQuery(query, new String[]{username, String.valueOf(workoutRefId)});
    }
}
