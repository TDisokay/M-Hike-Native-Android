package com.duongnguyen.m_hike.models;

import java.io.Serializable;

public class Hike implements Serializable {
    private long id;
    private String name;
    private String location;
    private String hikeDate;
    private String parkingAvailable;
    private double length;
    private String difficulty;
    private String description;

    // Constructors
    public Hike() {}

    public Hike(String name, String location, String hikeDate, String parkingAvailable, double length, String difficulty, String description) {
        this.name = name;
        this.location = location;
        this.hikeDate = hikeDate;
        this.parkingAvailable = parkingAvailable;
        this.length = length;
        this.difficulty = difficulty;
        this.description = description;
    }

    // Getters and Setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getHikeDate() { return hikeDate; }
    public void setHikeDate(String hikeDate) { this.hikeDate = hikeDate; }

    public String getParkingAvailable() { return parkingAvailable; }
    public void setParkingAvailable(String parkingAvailable) { this.parkingAvailable = parkingAvailable; }

    public double getLength() { return length; }
    public void setLength(double length) { this.length = length; }

    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
