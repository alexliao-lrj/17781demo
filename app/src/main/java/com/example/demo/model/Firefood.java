package com.example.demo.model;

import com.google.firebase.firestore.IgnoreExtraProperties;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

//firestore Food pojo
@IgnoreExtraProperties
public class Firefood {
    //food name
    private String name;
    //total calorie
    private double totalCal;
    //calorie per serving
    private double perCal;
    //how many servings
    private double serving;
    //breakfast1 lunch2 dinner3 snack4
    private int category;
    //datetime
    private @ServerTimestamp Date timestamp;

    public Firefood () {}

    public Firefood(String name, double perCal, double serving, int category){
        this.name = name;
        this.perCal = perCal;
        this.serving = serving;
        this.category = category;
        this.totalCal = perCal * serving;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getTotalCal() {
        return totalCal;
    }

    public void setTotalCal(double totalCal) {
        this.totalCal = totalCal;
    }

    public double getPerCal() {
        return perCal;
    }

    public void setPerCal(double perCal) {
        this.perCal = perCal;
    }

    public double getServing() {
        return serving;
    }

    public void setServing(double serving) {
        this.serving = serving;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
