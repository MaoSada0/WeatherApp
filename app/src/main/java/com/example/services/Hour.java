package com.example.services;

public class Hour {
    int temperature;
    String condition;
    int timeInMinutes;

    public Hour(int temperature, String condition, int hours) {
        this.temperature = temperature;
        this.condition = condition;
        this.timeInMinutes = hours * 60;
    }

    public int getTemperature() {
        return temperature;
    }

    public String getCondition() {
        return condition;
    }

    public int getTimeInMinutes() {
        return timeInMinutes;
    }
}
