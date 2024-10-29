package com.example.nibbles_project;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private static final int REQUEST_CODE = 100;
    private SensorManager sensorManager;
    private Sensor stepCounterSensor;
    private TextView stepCountText;
    private ProgressBar stepProgressBar;
    private Button updateProfileButton;
    private Button nutritionGuidelinesButton;
    private Button dailySaveButton;
    private Button dailyTrackButton;
    private EditText nicotineExposureEditText;
    private EditText proteinIntakeEditText;
    private EditText caloriesIntakeEditText;
    private EditText waterIntakeEditText;
    private int totalSteps = 0;

    private static final String SHARED_PREFS = "NibblesPrefs";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_USERNAME = "username"; // Added for storing the username

    private SQLiteDatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkLoginStatus();

        // Initialize the SQLite database helper
        databaseHelper = new SQLiteDatabaseHelper(this);

        stepCountText = findViewById(R.id.stepCountText);
        stepProgressBar = findViewById(R.id.stepProgressBar);
        nicotineExposureEditText = findViewById(R.id.nicotineExposure);
        proteinIntakeEditText = findViewById(R.id.proteinIntake);
        caloriesIntakeEditText = findViewById(R.id.caloriesIntake);
        waterIntakeEditText = findViewById(R.id.waterIntake);

        // Initialize buttons
        dailySaveButton = findViewById(R.id.dailySave);
        dailyTrackButton = findViewById(R.id.dailyTrack);
        updateProfileButton = findViewById(R.id.updateProfile);
        nutritionGuidelinesButton = findViewById(R.id.nutritionGuidelines);

        // Request permission for activity recognition
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, 1);
        } else {
            initializeStepCounter();
        }

        loadProfileDataAndCalculateBMI();

        dailySaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDailyIntake();
            }
        });

        dailyTrackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trackDailyIntake();
            }
        });

        updateProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, profile.class);
                startActivity(intent);
            }
        });

        nutritionGuidelinesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, VideosActivity.class);
                startActivity(intent); // Start the profile activity
            }
        });
    }

    private void initializeStepCounter() {
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        if (stepCounterSensor != null) {
            sensorManager.registerListener(this, stepCounterSensor, SensorManager.SENSOR_DELAY_UI);
        } else {
            Toast.makeText(this, "Step counter sensor not available", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        totalSteps = (int) event.values[0];  // Get the step count
        stepCountText.setText("Steps: " + totalSteps);

        // Set progress based on step count
        stepProgressBar.setProgress(totalSteps);

        // Ensure steps do not exceed the max
        if (totalSteps > stepProgressBar.getMax()) {
            stepProgressBar.setProgress(stepProgressBar.getMax());
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Not used
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initializeStepCounter();
    }

    private void loadProfileDataAndCalculateBMI() {
        // Retrieve saved weight and height from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        String weightStr = sharedPreferences.getString("weight", "0");
        String heightStr = sharedPreferences.getString("height", "0");

        float weight = Float.parseFloat(weightStr);
        float height = Float.parseFloat(heightStr);

        float bmi = (height != 0) ? (weight / (height * height)) : 0;
        TextView bmiTextView = findViewById(R.id.bmiTextView);
        bmiTextView.setText(String.format("BMI: %.2f", bmi));

        updateBMIProgressBar(bmi);
    }

    private void updateBMIProgressBar(float bmi) {
        ProgressBar bmiProgressBar = findViewById(R.id.bmiProgressBar);
        bmiProgressBar.setMax(40);
        bmiProgressBar.setProgress((int) bmi);

        if (bmi < 18.5) {
            bmiProgressBar.setProgressTintList(getResources().getColorStateList(R.color.orange));
        } else if (bmi >= 18.5 && bmi < 25) {
            bmiProgressBar.setProgressTintList(getResources().getColorStateList(R.color.green));
        } else if (bmi >= 25 && bmi < 30) {
            bmiProgressBar.setProgressTintList(getResources().getColorStateList(R.color.orange));
        } else {
            bmiProgressBar.setProgressTintList(getResources().getColorStateList(R.color.red));
        }
    }

    private void saveDailyIntake() {
        String nicotine = nicotineExposureEditText.getText().toString();
        String protein = proteinIntakeEditText.getText().toString();
        String calories = caloriesIntakeEditText.getText().toString();
        String water = waterIntakeEditText.getText().toString();

        if (nicotine.isEmpty() || protein.isEmpty() || calories.isEmpty() || water.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get the signed-in user's username
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        String currentUsername = sharedPreferences.getString(KEY_USERNAME, null);

        // Save daily intake with the username
        boolean isInserted = databaseHelper.insertDailyIntake(currentUsername, nicotine, protein, calories, water);
        if (isInserted) {
            Toast.makeText(this, "Data saved successfully", Toast.LENGTH_SHORT).show();
            // Clear the input fields after saving
            nicotineExposureEditText.setText("");
            proteinIntakeEditText.setText("");
            caloriesIntakeEditText.setText("");
            waterIntakeEditText.setText("");
        } else {
            Toast.makeText(this, "Failed to save data", Toast.LENGTH_SHORT).show();
        }
    }

    private void trackDailyIntake() {
        // Get the signed-in user's username
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        String currentUsername = sharedPreferences.getString(KEY_USERNAME, null);

        // Get daily intakes for the current user
        List<DailyIntake> dailyIntakes = databaseHelper.getDailyIntakesByUsername(currentUsername);
        StringBuilder intakeSummary = new StringBuilder("Daily Intake for " + currentUsername + ":\n");

        if (dailyIntakes.isEmpty()) {
            intakeSummary.append("No records found for today.");
        } else {
            for (DailyIntake intake : dailyIntakes) {
                intakeSummary.append("Nicotine: ").append(intake.getNicotine())
                        .append(", Protein: ").append(intake.getProtein())
                        .append(", Calories: ").append(intake.getCalories())
                        .append(", Water: ").append(intake.getWater()).append("\n");
            }
        }

        // Update the TextView to show the summary
        TextView intakeSummaryTextView = findViewById(R.id.intakeSummaryTextView);
        intakeSummaryTextView.setText(intakeSummary.toString());
        intakeSummaryTextView.setVisibility(View.VISIBLE); // Show the TextView
    }

    private void checkLoginStatus() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);

        if (!isLoggedIn) {
            // Redirect to login activity
            Intent intent = new Intent(this, login.class);
            startActivity(intent);
            finish(); // Close the MainActivity
        }
    }
}
