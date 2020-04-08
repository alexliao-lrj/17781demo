package com.example.demo.model;

import com.google.firebase.firestore.IgnoreExtraProperties;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

//firestore wellness profile pojo
@IgnoreExtraProperties
public class Wellness {
    private double weight;
    private double height;
    private double calorieIntake;
    private double calorieBurn;
    //datetime
    private @ServerTimestamp Date timestamp;

    public Wellness(){}

    public Wellness(double weight, double height, double calorieIntake, double calorieBurn) {
        this.weight = weight;
        this.height = height;
        this.calorieIntake = calorieIntake;
        this.calorieBurn = calorieBurn;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getCalorieIntake() {
        return calorieIntake;
    }

    public void setCalorieIntake(double calorieIntake) {
        this.calorieIntake = calorieIntake;
    }

    public double getCalorieBurn() {
        return calorieBurn;
    }

    public void setCalorieBurn(double calorieBurn) {
        this.calorieBurn = calorieBurn;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
