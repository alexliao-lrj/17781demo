package com.example.demo;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class FoodRepository {

    private LiveData<List<Food>> allFoodLive;
    private FoodDao foodDao;

    public FoodRepository(Context context){
        WefitDatabase wefitDatabase = WefitDatabase.getWefitDatabase(context);
        foodDao = wefitDatabase.getFoodDao();
        allFoodLive = foodDao.getAllFoods();
    }

    public LiveData<List<Food>> getAllFoodLive() {
        return allFoodLive;
    }

    void insertFoods(Food... foods){
        new InsertAsyncTask(foodDao).execute(foods);
    }

    void clearAllFoods(){
        new ClearAllAsyncTask(foodDao).execute();
    }

    void deleteFoods(Food... foods){
        new DeleteAsyncTask(foodDao).execute(foods);
    }

    void updateFoods(Food... foods){
        new UpdateAsyncTask(foodDao).execute(foods);
    }

    //asynchronized task in background
    static class InsertAsyncTask extends AsyncTask<Food, Void, Void>{

        private FoodDao foodDao;

        InsertAsyncTask(FoodDao foodDao){
            this.foodDao = foodDao;
        }

        @Override
        protected Void doInBackground(Food... foods) {
            foodDao.insertFoods(foods);
            return null;
        }
    }

    static class ClearAllAsyncTask extends AsyncTask<Void, Void, Void>{

        private FoodDao foodDao;

        ClearAllAsyncTask(FoodDao foodDao){
            this.foodDao = foodDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            foodDao.deleteAllFoods();
            return null;
        }
    }

    static class DeleteAsyncTask extends AsyncTask<Food, Void, Void>{

        private FoodDao foodDao;

        DeleteAsyncTask(FoodDao foodDao){
            this.foodDao = foodDao;
        }

        @Override
        protected Void doInBackground(Food... foods) {
            foodDao.deleteFoods(foods);
            return null;
        }
    }

    static class UpdateAsyncTask extends AsyncTask<Food, Void, Void>{

        private FoodDao foodDao;

        UpdateAsyncTask(FoodDao foodDao){
            this.foodDao = foodDao;
        }

        @Override
        protected Void doInBackground(Food... foods) {
            foodDao.updateFoods(foods);
            return null;
        }
    }

}
