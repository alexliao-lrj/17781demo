package com.example.demo;

public class FoodItem {
    private String foodName;
    private int foodCal;

    public FoodItem(String foodName, int foodCal){
        this.foodName = foodName;
        this.foodCal = foodCal;
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
