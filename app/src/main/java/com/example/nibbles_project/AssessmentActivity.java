package com.example.nibbles_project;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;
import java.util.Calendar;

public class AssessmentActivity extends AppCompatActivity {
    private EditText nicotineInput, proteinInput, calorieInput, waterInput;
    private Button saveButton;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment);

        nicotineInput = findViewById(R.id.nicotineInput);
        proteinInput = findViewById(R.id.proteinInput);
        calorieInput = findViewById(R.id.calorieInput);
        waterInput = findViewById(R.id.waterInput);
        saveButton = findViewById(R.id.saveButton);

        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "intake-database").build();

        saveButton.setOnClickListener(v -> saveData());

        checkAssessmentInterval();
    }

    private void checkAssessmentInterval() {
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH) + 1; // Months are 0-based in Calendar

        if (month % 3 == 0) {
            findViewById(R.id.assessmentForm).setVisibility(TextView.VISIBLE);
        } else {
            findViewById(R.id.assessmentForm).setVisibility(TextView.GONE);
        }
    }

    private void saveData() {
        int nicotine = Integer.parseInt(nicotineInput.getText().toString());
        int protein = Integer.parseInt(proteinInput.getText().toString());
        int calorie = Integer.parseInt(calorieInput.getText().toString());
        int water = Integer.parseInt(waterInput.getText().toString());

        Intake intake = new Intake();
        intake.nicotine = nicotine;
        intake.protein = protein;
        intake.calorie = calorie;
        intake.water = water;

        new Thread(() -> {
            db.intakeDao().insert(intake);
            runOnUiThread(() -> Toast.makeText(this, "Data saved successfully", Toast.LENGTH_SHORT).show());
        }).start();
    }
}