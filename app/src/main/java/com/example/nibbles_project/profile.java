package com.example.nibbles_project;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class profile extends AppCompatActivity {

    private EditText inputWeight, inputHeight, inputAge;
    private Spinner inputGender;
    private Switch inputNicotine;
    private Button saveButton;
    private TimePicker timePicker;

    // Define SharedPreferences name and keys
    private static final String SHARED_PREFS = "userPrefs";
    private static final String KEY_WEIGHT = "weight";
    private static final String KEY_HEIGHT = "height";
    private static final String KEY_AGE = "age";
    private static final String KEY_GENDER = "gender";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize input fields and button
        inputWeight = findViewById(R.id.inputWeight);
        inputHeight = findViewById(R.id.inputHeight);
        inputAge = findViewById(R.id.inputAge);
        inputGender = findViewById(R.id.inputGender);
        saveButton = findViewById(R.id.saveButton);
        timePicker = findViewById(R.id.timePicker);

        // Load saved profile data when the activity is opened
        loadProfileData();

        // Save data when the save button is clicked
        saveButton.setOnClickListener(v -> saveProfileData());
    }

    private void saveProfileData() {
        String weight = inputWeight.getText().toString();
        String height = inputHeight.getText().toString();
        String age = inputAge.getText().toString();
        String gender = inputGender.getSelectedItem().toString();
        int hour;
        int minute;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            hour = timePicker.getHour();
            minute = timePicker.getMinute();
        } else {
            hour = timePicker.getCurrentHour(); // For devices running on older versions
            minute = timePicker.getCurrentMinute();
        }
        // Save data to SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(KEY_WEIGHT, weight);
        editor.putString(KEY_HEIGHT, height);
        editor.putString(KEY_AGE, age);
        editor.putString(KEY_GENDER, gender);

        editor.apply();  // Apply the changes

        // Display a confirmation message
        Toast.makeText(this, "Profile Saved", Toast.LENGTH_SHORT).show();

        // Navigate to MainActivity
        Intent intent = new Intent(profile.this, MainActivity.class);
        startActivity(intent);

        // Optionally finish this activity to prevent going back
        finish();
    }

    private void loadProfileData() {
        // Load data from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);

        String weight = sharedPreferences.getString(KEY_WEIGHT, "");
        String height = sharedPreferences.getString(KEY_HEIGHT, "");
        String age = sharedPreferences.getString(KEY_AGE, "");
        String gender = sharedPreferences.getString(KEY_GENDER, "");

        // Set the loaded data to input fields
        inputWeight.setText(weight);
        inputHeight.setText(height);
        inputAge.setText(age);

        // Set the spinner to the correct gender (you may need a helper function for this)
        setSpinnerToValue(inputGender, gender);

    }

    // Helper function to set the Spinner's value (gender) based on the saved data
    private void setSpinnerToValue(Spinner spinner, String value) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equals(value)) {
                spinner.setSelection(i);
                break;
            }
        }
    }
}
