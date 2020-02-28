package com.example.demo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    NavController bottomNavController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavController = Navigation.findNavController(this, R.id.nav_host_fragment);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_home:
                        bottomNavController.navigate(R.id.homeFragment);
                        break;
                    case R.id.nav_weight:
                        bottomNavController.navigate(R.id.healthDataFragment);
                        break;
                    case R.id.nav_me:
                        bottomNavController.navigate(R.id.fitnessGoalFragment);
                        break;
                    case R.id.nav_food:
                        bottomNavController.navigate(R.id.addFoodFragment);
                        break;
                    case R.id.nav_sport:
                        bottomNavController.navigate(R.id.dailyStepFragment);
                        break;
                }
                return true;
            }
        });
    }
}
