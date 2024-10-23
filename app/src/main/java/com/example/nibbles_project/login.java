package com.example.nibbles_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class login extends AppCompatActivity {

    EditText usernameInput, passwordInput;
    Button loginButton;
    TextView signupText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameInput = findViewById(R.id.username_input);
        passwordInput = findViewById(R.id.password_input);
        loginButton = findViewById(R.id.login_button);
        signupText = findViewById(R.id.signup_text);

        // On login button click
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Here you should add validation logic and check if the login details are correct
                String username = usernameInput.getText().toString();
                String password = passwordInput.getText().toString();
                if (validateLogin(username, password)) {
                    Toast.makeText(login.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    // Navigate to the home screen or wherever
                    Intent intent = new Intent(login.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(login.this, "Invalid login. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // On sign up text click, navigate to SignupActivity
        signupText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(login.this, signup.class);
                startActivity(intent);
            }
        });
    }

    private boolean validateLogin(String username, String password) {
        // Placeholder for login validation logic
        // Replace this with actual validation, e.g., checking against a database
        return username.equals("admin") && password.equals("password");
    }
}
