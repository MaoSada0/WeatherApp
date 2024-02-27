package com.example.services;

public class Day {

    String condition;

    int maxTemperature;
    int minTemperature;

    int DayInWeek;

    public Day(String condition, int maxTemperature, int minTemperature, int DayInWeek) {
        this.condition = condition;
        this.maxTemperature = maxTemperature;
        this.minTemperature = minTemperature;
        this.DayInWeek = DayInWeek;
    }

    public String getCondition() {
        return condition;
    }

    public int getDayInWeek() {
        return DayInWeek;
    }

    public int getMaxTemperature() {
        return maxTemperature;
    }

    public int getMinTemperature() {
        return minTemperature;
    }

    @Override
    public String toString() {
        return "Day{" +
                "condition='" + condition + '\'' +
                ", maxTemperature=" + maxTemperature +
                ", minTemperature=" + minTemperature +
                ", countOfDay=" + DayInWeek +
                '}';
    }
}
