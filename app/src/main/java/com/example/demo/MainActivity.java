package com.example.demo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    //firebase app id: fir-17781demo
    private BottomNavigationView bottomNavigationView;
    private NavController bottomNavController;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.top_toolbar);
        setSupportActionBar(toolbar);


        mAuth = FirebaseAuth.getInstance();

        FirebaseUser curUser = mAuth.getCurrentUser();
        if(curUser != null){
            System.out.println("Main: " + curUser.getEmail());
        }

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
                        Intent foodIntent = new Intent(MainActivity.this, FoodActivity.class);
                        startActivity(foodIntent);
                        break;
                    case R.id.nav_sport:
                        bottomNavController.navigate(R.id.dailyStepFragment);
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.signOut:
                FirebaseLoginUtil.signOutUser(this);
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_menu_main, menu);
        return true;
    }
}
