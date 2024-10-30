package com.example.nibbles_project;
import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class signup extends AppCompatActivity {

    EditText nameInput, passwordInput;
    Button signupButton;
    FirebaseFirestore db;
    private static final String CHANNEL_ID = "signup_channel"; // Notification channel ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        nameInput = findViewById(R.id.name_input);
        passwordInput = findViewById(R.id.password_input_signup);
        signupButton = findViewById(R.id.signup_button);

        db = FirebaseFirestore.getInstance(); // Initialize Firestore
        createNotificationChannel(); // Create the notification channel

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
                    saveUserData(name, password);
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
        db.collection("Logins").document(username)
                .set(user)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(signup.this, "Sign Up Successful", Toast.LENGTH_SHORT).show();

                    // Set up notification
                    String channelId = "your_channel_id"; // Use the channel ID you created earlier
                    Intent intent = new Intent(signup.this, login.class); // Change to the desired activity
                    PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

                    NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                            .setSmallIcon(R.drawable.ic_launcher) // Use your notification icon
                            .setContentTitle("Welcome!")
                            .setContentText("Thank you for signing up!")
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                            .setContentIntent(pendingIntent)
                            .setAutoCancel(true); // Automatically remove the notification when clicked

                    // Check for notification permission before sending the notification
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // API 33 and above
                        if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
                            notificationManager.notify(1001, builder.build());
                        } else {
                            // Handle the case where permission is not granted
                            Toast.makeText(this, "Notification permission is required", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // For API levels below 33, send the notification directly
                        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
                        notificationManager.notify(1001, builder.build());
                    }

                    // Navigate to login screen after signup
                    Intent intentLogin = new Intent(signup.this, login.class);
                    startActivity(intentLogin);
                    finish(); // Close the signup activity
                })
                .addOnFailureListener(e -> Toast.makeText(signup.this, "Sign Up Failed", Toast.LENGTH_SHORT).show());
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Sign Up Notifications";
            String description = "Channel for sign up notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}