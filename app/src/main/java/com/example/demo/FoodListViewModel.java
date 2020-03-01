package com.example.demo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;


public class FoodListViewModel extends ViewModel {

    private MutableLiveData<List<FoodItem>> foodList = new MutableLiveData<>();

    public void addFoodItem(FoodItem item){
        foodList.getValue().add(item);
    }

    public void initializeList(List<FoodItem> items){
        if(foodList.getValue() == null){
            foodList.setValue(items);
        }
    }

    public void clearAllFoods(){
        foodList.getValue().clear();
    }

    public LiveData<List<FoodItem>> getFoodList(){
        return foodList;
    }
}
