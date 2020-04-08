package com.example.demo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.demo.viewmodel.MainActivityViewModel;
import com.firebase.ui.auth.AuthUI;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Collections;

public class MainActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 9001;
    //firebase app id: fir-17781demo
    private BottomNavigationView bottomNavigationView;
    private NavController bottomNavController;

    private MainActivityViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.top_toolbar);
        setSupportActionBar(toolbar);

        mViewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);

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
                        Intent foodIntent = new Intent(MainActivity.this, FirestoreActivity.class);
                        startActivity(foodIntent);
                        break;
                    case R.id.nav_sport:
                        Intent sportIntent = new Intent(MainActivity.this, SportActivity.class);
                        startActivity(sportIntent);
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
                AuthUI.getInstance().signOut(this);
                startSignIn();
                break;
            case R.id.home:
                bottomNavController.navigate(R.id.homeFragment);
                break;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_menu_main, menu);
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();

        // Start sign in if necessary
        if (shouldStartSignIn()) {
            startSignIn();
            return;
        }

    }

    private boolean shouldStartSignIn() {
        return (!mViewModel.getIsSigningIn() && FirebaseAuth.getInstance().getCurrentUser() == null);
    }

    private void startSignIn() {
        // Sign in with FirebaseUI
        Intent intent = AuthUI.getInstance().createSignInIntentBuilder()
                .setAvailableProviders(Collections.singletonList(
                        new AuthUI.IdpConfig.EmailBuilder().build()))
                .setIsSmartLockEnabled(false)
                .build();

        startActivityForResult(intent, RC_SIGN_IN);
        mViewModel.setIsSigningIn(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            mViewModel.setIsSigningIn(false);

            if (resultCode != RESULT_OK && shouldStartSignIn()) {
                startSignIn();
            }
        }
    }
}
