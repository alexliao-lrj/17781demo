package com.example.demo;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.demo.model.Firefood;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDate;


/**
 * A simple {@link Fragment} subclass.
 */
public class UpdateFoodDialogFragment extends DialogFragment implements View.OnClickListener{
    public static final String TAG = "Update Food Dialog";

    public static final String KEY_FOOD_ID = "key_food_id";

    private FirebaseFirestore mFirestore;
    private DocumentReference mFoodRef;
    private CollectionReference mFoodsCollectionRef;
    private String foodId;

    private Firefood mFood;

    private TextView foodName;
    private EditText servingSize;
    private Button cancel, submit, delete;

    interface UpdateFoodListner{
        void onFoodUpdating(DocumentReference foodRef, Firefood food);

        void onFoodDeleting(CollectionReference foodsRef, String foodId);
    }

    private UpdateFoodListner updateFoodListner;

    @NonNull
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_update_food_dialog, container, false);
        foodName = v.findViewById(R.id.food_name);
        servingSize = v.findViewById(R.id.serving_size);

        cancel = v.findViewById(R.id.cancel_update);
        submit = v.findViewById(R.id.submit_update);
        delete = v.findViewById(R.id.delete_food);

        cancel.setOnClickListener(this);
        submit.setOnClickListener(this);
        delete.setOnClickListener(this);

        mFirestore = FirebaseFirestore.getInstance();
        String userKey = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        String dateKey = LocalDate.now().toString();
        foodId = getArguments().getString(KEY_FOOD_ID);
        mFoodsCollectionRef = mFirestore
                .collection("users")
                .document(userKey)
                .collection(dateKey)
                .document("calorieIntake")
                .collection("foods");
        mFoodRef = mFoodsCollectionRef.document(foodId);
        mFoodRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                mFood = documentSnapshot.toObject(Firefood.class);
                foodName.setText(mFood.getName());
                servingSize.setText(String.valueOf(mFood.getServing()));
            }
        });

        return v;
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        //set updatefoodlistener to FirestoreActivity
        if(context instanceof UpdateFoodListner){
            updateFoodListner = (UpdateFoodListner) context;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit_update:
                onSubmitClicked(v);
                break;
            case R.id.cancel_update:
                onCancelClicked(v);
                break;
            case R.id.delete_food:
                onDeleteClicked(v);
                break;
        }
        servingSize.setText("");
    }

    public void onSubmitClicked(View view) {
        System.out.println("----------food update submitted");
        double newSize = Double.valueOf(servingSize.getText().toString());
        mFood.setServing(newSize);
        mFood.setTotalCal(newSize * mFood.getPerCal());
        if(updateFoodListner != null){
            updateFoodListner.onFoodUpdating(mFoodRef, mFood);
        }

        dismiss();
    }

    public void onCancelClicked(View view) {
        dismiss();
    }

    public void onDeleteClicked(View view){
        if(updateFoodListner != null){
            updateFoodListner.onFoodDeleting(mFoodsCollectionRef, foodId);
        }
        dismiss();
    }
}
