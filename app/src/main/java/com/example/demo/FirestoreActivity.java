package com.example.demo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.example.demo.adapter.FirefoodAdapter;
import com.example.demo.model.Firefood;
import com.example.demo.viewmodel.FirestoreActivityViewModel;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

//food activity
public class FirestoreActivity extends AppCompatActivity implements
        FirefoodAdapter.OnFoodSelectedListener,
        UpdateFoodDialogFragment.UpdateFoodListner,
        AddFoodDialogFragment.OnAddFoodListener {

    private static final String TAG = "FirestoreActivity";

    //private FirebaseFirestore mFirestore;

    private static final int RC_SIGN_IN = 9001;

    private static final int LIMIT = 50;

    private FirebaseFirestore mFirestore;
    private Query mQuery;
    private DocumentReference mCalorieIntakeRef;
    private CollectionReference mCalorieIntakeFoodsRef;

    private FirestoreActivityViewModel mViewModel;
    private FirefoodAdapter mAdapter;
    private RecyclerView mFirefoodRecycler;

    private UpdateFoodDialogFragment updateFoodDialog;
    private AddFoodDialogFragment addFoodDialog;
    private Toolbar toolbar;
    private FloatingActionButton addFoodBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firestore);

        toolbar = findViewById(R.id.top_toolbar);
        setSupportActionBar(toolbar);

        mViewModel = new ViewModelProvider(this).get(FirestoreActivityViewModel.class);

        mFirefoodRecycler = findViewById(R.id.recycler_firefood);

        //start sign in if necessary
        if (shouldStartSignIn()) {
            startSignIn();
        }

        initFirestore();
        initRecyclerView();

        addFoodDialog = new AddFoodDialogFragment();

        addFoodBtn = findViewById(R.id.addFoodButton);
        addFoodBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //onAddItemsClicked();
                addFoodItem();
            }
        });

        updateFoodDialog = new UpdateFoodDialogFragment();
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

    private void initFirestore(){
        mFirestore = FirebaseFirestore.getInstance();
        String userKey = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        String dateKey = LocalDate.now().toString();
        mCalorieIntakeRef = mFirestore
                .collection("users")
                .document(userKey)
                .collection(dateKey)
                .document("calorieIntake");
        mCalorieIntakeFoodsRef = mCalorieIntakeRef.collection("foods");

        mQuery = mCalorieIntakeFoodsRef
                .orderBy("category", Query.Direction.ASCENDING)
                .limit(LIMIT);
        /*
        mQuery = mFirestore
                .collection("users")
                .document(userKey)
                .collection(dateKey)
                .document("calorieIntake")
                .collection("foods")
                .orderBy("category", Query.Direction.ASCENDING)
                .limit(LIMIT);
         */
    }

    //frontend recycler view
    private void initRecyclerView(){
        if (mQuery == null) {
            Log.w(TAG, "No query, not initializing RecyclerView");
        }

        mAdapter = new FirefoodAdapter(mQuery, this) {
            @Override
            protected void onError(FirebaseFirestoreException e) {
                // Show a snackbar on errors
                Snackbar.make(findViewById(android.R.id.content),
                        "Error: check logs for info.", Snackbar.LENGTH_LONG).show();
            }
        };

        mFirefoodRecycler.setLayoutManager(new LinearLayoutManager(this));
        mFirefoodRecycler.setAdapter(mAdapter);
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

    private void onAddItemsClicked() {
        // TODO(developer): Add random foods
        String userKey = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        String dateKey = LocalDate.now().toString();
        CollectionReference foods = mFirestore
                .collection("users")
                .document(userKey)
                .collection(dateKey)
                .document("calorieIntake")
                .collection("foods");

        String[] fn = {"Melon", "Banana", "Orange", "Milk", "Coffee"};
        double[] pc = {32, 82, 42.5, 90, 5};
        double[] serving = {2, 1, 3, 0.8, 1};
        int[] category = {4, 1, 3, 2, 1};
        for(int i = 0; i < 5; i++){
            Firefood food = new Firefood(fn[i], pc[i], serving[i], category[i]);
            foods.add(food);
        }
    }

    private void addFoodItem(){
        addFoodDialog.show(getSupportFragmentManager(), AddFoodDialogFragment.TAG);
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
                Intent intent = new Intent(FirestoreActivity.this, MainActivity.class);
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
    public void onFoodSelected(DocumentSnapshot food) {
        Bundle bundle = new Bundle();
        bundle.putString(UpdateFoodDialogFragment.KEY_FOOD_ID, food.getId());
        updateFoodDialog.setArguments(bundle);
        updateFoodDialog.show(getSupportFragmentManager(), UpdateFoodDialogFragment.TAG);
    }

    private Task<Void> updateFood(final DocumentReference intakeRef, final DocumentReference oldFoodRef, final Firefood food){
        return mFirestore.runTransaction((transaction -> {
            Double intakeTotalCal = (Double) transaction.get(intakeRef).get("Total");
            Double foodTotalCal = (Double) transaction.get(oldFoodRef).get("totalCal");
            intakeTotalCal = intakeTotalCal - foodTotalCal + food.getTotalCal();
            transaction.set(oldFoodRef, food);
            transaction.update(intakeRef, "Total", intakeTotalCal);
            return null;
        }));
    }

    //update food
    @Override
    public void onFoodUpdating(DocumentReference foodRef, Firefood food) {
        updateFood(mCalorieIntakeRef, foodRef, food).addOnSuccessListener(this, new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "-------Food updated");
                hideKeyboard();
                mFirefoodRecycler.smoothScrollToPosition(0);
            }
        }).addOnFailureListener(this, (e)->{
            Log.w(TAG, "--------Food update failed", e);
            hideKeyboard();
            Snackbar.make(findViewById(android.R.id.content), "Failed to update food, Retry.",
                    Snackbar.LENGTH_SHORT).show();
        });
    }

    private void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    //add food
    @Override
    public void onFoodAdding(Firefood food) {
        addFood(mCalorieIntakeRef, mCalorieIntakeFoodsRef, food).addOnSuccessListener(this, new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "-------Food added");
                hideKeyboard();
                mFirefoodRecycler.smoothScrollToPosition(0);
            }
        }).addOnFailureListener(this, (e)->{
            Log.w(TAG, "--------Food add failed", e);
            hideKeyboard();
            Snackbar.make(findViewById(android.R.id.content), "Failed to add food, Retry.",
                    Snackbar.LENGTH_SHORT).show();
        });
    }

    private Task<Void> addFood(final DocumentReference intakeRef, final CollectionReference foodsRef, final Firefood food){
        final DocumentReference foodRef = foodsRef.document();
        return mFirestore.runTransaction((transaction -> {
            //return null if not exists
            Object temp = transaction.get(intakeRef).get("Total");
            Double intakeTotalCal = 0.0;
            if(temp == null){
                Map<String, Double> total = new HashMap<>();
                total.put("Total", intakeTotalCal);
                transaction.set(intakeRef, total);
            }else{
                intakeTotalCal = (Double) temp;
            }
            intakeTotalCal += food.getTotalCal();
            transaction.set(foodRef, food);
            transaction.update(intakeRef, "Total", intakeTotalCal);
            return null;
        }));
    }

    private Task<Void> deleteFood(final DocumentReference intakeRef, final CollectionReference foodsRef, final String foodId){
        DocumentReference foodRef = foodsRef.document(foodId);
        return mFirestore.runTransaction((transaction -> {
            Double intakeTotalCal = (Double) transaction.get(intakeRef).get("Total");
            Firefood food = transaction.get(foodRef).toObject(Firefood.class);
            intakeTotalCal -= food.getTotalCal();
            transaction.delete(foodRef);
            transaction.update(intakeRef, "Total", intakeTotalCal);
            return null;
        }));
    }

    //delete food
    @Override
    public void onFoodDeleting(CollectionReference foodsRef, String foodId) {
        deleteFood(mCalorieIntakeRef, foodsRef, foodId).addOnSuccessListener(this, new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "-------Food deleted");
                hideKeyboard();
                mFirefoodRecycler.smoothScrollToPosition(0);
            }
        }).addOnFailureListener(this, (e)->{
            Log.w(TAG, "--------Food delete failed", e);
            hideKeyboard();
            Snackbar.make(findViewById(android.R.id.content), "Failed to delete food, Retry.",
                    Snackbar.LENGTH_SHORT).show();
        });
    }
}
