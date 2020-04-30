package com.example.demo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.fragment.app.DialogFragment;

public class EditCalorieBurnDialogFragment extends DialogFragment {
    public static final String TAG = "Edit burn goal";

    public static final String KEY_DATE = "key_date";

    private EditText currentBurnGoal;

    public interface BurnGoalListener {
        void editBurnGoal(Double burnGoal);
    }

    private BurnGoalListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_edit_calorie_burn, null);


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
                        String newGoal = currentBurnGoal.getText().toString();
                        listener.editBurnGoal(Double.parseDouble(newGoal));
                    }
                });

        currentBurnGoal = view.findViewById(R.id.current_burn_goal);

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (BurnGoalListener) getParentFragment();
    }
}