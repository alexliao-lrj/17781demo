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
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.example.demo.model.Firesport;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class EditCurrentWeightDialogFragment extends DialogFragment{
    public static final String TAG = "Edit current weight";

    public static final String KEY_DATE = "key_date";

    private EditText currentWeight;

    public interface CurrentWeightListener {
        void editCurrentWeight(Double weight);
    }

    private CurrentWeightListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_edit_current_weight, null);

        builder.setView(view)
                .setTitle("Current Weight")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String newWeight = currentWeight.getText().toString();
                        listener.editCurrentWeight(Double.parseDouble(newWeight));
                    }
                });

        currentWeight = view.findViewById(R.id.current_weight);

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (CurrentWeightListener) getParentFragment();
    }
}
