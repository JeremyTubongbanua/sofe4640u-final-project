package com.sofe4640u.track;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private AuthDatabase authDatabase;
    private EditText editTextUsername;
    private EditText editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        authDatabase = new AuthDatabase(this);
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        Button loginBtn = findViewById(R.id.loginBtn);

        loginBtn.setOnClickListener(view -> {
            String username = editTextUsername.getText().toString();
            String password = editTextPassword.getText().toString();

            if (authDatabase.validateUser(username, password)) {
                Toast.makeText(MainActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
                // Navigate to the next activity
                // startActivity(new Intent(MainActivity.this, HomePageActivity.class));
            } else {
                Toast.makeText(MainActivity.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
            }
        });

        Button signUpBtn = findViewById(R.id.signUpBtn);
        signUpBtn.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, SignUpActivity.class));
        });
    }
}
