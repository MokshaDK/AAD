package com.example.nibbles_project;

import android.Manifest;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.content.Context;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private static final int REQUEST_CODE = 100;
    private SensorManager sensorManager;
    private Sensor stepCounterSensor;
    private TextView stepCountText;
    private ProgressBar stepProgressBar;
    private Button updateProfileButton;
    private Button nutritionGuidelinesButton;
    private int totalSteps = 0;
    private static final String SHARED_PREFS = "userPrefs";
    private static final String KEY_WEIGHT = "weight";
    private static final String KEY_HEIGHT = "height";

    private static final String CHANNEL_ID = "channel_id";
    private static final int NOTIFICATION_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        stepCountText = findViewById(R.id.stepCountText);
        stepProgressBar = findViewById(R.id.stepProgressBar);

        // Request permission for activity recognition
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, 1);
        } else {
            initializeStepCounter();
        }

        loadProfileDataAndCalculateBMI();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, REQUEST_CODE);
            }
        }

        updateProfileButton = findViewById(R.id.updateProfile);
        nutritionGuidelinesButton = findViewById(R.id.nutritionGuidelines);
        nutritionGuidelinesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, VideosActivity.class);
                startActivity(intent); // Start the profile activity
            }
        });

        // Set OnClickListener for the button
        updateProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to start the profile activity
                Intent intent = new Intent(MainActivity.this, profile.class);
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

        // Optionally, if you want a limit, make sure steps do not exceed the max
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
        String weightStr = sharedPreferences.getString(KEY_WEIGHT, "0");
        String heightStr = sharedPreferences.getString(KEY_HEIGHT, "0");

        // Convert the weight and height from String to float (if they are available)
        float weight = Float.parseFloat(weightStr);
        float height = Float.parseFloat(heightStr);

        // Calculate BMI
        float bmi = (height != 0) ? (weight / (height * height)) : 0;

        // Update the TextView to display the BMI
        TextView bmiTextView=findViewById(R.id.bmiTextView);
        bmiTextView.setText(String.format("BMI: %.2f", bmi));

        // Update the ProgressBar and its color based on the BMI
        updateBMIProgressBar(bmi);
    }
    private void updateBMIProgressBar(float bmi) {
        // Assuming a BMI range of 0 - 40 for progress bar
        ProgressBar bmiProgressBar=findViewById(R.id.bmiProgressBar);
        bmiProgressBar.setMax(40);
        bmiProgressBar.setProgress((int) bmi);

        // Change the color of the ProgressBar based on BMI range
        if (bmi < 18.5) {
            bmiProgressBar.setProgressTintList(getResources().getColorStateList(R.color.orange)); // Underweight
        } else if (bmi >= 18.5 && bmi < 25) {
            bmiProgressBar.setProgressTintList(getResources().getColorStateList(R.color.green)); // Healthy
        } else if (bmi >= 25 && bmi < 30) {
            bmiProgressBar.setProgressTintList(getResources().getColorStateList(R.color.orange)); // Overweight
        } else {
            bmiProgressBar.setProgressTintList(getResources().getColorStateList(R.color.red)); // Obese
        }
    }
}