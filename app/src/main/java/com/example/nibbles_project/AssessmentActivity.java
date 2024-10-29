package com.example.nibbles_project;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;


public class AssessmentActivity extends AppCompatActivity {

    private EditText satisfactionInput, improvementInput, progressInput;
    private Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

    private void submitAssessment() {
        String satisfaction = satisfactionInput.getText().toString();
        String improvements = improvementInput.getText().toString();
        String progress = progressInput.getText().toString();

        // Here you could save this data to Firestore or handle it as needed
        if (satisfaction.isEmpty() || improvements.isEmpty() || progress.isEmpty()) {
            Toast.makeText(AssessmentActivity.this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(AssessmentActivity.this, "Assessment submitted!", Toast.LENGTH_SHORT).show();
            // Clear inputs or navigate back if needed
            // Create an intent to navigate back to MainActivity
            Intent intent = new Intent(AssessmentActivity.this, MainActivity.class);
            startActivity(intent);

            // Finish the current activity to remove it from the back stack
            finish();
        }
    }
}
