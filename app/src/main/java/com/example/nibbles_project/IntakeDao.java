package com.example.nibbles_project;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface IntakeDao {
    @Insert
    void insert(Intake intake);

    @Query("SELECT * FROM Intake")
    List<Intake> getAllIntakes();
}