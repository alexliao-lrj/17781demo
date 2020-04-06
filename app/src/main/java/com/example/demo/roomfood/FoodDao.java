package com.example.demo.roomfood;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.demo.roomfood.Food;

import java.util.List;

@Dao
public interface FoodDao {
    @Insert
    void insertFoods(Food... foods);

    @Update
    void updateFoods(Food... foods);

    @Delete
    void deleteFoods(Food... food);

    @Query("select * from FOOD order by id")
    LiveData<List<Food>> getAllFoods();

    @Query("delete from FOOD")
    void deleteAllFoods();

    /*
    @Query("select * from FOOD where food_name like :pattern order by id")
    void findFoodsWithPattern(String pattern);
     */
}
