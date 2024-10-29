package com.example.nibbles_project;

public class DailyIntake {
    private int id;
    private String username; // Add username field
    private String nicotine;
    private String protein;
    private String calories;
    private String water;

    public DailyIntake(int id, String username, String nicotine, String protein, String calories, String water) {
        this.id = id;
        this.username = username; // Initialize username
        this.nicotine = nicotine;
        this.protein = protein;
        this.calories = calories;
        this.water = water;
    }

    public int getId() {
        return id;
    }

    public String getUsername() { // Getter for username
        return username;
    }

    public String getNicotine() {
        return nicotine;
    }

    public String getProtein() {
        return protein;
    }

    public String getCalories() {
        return calories;
    }

    public String getWater() {
        return water;
    }
}
