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
    private static final String SHARED_PREFS = "userPrefs";
    private static final String KEY_WEIGHT = "weight";
    private static final String KEY_HEIGHT = "height";
    private static final String KEY_AGE = "age";
    private static final String KEY_GENDER = "gender";
    private static final String KEY_DOCTOR_NUMBER = "doctor_number"; // Key for doctor's number

    private EditText inputWeight, inputHeight, inputAge, doctorNumber;
    private Spinner inputGender;
    private Button saveButton, callButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize input fields and button
        inputWeight = findViewById(R.id.inputWeight);
        inputHeight = findViewById(R.id.inputHeight);
        inputAge = findViewById(R.id.inputAge);
        inputGender = findViewById(R.id.inputGender);
        doctorNumber = findViewById(R.id.doctorNumber); // Initialize doctor number field
        saveButton = findViewById(R.id.saveButton);
        callButton = findViewById(R.id.callButton);

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
    }

    private void saveProfileData() {
        String weight = inputWeight.getText().toString();
        String height = inputHeight.getText().toString();
        String age = inputAge.getText().toString();
        String gender = inputGender.getSelectedItem().toString();
        String number = doctorNumber.getText().toString(); // Get the doctor number

        // Save data to SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(KEY_WEIGHT, weight);
        editor.putString(KEY_HEIGHT, height);
        editor.putString(KEY_AGE, age);
        editor.putString(KEY_GENDER, gender);
        editor.putString(KEY_DOCTOR_NUMBER, number); // Save the doctor number
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
        String number = sharedPreferences.getString(KEY_DOCTOR_NUMBER, ""); // Load the doctor number

        // Set the loaded data to input fields
        inputWeight.setText(weight);
        inputHeight.setText(height);
        inputAge.setText(age);
        doctorNumber.setText(number); // Set the loaded doctor number

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
}
