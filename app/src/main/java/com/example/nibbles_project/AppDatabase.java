package com.example.nibbles_project;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Intake.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract IntakeDao intakeDao();
}
