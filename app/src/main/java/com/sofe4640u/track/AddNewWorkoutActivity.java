package com.sofe4640u.track;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;

public class AddNewWorkoutActivity extends AppCompatActivity {

    private LinearLayout exerciseListLayout;
    private Button backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_workout);

        // Initialize back button and set click listener
        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(v -> finish());

        // Initialize the layout to hold exercise components
        exerciseListLayout = findViewById(R.id.exerciseListLayout);

        // Inflate and add Workout View Component
        View workoutView = LayoutInflater.from(this).inflate(R.layout.workout_view_component, exerciseListLayout, false);
        Button addRepsButton = workoutView.findViewById(R.id.addRepsButton);
        addRepsButton.setOnClickListener(v -> {
            // Logic to add reps here
        });
        exerciseListLayout.addView(workoutView);

        // Inflate and add New Workout Component
        View addWorkoutView = LayoutInflater.from(this).inflate(R.layout.add_workout_component, exerciseListLayout, false);
        Button addWorkoutButton = addWorkoutView.findViewById(R.id.addWorkoutButton);
        addWorkoutButton.setOnClickListener(v -> {
            // Logic to add a new workout here
        });
        exerciseListLayout.addView(addWorkoutView);
    }
}
