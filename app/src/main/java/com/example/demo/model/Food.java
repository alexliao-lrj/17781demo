package com.example.demo.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Food {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "food_name")
    private String foodName;

    @ColumnInfo(name = "food_cal")
    private int foodCal;

    public Food(String foodName, int foodCal){
        this.foodName = foodName;
        this.foodCal = foodCal;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public int getFoodCal() {
        return foodCal;
    }

    public void setFoodCal(int foodCal) {
        this.foodCal = foodCal;
    }
}
