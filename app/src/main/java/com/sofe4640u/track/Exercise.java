package com.sofe4640u.track;

public class Exercise {
    private String name;
    private int reps;
    private double weight;
    private String timestamp;

    public Exercise(String name, int reps, double weight, String timestamp) {
        this.name = name;
        this.reps = reps;
        this.weight = weight;
        this.timestamp = timestamp;
    }

    public String getName() {
        return name;
    }

    public int getReps() {
        return reps;
    }

    public double getWeight() {
        return weight;
    }

    public String getTimestamp() {
        return timestamp;
    }
}
