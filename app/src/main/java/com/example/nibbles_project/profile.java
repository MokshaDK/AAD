package com.example.nibbles_project;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class profile extends AppCompatActivity {
    // Define SharedPreferences name and keys
    private static final String SHARED_PREFS = "NibblesPrefs";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_WEIGHT = "weight";
    private static final String KEY_HEIGHT = "height";
    private static final String KEY_AGE = "age";
    private static final String KEY_GENDER = "gender";
    private static final String KEY_DOCTOR_NUMBER = "doctor_number";

    private EditText inputWeight, inputHeight, inputAge, doctorNumber;
    private Spinner inputGender;
    private Button saveButton, callButton, logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize input fields and buttons
        inputWeight = findViewById(R.id.inputWeight);
        inputHeight = findViewById(R.id.inputHeight);
        inputAge = findViewById(R.id.inputAge);
        inputGender = findViewById(R.id.inputGender);
        doctorNumber = findViewById(R.id.doctorNumber);
        saveButton = findViewById(R.id.saveButton);
        callButton = findViewById(R.id.callButton);
        logoutButton = findViewById(R.id.logoutButton); // Initialize the logout button

        // Check if user is logged in
        checkLoginStatus();

        // Load saved profile data when the activity is opened
        loadProfileData();

        // Set OnClickListener on the call button
        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makePhoneCall();
            }
        });

        // Save data when the save button is clicked
        saveButton.setOnClickListener(v -> saveProfileData());

        // Set OnClickListener on the logout button
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
    }

    private void checkLoginStatus() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);

        if (!isLoggedIn) {
            // If not logged in, redirect to login page
            Intent intent = new Intent(profile.this, login.class);
            startActivity(intent);
            finish();
        }
    }

    private void saveProfileData() {
        String weight = inputWeight.getText().toString();
        String height = inputHeight.getText().toString();
        String age = inputAge.getText().toString();
        String gender = inputGender.getSelectedItem().toString();
        String number = doctorNumber.getText().toString();

        // Save data to SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(KEY_WEIGHT, weight);
        editor.putString(KEY_HEIGHT, height);
        editor.putString(KEY_AGE, age);
        editor.putString(KEY_GENDER, gender);
        editor.putString(KEY_DOCTOR_NUMBER, number);
        editor.apply();

        // Display a confirmation message
        Toast.makeText(this, "Profile Saved", Toast.LENGTH_SHORT).show();

        // Navigate to MainActivity
        Intent intent = new Intent(profile.this, MainActivity.class);
        startActivity(intent);
    }

    private void loadProfileData() {
        // Load data from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);

        String weight = sharedPreferences.getString(KEY_WEIGHT, "");
        String height = sharedPreferences.getString(KEY_HEIGHT, "");
        String age = sharedPreferences.getString(KEY_AGE, "");
        String gender = sharedPreferences.getString(KEY_GENDER, "");
        String number = sharedPreferences.getString(KEY_DOCTOR_NUMBER, "");

        // Set the loaded data to input fields
        inputWeight.setText(weight);
        inputHeight.setText(height);
        inputAge.setText(age);
        doctorNumber.setText(number);

        // Set the spinner to the correct gender
        setSpinnerToValue(inputGender, gender);
    }

    private void setSpinnerToValue(Spinner spinner, String value) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equals(value)) {
                spinner.setSelection(i);
                break;
            }
        }
    }

    private void makePhoneCall() {
        String number = doctorNumber.getText().toString();

        // Check if the number is not empty
        if (number.trim().length() > 0) {
            // Create an Intent to dial the number (opens the dialer)
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + number));
            startActivity(intent);
        } else {
            // Show a message if the number is empty
            Toast.makeText(profile.this, "Enter a valid phone number", Toast.LENGTH_SHORT).show();
        }
    }

    private void logout() {
        // Update SharedPreferences to log the user out
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_IS_LOGGED_IN, false);
        editor.remove(KEY_USERNAME); // Optionally clear username if no longer needed
        editor.apply();

        // Redirect to login activity
        Intent intent = new Intent(profile.this, login.class);
        startActivity(intent);
        finish(); // Close profile activity
    }
}
