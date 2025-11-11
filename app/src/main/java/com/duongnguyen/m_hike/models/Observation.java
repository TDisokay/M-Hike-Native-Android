package com.duongnguyen.m_hike.models;

public class Observation {
    private long id;
    private String observation;
    private String observationTime;
    private String comments;
    private long hikeId;

    // Constructors
    public Observation() {}

    public Observation(String observation, String observationTime, String comments, long hikeId) {
        this.observation = observation;
        this.observationTime = observationTime;
        this.comments = comments;
        this.hikeId = hikeId;
    }

    // Getters and Setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getObservation() { return observation; }
    public void setObservation(String observation) { this.observation = observation; }

    public String getObservationTime() { return observationTime; }
    public void setObservationTime(String observationTime) { this.observationTime = observationTime; }

    public String getComments() { return comments; }
    public void setComments(String comments) { this.comments = comments; }

    public long getHikeId() { return hikeId; }
    public void setHikeId(long hikeId) { this.hikeId = hikeId; }
}
