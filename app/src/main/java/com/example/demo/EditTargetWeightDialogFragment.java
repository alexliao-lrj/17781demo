package com.example.demo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.fragment.app.DialogFragment;

import com.example.demo.AddFoodDialogFragment;
import com.example.demo.R;
import com.example.demo.model.Firefood;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class EditTargetWeightDialogFragment extends DialogFragment{
    public static final String TAG = "Edit taget weight";

    public static final String KEY_DATE = "key_date";

    private EditText targetWeight;

    public interface TargetWeightListener {
        void editTargetWeight(Double weight);
    }

    private TargetWeightListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_edit_weight_goal, null);


        builder.setView(view)
//                .setTitle("Current Weight")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String newWeight = targetWeight.getText().toString();
                        listener.editTargetWeight(Double.parseDouble(newWeight));
                    }
                });

        targetWeight = view.findViewById(R.id.target_weight);

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (TargetWeightListener) getParentFragment();
    }
}