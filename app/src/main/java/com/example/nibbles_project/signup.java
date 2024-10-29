package com.example.nibbles_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class signup extends AppCompatActivity {

    EditText nameInput, emailInput, passwordInput;
    Button signupButton;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        nameInput = findViewById(R.id.name_input);
        passwordInput = findViewById(R.id.password_input_signup);
        signupButton = findViewById(R.id.signup_button);

        db = FirebaseFirestore.getInstance(); // Initialize Firestore

        // On signup button click, save user data in Firestore and navigate to login
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameInput.getText().toString();
                String password = passwordInput.getText().toString();
                if (name.isEmpty() || password.isEmpty()) {
                    Toast.makeText(signup.this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
                } else {
                    // Save the user data to Firestore
                    saveUserData(name,password);
                }
            }
        });
    }

    private void saveUserData(String username, String password) {
        // Create a user map to store in Firestore
        Map<String, Object> user = new HashMap<>();
        user.put("username", username);
        user.put("password", password); // Save password directly (consider hashing for security)

        // Store user data under a document ID corresponding to the username
        db.collection("Logins").document(username) // Change collection to "Logins"
                .set(user)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(signup.this, "Sign Up Successful", Toast.LENGTH_SHORT).show();
                    // Navigate to login screen after signup
                    Intent intent = new Intent(signup.this, login.class);
                    startActivity(intent);
                    finish(); // Close the signup activity
                })
                .addOnFailureListener(e -> Toast.makeText(signup.this, "Sign Up Failed", Toast.LENGTH_SHORT).show());
    }
}
