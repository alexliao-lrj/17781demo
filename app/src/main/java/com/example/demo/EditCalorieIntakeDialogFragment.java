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

import com.example.demo.model.Firesport;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class EditCalorieIntakeDialogFragment extends DialogFragment{
    public static final String TAG = "Edit intake goal";

    public static final String KEY_DATE = "key_date";

    private EditText currentIntakeGoal;

    public interface IntakeGoalListener {
        void editIntakeGoal(Double intakeGoal);
    }

    private IntakeGoalListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_edit_calorie_intake, null);


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
                        String newGoal = currentIntakeGoal.getText().toString();
                        listener.editIntakeGoal(Double.parseDouble(newGoal));
                    }
                });

        currentIntakeGoal = view.findViewById(R.id.current_intake_goal);

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (IntakeGoalListener) getParentFragment();
    }
}
