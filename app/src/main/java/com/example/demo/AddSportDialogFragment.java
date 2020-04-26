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

import com.example.demo.model.Firesport;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class AddSportDialogFragment extends DialogFragment implements View.OnClickListener{
    public static final String TAG = "Add Sport Dialog";

    public static final String KEY_DATE = "key_date";

    private FirebaseFirestore mFirestore;
    private CollectionReference sportRef;

    private EditText sportName;
    private EditText sportCalorie;
    private Spinner sportCategory;
    private Button cancel, submit;

    interface OnAddSportListener{
        void onSportAdding(Firesport sport);
    }

    private OnAddSportListener addSportListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_add_sport_dialog, container, false);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        sportName = v.findViewById(R.id.sport_name);
        sportCalorie = v.findViewById(R.id.sport_calorie);
        sportCategory = v.findViewById(R.id.spinner_category_sport);

        cancel = v.findViewById(R.id.cancel_add_sport);
        submit = v.findViewById(R.id.submit_add_sport);
        cancel.setOnClickListener(this);
        submit.setOnClickListener(this);

        return v;
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        if(context instanceof OnAddSportListener){
            addSportListener = (OnAddSportListener) context;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit_add_sport:
                onSubmitClicked(v);
                break;
            case R.id.cancel_add_sport:
                onCancelClicked(v);
                break;
        }
    }

    private int getSelectedCategory(){
        String selected = (String)sportCategory.getSelectedItem();
        if(selected.equals("Rope skipping")){
            return 1;
        }else if(selected.equals("Walk")){
            return 2;
        }else if(selected.equals("Running")){
            return 3;
        }else if(selected.equals("Climb stairs")){
            return 4;
        } else if(selected.equals("Swim")){
            return 5;
        } else if(selected.equals("Ride bike")){
            return 6;
        } else if(selected.equals("Dance")){
            return 7;
        } else if(selected.equals("Yoga")){
            return 8;
        } else if(selected.equals("Ball game")){
            return 9;
        } else if(selected.equals("Skateboard")){
            return 10;
        }else if(selected.equals("Skiing")){
            return 11;
        }
        return 12;
    }

    public void onSubmitClicked(View view) {
        String name = sportName.getText().toString();
        double totalCalorie = Double.parseDouble(sportCalorie.getText().toString());
        int category = getSelectedCategory();
        Firesport sport = new Firesport(name, totalCalorie, category);
        if(addSportListener != null){
            addSportListener.onSportAdding(sport);
        }

        clearFields();

        dismiss();
    }

    public void onCancelClicked(View view) {
        dismiss();
    }

    private void clearFields(){
        sportName.setText("");
        sportCalorie.setText("");
        sportCategory.setSelection(0);
    }
}
