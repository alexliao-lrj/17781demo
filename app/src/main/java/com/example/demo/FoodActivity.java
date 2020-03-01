package com.example.demo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Bundle;

public class FoodActivity extends AppCompatActivity {

    private NavController foodNavController;
    private FoodListViewModel foodListViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);
        foodNavController = Navigation.findNavController(this, R.id.foodHomeFragmentHost);
        foodListViewModel = new ViewModelProvider(this).get(FoodListViewModel.class);
    }
}
