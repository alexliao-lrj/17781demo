package com.example.demo.roomfood;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;


public class FoodListViewModel extends AndroidViewModel {

    private FoodRepository foodRepository;

    public FoodListViewModel(@NonNull Application application){
        super(application);
        foodRepository = new FoodRepository(application);
    }

    public LiveData<List<Food>> getAllFoodsLive(){
        return foodRepository.getAllFoodLive();
    }

    public void addFoodItems(Food... items){
        foodRepository.insertFoods(items);
    }

    public void clearAllFood(){
        foodRepository.clearAllFoods();
    }

    public void deleteFoodItems(Food... items){
        foodRepository.deleteFoods(items);
    }

    public void updateFoodItems(Food... items){
        foodRepository.updateFoods(items);
    }

}
