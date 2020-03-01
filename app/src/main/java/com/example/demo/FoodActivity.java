package com.example.demo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.Context;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;

public class FoodActivity extends AppCompatActivity {

    private NavController foodNavController;
    private FoodListViewModel foodListViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);
        foodNavController = Navigation.findNavController(this, R.id.foodHomeFragmentHost);
        foodListViewModel = new ViewModelProvider(this).get(FoodListViewModel.class);
        NavigationUI.setupActionBarWithNavController(this, foodNavController);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        foodNavController.navigateUp();
    }

    @Override
    public boolean onSupportNavigateUp() {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(findViewById(R.id.foodHomeFragmentHost).getWindowToken(),0);
        foodNavController.navigateUp();
        return super.onSupportNavigateUp();
    }
}
