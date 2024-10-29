package com.example.nibbles_project;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

public class login extends AppCompatActivity {

    EditText usernameInput, passwordInput;
    Button loginButton;
    TextView signupText;
    FirebaseFirestore db;

    private static final String SHARED_PREFS = "NibblesPrefs";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameInput = findViewById(R.id.username_input);
        passwordInput = findViewById(R.id.password_input);
        loginButton = findViewById(R.id.login_button);
        signupText = findViewById(R.id.signup_text);

        db = FirebaseFirestore.getInstance(); // Initialize Firestore

        // Check login status and redirect if already logged in
        checkLoginStatus();

        // On login button click
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameInput.getText().toString();
                String password = passwordInput.getText().toString();

                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(login.this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
                } else {
                    validateLogin(username, password);
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

    private void validateLogin(String username, String password) {
        // Check the "Logins" collection for the document with the username
        db.collection("Logins").document(username)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String storedPassword = documentSnapshot.getString("password");
                        if (password.equals(storedPassword)) {
                            // Password matches, login successful
                            Toast.makeText(login.this, "Login Successful", Toast.LENGTH_SHORT).show();

                            // Save login status and username
                            saveLoginData(username);

                            // Redirect to main activity
                            Intent intent = new Intent(login.this, MainActivity.class);
                            startActivity(intent);
                            finish(); // Close login activity
                        } else {
                            // Password does not match
                            Toast.makeText(login.this, "Incorrect Password", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // Username not found
                        Toast.makeText(login.this, "Username not found", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(login.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void saveLoginData(String username) {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Store username and set login status to true
        editor.putString(KEY_USERNAME, username);
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.apply();
    }

    private void checkLoginStatus() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);

        if (isLoggedIn) {
            // User is logged in, skip login screen and go to main activity
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
