package com.sofe4640u.track;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.sofe4640u.track.Exercise;
import com.sofe4640u.track.R;
import com.sofe4640u.track.WorkoutsDatabase;

import java.util.ArrayList;
import java.util.List;

public class AddNewWorkoutActivity extends AppCompatActivity {

    private LinearLayout exerciseListLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_workout);

        exerciseListLayout = findViewById(R.id.exerciseListLayout);

        // Load exercise data (replace with actual data source)
        loadExercises();
    }

    private void loadExercises() {
        List<Exercise> exercises = getExercises(); // Replace with actual method to fetch data

        for (Exercise exercise : exercises) {
            View exerciseView = LayoutInflater.from(this).inflate(R.layout.add_workout_list_item, exerciseListLayout, false);

            TextView exerciseTitle = exerciseView.findViewById(R.id.exerciseTitle);
            TextView repsValue = exerciseView.findViewById(R.id.repsValue);
            TextView weightValue = exerciseView.findViewById(R.id.weightValue);
            TextView timestampValue = exerciseView.findViewById(R.id.timestampValue);

            exerciseTitle.setText(exercise.getName());
            repsValue.setText(exercise.getReps() + " reps");
            weightValue.setText(exercise.getWeight() + " lbs");
            timestampValue.setText(exercise.getTimestamp());

            exerciseListLayout.addView(exerciseView);
        }
    }

    private List<Exercise> getExercises() {
        List<Exercise> exercises = new ArrayList<>();
        WorkoutsDatabase db = new WorkoutsDatabase(this);
        Cursor cursor = db.getWorkoutLogs("username", 1); // Replace "username" and "1" with dynamic data as needed

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String name = "Exercise Name"; // Modify as per your design
                int reps = cursor.getInt(cursor.getColumnIndexOrThrow("reps"));
                double weight = cursor.getDouble(cursor.getColumnIndexOrThrow("weight"));
                String timestamp = cursor.getString(cursor.getColumnIndexOrThrow("timestamp"));

                exercises.add(new Exercise(name, reps, weight, timestamp));
            }
            cursor.close();
        }
        return exercises;
    }

}
