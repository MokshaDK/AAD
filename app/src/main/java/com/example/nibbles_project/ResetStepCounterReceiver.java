package com.example.nibbles_project;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class ResetStepCounterReceiver extends BroadcastReceiver {
    private static final String SHARED_PREFS = "userPrefs";
    private static final String KEY_STEP_COUNT = "stepCount";

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_STEP_COUNT, 0);
        editor.apply();
    }
}