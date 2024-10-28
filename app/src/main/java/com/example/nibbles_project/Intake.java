package com.example.nibbles_project;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Intake {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public int nicotine;
    public int protein;
    public int calorie;
    public int water;
}
