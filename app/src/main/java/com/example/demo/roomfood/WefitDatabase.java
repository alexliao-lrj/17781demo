package com.example.demo.roomfood;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Food.class}, version = 1, exportSchema = false)
public abstract class WefitDatabase extends RoomDatabase {
    private static WefitDatabase wefitDatabase;
    public static synchronized WefitDatabase getWefitDatabase(Context context){
        if(wefitDatabase == null){
            wefitDatabase = Room
                    .databaseBuilder(context.getApplicationContext(), WefitDatabase.class, "wefit_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return wefitDatabase;
    }

    public abstract FoodDao getFoodDao();
}
