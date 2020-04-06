package com.example.demo.roomfood;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;

import com.example.demo.FirebaseLoginUtil;
import com.example.demo.R;

public class FoodActivity extends AppCompatActivity {

    private NavController foodNavController;
    //private FoodListViewModel foodListViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);
        foodNavController = Navigation.findNavController(this, R.id.foodHomeFragmentHost);
        Toolbar toolbar = findViewById(R.id.top_toolbar);
        setSupportActionBar(toolbar);
        //foodListViewModel = new ViewModelProvider(this).get(FoodListViewModel.class);
        //NavigationUI.setupActionBarWithNavController(this, foodNavController);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_menu_main, menu);
        return true;
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
}
