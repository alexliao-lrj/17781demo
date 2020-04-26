package com.example.demo;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.demo.model.Firefood;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddFoodDialogFragment extends DialogFragment implements View.OnClickListener{

    public static final String TAG = "Add Food Dialog";

    public static final String KEY_DATE = "key_date";

    private FirebaseFirestore mFirestore;
    private CollectionReference foodRef;

    private EditText foodName;
    private EditText foodPerCal;
    private EditText foodServingSize;
    private Spinner foodCategory;
    private Button cancel, submit;

    interface OnAddFoodListener{
        void onFoodAdding(Firefood food);
    }

    private OnAddFoodListener addFoodListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_add_food_dialog, container, false);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        foodName = v.findViewById(R.id.food_name);
        foodPerCal = v.findViewById(R.id.per_cal);
        foodServingSize = v.findViewById(R.id.serving_size);
        foodCategory = v.findViewById(R.id.spinner_category);

        cancel = v.findViewById(R.id.cancel_add);
        submit = v.findViewById(R.id.submit_add);
        cancel.setOnClickListener(this);
        submit.setOnClickListener(this);

        return v;
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        if(context instanceof OnAddFoodListener){
            addFoodListener = (OnAddFoodListener) context;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit_add:
                onSubmitClicked(v);
                break;
            case R.id.cancel_add:
                onCancelClicked(v);
                break;
        }
    }

    private int getSelectedCategory(){
        String selected = (String)foodCategory.getSelectedItem();
        if(selected.equals("Breakfast")){
            return 1;
        }else if(selected.equals("Lunch")){
            return 2;
        }else if(selected.equals("Dinner")){
            return 3;
        }else if(selected.equals("Snack")){
            return 4;
        }
        return 5;
    }

    public void onSubmitClicked(View view) {
        System.out.println("----------add food submitted");
        String name = foodName.getText().toString();
        double perCal = Double.valueOf(foodPerCal.getText().toString());
        double size = Double.valueOf(foodServingSize.getText().toString());
        int category = getSelectedCategory();
        Firefood food = new Firefood(name, perCal, size, category);
        if(addFoodListener != null){
            addFoodListener.onFoodAdding(food);
        }

        clearFields();

        dismiss();
    }

    public void onCancelClicked(View view) {
        dismiss();
    }

    private void clearFields(){
        foodName.setText("");
        foodPerCal.setText("");
        foodServingSize.setText("");
        foodCategory.setSelection(0);
    }
}
