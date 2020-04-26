package com.example.demo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.tasks.OnSuccessListener;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.example.demo.adapter.FiresportAdapter;
import com.example.demo.model.Firesport;
import com.example.demo.viewmodel.SportActivityViewModel;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

// sport activity
public class SportActivity extends AppCompatActivity implements
        FiresportAdapter.OnSportSelectedListener,
        AddSportDialogFragment.OnAddSportListener{
    private static final int RC_SIGN_IN = 9001;

    private static final int LIMIT = 50;

    private static final String TAG = "FiresportActivity";

    private FirebaseFirestore mFirestore;
    private Query mQuery;
    private CollectionReference mCalorieBurnSportsRef;
    private DocumentReference mCalorieBurnRef;

    private SportActivityViewModel mViewModel;
    private FiresportAdapter mAdapter;
    private RecyclerView mFiresportRecycler;

    private AddSportDialogFragment addSportDialog;
    private Toolbar toolbar;
    private FloatingActionButton addBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sport);

        toolbar = findViewById(R.id.top_toolbar);
        setSupportActionBar(toolbar);

        mViewModel = new ViewModelProvider(this).get(SportActivityViewModel.class);
        mFiresportRecycler = findViewById(R.id.recycler_firesport);

        //start sign in if necessary
        if (shouldStartSignIn()) {
            startSignIn();
        }

        initFirestore();
        initRecyclerView(); //

        addSportDialog = new AddSportDialogFragment();//

        addBtn = findViewById(R.id.add_sport_button);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //onAddItemsClicked();
                addSportItem();
            }
        });

    }

    private void initFirestore(){
        mFirestore = FirebaseFirestore.getInstance();
        String userKey = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        String dateKey = LocalDate.now().toString();
        mCalorieBurnRef = mFirestore
                .collection("users")
                .document(userKey)
                .collection(dateKey)
                .document("calorieBurn");
        mCalorieBurnSportsRef = mCalorieBurnRef.collection("sports");

        mQuery = mCalorieBurnSportsRef
                .orderBy("timestamp", Query.Direction.ASCENDING)
                .limit(LIMIT);
    }

    private void initRecyclerView(){
        if (mQuery == null) {
            Log.w(TAG, "No query, not initializing RecyclerView");
        }

        mAdapter = new FiresportAdapter(mQuery, this) {
            @Override
            protected void onError(FirebaseFirestoreException e) {
                // Show a snackbar on errors
                Snackbar.make(findViewById(android.R.id.content),
                        "Error: check logs for info.", Snackbar.LENGTH_LONG).show();
            }
        };

        mFiresportRecycler.setLayoutManager(new LinearLayoutManager(this));
        mFiresportRecycler.setAdapter(mAdapter);
    }

    private void onAddItemsClicked() {
        // TODO(developer): Add random foods
        String[] sn = {"Ellipticals", "Walking", "Running", "Zumba", "305Fitness"};
        double[] tc = {432, 212, 245, 320, 280};
        int[] ca = {1, 2, 3, 4, 5};
        for(int i = 0; i < 5; i++){
            Firesport sport = new Firesport(sn[i], tc[i], ca[i]);
            mCalorieBurnSportsRef.add(sport);
        }
    }

    //add sport transactions
    private Task<Void> addSport(final DocumentReference burnRef, CollectionReference mCalorieBurnSportsRef, Firesport sport){
        final DocumentReference sportRef = burnRef.collection("sports").document();
        return mFirestore.runTransaction((transaction -> {
            //return null if not exists
            Object temp = transaction.get(burnRef).get("Total");
            Double burnTotalCal = 0.0;
            if(temp == null){
                Map<String, Double> total = new HashMap<>();
                total.put("Total", burnTotalCal);
                transaction.set(burnRef, total);
            }else{
                burnTotalCal = (Double) temp;
            }
            burnTotalCal += sport.getTotalCal();
            transaction.set(sportRef, sport);
            transaction.update(burnRef, "Total", burnTotalCal);
            return null;
        }));
    }

    //delete sport transaction
    private Task<Void> deleteSport(final DocumentReference burnRef, String sportId){
        DocumentReference sportRef = burnRef.collection("sports").document(sportId);
        return mFirestore.runTransaction((transaction -> {
            Double intakeTotalCal = (Double) transaction.get(burnRef).get("Total");
            Firesport sport = transaction.get(sportRef).toObject(Firesport.class);
            intakeTotalCal -= sport.getTotalCal();
            transaction.delete(sportRef);
            transaction.update(burnRef, "Total", intakeTotalCal);
            return null;
        }));
    }

    //update sport transaction
    private Task<Void> updateSport(final DocumentReference burnRef, final DocumentReference oldSportRef, Firesport sport){
        return mFirestore.runTransaction((transaction -> {
            Double burnTotalCal = (Double) transaction.get(burnRef).get("Total");
            Double sportTotalCal = (Double) transaction.get(oldSportRef).get("totalCal");
            burnTotalCal = burnTotalCal - sportTotalCal + sport.getTotalCal();
            transaction.set(oldSportRef, sport);
            transaction.update(burnRef, "Total", burnTotalCal);
            return null;
        }));
    }

    private void addSportItem(){//
        addSportDialog.show(getSupportFragmentManager(), AddSportDialogFragment.TAG);
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

        // Start listening for Firestore updates
        if (mAdapter != null) {
            mAdapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAdapter != null) {
            mAdapter.stopListening();
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


    @Override
    public void onSportSelected(DocumentSnapshot sport) {
    }

    // override by yawei

    private void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    // add sport
    @Override
    public void onSportAdding(Firesport sport) {
        addSport(mCalorieBurnRef, mCalorieBurnSportsRef, sport).addOnSuccessListener(this, new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "-------Sport added");
                hideKeyboard();
                mFiresportRecycler.smoothScrollToPosition(0);
            }
        }).addOnFailureListener(this, (e)->{
            Log.w(TAG, "--------Sport add failed", e);
            hideKeyboard();
            Snackbar.make(findViewById(android.R.id.content), "Failed to add sport, Retry.",
                    Snackbar.LENGTH_SHORT).show();
        });
    }
}
