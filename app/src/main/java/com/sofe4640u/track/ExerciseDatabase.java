package com.sofe4640u.track;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class ExerciseDatabase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "ExerciseDatabase.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_EXERCISE = "exercise";
    private static final String COLUMN_EXERCISE_ID = "exercise_id";
    private static final String COLUMN_EXERCISE_NAME = "exercise_name";

    private static final String TABLE_MUSCLE = "muscle";
    private static final String COLUMN_MUSCLE_ID = "muscle_id";
    private static final String COLUMN_MUSCLE_NAME = "muscle_name";
    private static final String COLUMN_EXERCISE_REF_ID = "exercise_ref_id"; // Foreign key to link muscle to exercise

    public ExerciseDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createExerciseTable = "CREATE TABLE " + TABLE_EXERCISE + " (" +
                COLUMN_EXERCISE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_EXERCISE_NAME + " TEXT NOT NULL UNIQUE)";

        String createMuscleTable = "CREATE TABLE " + TABLE_MUSCLE + " (" +
                COLUMN_MUSCLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_MUSCLE_NAME + " TEXT NOT NULL, " +
                COLUMN_EXERCISE_REF_ID + " INTEGER, " +
                "FOREIGN KEY(" + COLUMN_EXERCISE_REF_ID + ") REFERENCES " + TABLE_EXERCISE + "(" + COLUMN_EXERCISE_ID + "))";

        db.execSQL(createExerciseTable);
        db.execSQL(createMuscleTable);

        // Insert default exercises and muscles
        insertDefaultExercises(db);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MUSCLE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXERCISE);
        onCreate(db);
    }

    public long addExercise(String exerciseName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_EXERCISE_NAME, exerciseName);

        return db.insert(TABLE_EXERCISE, null, values);
    }

    public long addMuscle(String exerciseName, String muscleName) {
        SQLiteDatabase db = this.getWritableDatabase();

        long exerciseId = getExerciseId(exerciseName);
        if (exerciseId == -1) return -1; // Exercise does not exist

        ContentValues values = new ContentValues();
        values.put(COLUMN_MUSCLE_NAME, muscleName);
        values.put(COLUMN_EXERCISE_REF_ID, exerciseId);

        return db.insert(TABLE_MUSCLE, null, values);
    }

    public long getExerciseId(String exerciseName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_EXERCISE,
                new String[]{COLUMN_EXERCISE_ID},
                COLUMN_EXERCISE_NAME + " = ?",
                new String[]{exerciseName},
                null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            long id = cursor.getLong(0);
            cursor.close();
            return id;
        }
        return -1;
    }

    public List<String> getMusclesForExercise(String exerciseName) {
        SQLiteDatabase db = this.getReadableDatabase();
        long exerciseId = getExerciseId(exerciseName);
        if (exerciseId == -1) return null; // Exercise does not exist

        List<String> muscles = new ArrayList<>();
        Cursor cursor = db.query(TABLE_MUSCLE,
                new String[]{COLUMN_MUSCLE_NAME},
                COLUMN_EXERCISE_REF_ID + " = ?",
                new String[]{String.valueOf(exerciseId)},
                null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                muscles.add(cursor.getString(0));
            }
            cursor.close();
        }
        return muscles;
    }

    public List<String> getAllExercises() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<String> exercises = new ArrayList<>();
        Cursor cursor = db.query(TABLE_EXERCISE,
                new String[]{COLUMN_EXERCISE_NAME},
                null, null, null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                exercises.add(cursor.getString(0));
            }
            cursor.close();
        }
        return exercises;
    }

    public List<String> getAllMuscleGroups() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<String> muscleGroups = new ArrayList<>();
        Cursor cursor = db.query(TABLE_MUSCLE,
                new String[]{COLUMN_MUSCLE_NAME},
                null, null, null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String muscle = cursor.getString(0);
                if (!muscleGroups.contains(muscle)) {
                    muscleGroups.add(muscle); // Avoid duplicates
                }
            }
            cursor.close();
        }
        return muscleGroups;
    }

    private void insertDefaultExercises(SQLiteDatabase db) {
        addExerciseWithMuscles(db, "Barbell Curl", new String[]{"Biceps Brachii", "Brachialis"});
        addExerciseWithMuscles(db, "Bench Press", new String[]{"Pectoralis Major", "Triceps Brachii", "Anterior Deltoid"});
        addExerciseWithMuscles(db, "Squat", new String[]{"Quadriceps", "Gluteus Maximus", "Hamstrings"});
        addExerciseWithMuscles(db, "Deadlift", new String[]{"Gluteus Maximus", "Hamstrings", "Erector Spinae", "Latissimus Dorsi"});
        addExerciseWithMuscles(db, "Shoulder Press", new String[]{"Deltoids", "Triceps Brachii"});
        addExerciseWithMuscles(db, "Pull Up", new String[]{"Latissimus Dorsi", "Biceps Brachii", "Rhomboids", "Trapezius"});
    }

    private void addExerciseWithMuscles(SQLiteDatabase db, String exerciseName, String[] muscles) {
        // Insert the exercise
        ContentValues exerciseValues = new ContentValues();
        exerciseValues.put(COLUMN_EXERCISE_NAME, exerciseName);
        long exerciseId = db.insert(TABLE_EXERCISE, null, exerciseValues);

        // Insert the muscles associated with the exercise
        if (exerciseId != -1) {
            for (String muscle : muscles) {
                ContentValues muscleValues = new ContentValues();
                muscleValues.put(COLUMN_MUSCLE_NAME, muscle);
                muscleValues.put(COLUMN_EXERCISE_REF_ID, exerciseId);
                db.insert(TABLE_MUSCLE, null, muscleValues);
            }
        }
    }
}
