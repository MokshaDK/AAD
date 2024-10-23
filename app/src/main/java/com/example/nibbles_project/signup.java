package com.example.nibbles_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;

public class signup extends AppCompatActivity {

    EditText nameInput, emailInput, passwordInput;
    Button signupButton;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        nameInput = findViewById(R.id.name_input);
        emailInput = findViewById(R.id.email_input);
        passwordInput = findViewById(R.id.password_input_signup);
        signupButton = findViewById(R.id.signup_button);

        sharedPreferences = getSharedPreferences("UserProfile", MODE_PRIVATE);

        // On signup button click, save user data and navigate to ProfileActivity
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameInput.getText().toString();
                String email = emailInput.getText().toString();
                String password = passwordInput.getText().toString();

                if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(signup.this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
                } else {
                    // Save the user profile data using SharedPreferences
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("name", name);
                    editor.putString("email", email);
                    editor.putString("password", password);
                    editor.apply();

                    Toast.makeText(signup.this, "Sign Up Successful", Toast.LENGTH_SHORT).show();

                    // Navigate to ProfileActivity to edit profile if needed
                    Intent intent = new Intent(signup.this, login.class);
                    startActivity(intent);
                }
            }
        });
    }
}
