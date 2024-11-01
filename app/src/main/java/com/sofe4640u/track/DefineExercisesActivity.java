package com.sofe4640u.track;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import java.util.List;

public class DefineExercisesActivity extends AppCompatActivity {

    private ExerciseDatabase exerciseDatabase;
    private AutoCompleteTextView editTextExerciseName;
    private AutoCompleteTextView editTextMuscleGroup;
    private Button addBtn;
    private LinearLayout definedExercisesLayout;
    private TextView navAddNewLog;
    private TextView navDefineExercises;
    private TextView navPastLogs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_define_exercises);

        exerciseDatabase = new ExerciseDatabase(this);
        editTextExerciseName = findViewById(R.id.editTextExerciseName);
        editTextMuscleGroup = findViewById(R.id.editTextMuscleGroup);
        addBtn = findViewById(R.id.addBtn);
        definedExercisesLayout = findViewById(R.id.defined_exercises_layout);

        // Initialize navigation buttons
        navAddNewLog = findViewById(R.id.nav_add_new_log);
        navDefineExercises = findViewById(R.id.nav_define_exercises);
        navPastLogs = findViewById(R.id.nav_past_logs);

        // Set up navigation button actions
        setupNavigation();

        loadSuggestions();
        loadDefinedExercises();

        addBtn.setOnClickListener(view -> {
            String exerciseName = editTextExerciseName.getText().toString().trim();
            String muscleGroup = editTextMuscleGroup.getText().toString().trim();

            if (exerciseName.isEmpty() || muscleGroup.isEmpty()) {
                Toast.makeText(DefineExercisesActivity.this, "Please enter both exercise and muscle group", Toast.LENGTH_SHORT).show();
                return;
            }

            if (exerciseDatabase.getExerciseId(exerciseName) == -1) {
                exerciseDatabase.addExercise(exerciseName);
                Toast.makeText(DefineExercisesActivity.this, "Exercise added", Toast.LENGTH_SHORT).show();
            }

            exerciseDatabase.addMuscle(exerciseName, muscleGroup);
            Toast.makeText(DefineExercisesActivity.this, "Muscle added to exercise", Toast.LENGTH_SHORT).show();

            loadSuggestions();
            loadDefinedExercises();
        });
    }

    private void setupNavigation() {
        navAddNewLog.setOnClickListener(view -> {
            Intent intent = new Intent(DefineExercisesActivity.this, AddNewWorkoutActivity.class);
            startActivity(intent);
        });

        navDefineExercises.setOnClickListener(view -> {
            Intent intent = new Intent(DefineExercisesActivity.this, DefineExercisesActivity.class);
            startActivity(intent);
        });

//        navPastLogs.setOnClickListener(view -> {
//            Intent intent = new Intent(DefineExercises.this, PastLogsActivity.class); // Replace with actual past logs activity if available
//            startActivity(intent);
//        });
    }

    private void loadSuggestions() {
        List<String> exerciseNames = exerciseDatabase.getAllExercises();
        List<String> muscleGroups = exerciseDatabase.getAllMuscleGroups();

        ArrayAdapter<String> exerciseAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, exerciseNames);
        ArrayAdapter<String> muscleAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, muscleGroups);

        editTextExerciseName.setAdapter(exerciseAdapter);
        editTextMuscleGroup.setAdapter(muscleAdapter);
    }

    private void loadDefinedExercises() {
        definedExercisesLayout.removeAllViews();
        List<String> exercises = exerciseDatabase.getAllExercises();

        for (String exercise : exercises) {
            List<String> muscles = exerciseDatabase.getMusclesForExercise(exercise);
            View itemView = getLayoutInflater().inflate(R.layout.defined_exercise_list_item, definedExercisesLayout, false);
            TextView exerciseNameView = itemView.findViewById(R.id.exerciseName);
            TextView muscleListView = itemView.findViewById(R.id.muscleList);

            exerciseNameView.setText(exercise);
            muscleListView.setText(muscles != null ? String.join(", ", muscles) : "");

            definedExercisesLayout.addView(itemView);
        }
    }
}
