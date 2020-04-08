package com.example.demo.model;

import com.google.firebase.firestore.IgnoreExtraProperties;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

//firstore sport pojo
@IgnoreExtraProperties
public class Firesport {
    //sport name
    private String name;
    //total calorie burn
    private double totalCal;
    //datetime
    private @ServerTimestamp Date timestamp;

    public Firesport(){}

    public Firesport(String name, double totalCal) {
        this.name = name;
        this.totalCal = totalCal;
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

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
