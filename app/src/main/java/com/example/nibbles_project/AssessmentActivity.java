package com.example.nibbles_project;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import java.util.Calendar;

public class AssessmentActivity extends AppCompatActivity {

    private EditText satisfactionInput, improvementInput, progressInput;
    private Button submitButton;
    private static final String SHARED_PREFS = "NibblesPrefs";
    private static final String KEY_LAST_ASSESSMENT_DATE = "lastAssessmentDate";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn"; // Key to check login status

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check if the user is logged in
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);

        if (!isLoggedIn) {
            // Redirect to login if the user is not logged in
            Intent intent = new Intent(AssessmentActivity.this, login.class);
            startActivity(intent);
            finish(); // Close AssessmentActivity
            return;
        }

        setContentView(R.layout.activity_assessment);

        satisfactionInput = findViewById(R.id.satisfaction_input);
        improvementInput = findViewById(R.id.improvement_input);
        progressInput = findViewById(R.id.progress_input);
        submitButton = findViewById(R.id.submit_button);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitAssessment();
            }
        });
    }

    /*private void checkAssessmentInterval() {
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH) + 1; // Months are 0-based in Calendar

        /*if (month % 3 == 0) {
            findViewById(R.id.assessmentForm).setVisibility(VISIBLE);
        } else {
            findViewById(R.id.assessmentForm).setVisibility(GONE);
        }
    }*/

    private void updateLastAssessmentDate() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(KEY_LAST_ASSESSMENT_DATE, Calendar.getInstance().getTimeInMillis());
        editor.apply();
    }

    private void submitAssessment() {
        String satisfaction = satisfactionInput.getText().toString();
        String improvements = improvementInput.getText().toString();
        String progress = progressInput.getText().toString();

        if (satisfaction.isEmpty() || improvements.isEmpty() || progress.isEmpty()) {
            Toast.makeText(AssessmentActivity.this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(AssessmentActivity.this, "Assessment submitted!", Toast.LENGTH_SHORT).show();
            new Thread(() -> {
                runOnUiThread(() -> {
                    updateLastAssessmentDate();
                });
            }).start();

            // Clear inputs or navigate back if needed
            Intent intent = new Intent(AssessmentActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
