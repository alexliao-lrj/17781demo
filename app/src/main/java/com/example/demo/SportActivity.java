package com.example.demo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.demo.model.Firefood;
import com.example.demo.model.Firesport;
import com.example.demo.viewmodel.FirestoreActivityViewModel;
import com.example.demo.viewmodel.SportActivityViewModel;
import com.firebase.ui.auth.AuthUI;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.time.LocalDate;
import java.util.Collections;

public class SportActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 9001;

    private static final int LIMIT = 50;

    private FirebaseFirestore mFirestore;
    private Query mQuery;
    private CollectionReference mCalorieBurnSportsRef;

    private SportActivityViewModel mViewModel;

    private Toolbar toolbar;
    private FloatingActionButton addBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sport);

        toolbar = findViewById(R.id.top_toolbar);
        setSupportActionBar(toolbar);

        mViewModel = new ViewModelProvider(this).get(SportActivityViewModel.class);

        //start sign in if necessary
        if (shouldStartSignIn()) {
            startSignIn();
        }

        initFirestore();

        addBtn = findViewById(R.id.add_sport_button);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddItemsClicked();
            }
        });

    }

    private void initFirestore(){
        mFirestore = FirebaseFirestore.getInstance();
        String userKey = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        String dateKey = LocalDate.now().toString();
        mCalorieBurnSportsRef = mFirestore
                .collection("users")
                .document(userKey)
                .collection(dateKey)
                .document("calorieBurn")
                .collection("sports");

        mQuery = mCalorieBurnSportsRef
                .orderBy("timestamp", Query.Direction.ASCENDING)
                .limit(LIMIT);
    }

    private void onAddItemsClicked() {
        // TODO(developer): Add random foods
        String[] sn = {"Ellipticals", "Walking", "Running", "Zumba", "305Fitness"};
        double[] tc = {432, 212, 245, 320, 280};
        for(int i = 0; i < 5; i++){
            Firesport sport = new Firesport(sn[i], tc[i]);
            mCalorieBurnSportsRef.add(sport);
        }
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
                AuthUI.getInstance().signOut(this);
                startSignIn();
                break;
            case R.id.home:
                Intent intent = new Intent(SportActivity.this, MainActivity.class);
                startActivity(intent);
                break;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                break;
        }
        return super.onOptionsItemSelected(item);
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
