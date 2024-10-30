package com.example.nibbles_project;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import androidx.core.app.NotificationCompat;

public class NotificationReceiver extends BroadcastReceiver {
    private static final String CHANNEL_ID = "signup_channel"; // Same as your notification channel ID

    @Override
    public void onReceive(Context context, Intent intent) {
        String username = intent.getStringExtra("username");

        // Create the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher) // Your notification icon
                .setContentTitle("Assessment Reminder")
                .setContentText("It's time to fill out your assessment form!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        // Intent to open the profile activity when notification is clicked
        Intent activityIntent = new Intent(context, AssessmentActivity.class);
        activityIntent.putExtra("username", username); // Pass username if needed
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, activityIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        builder.setContentIntent(pendingIntent);

        // Notify the user
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify((int) System.currentTimeMillis(), builder.build()); // Unique notification ID
    }
}
