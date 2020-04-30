package com.example.demo;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class HealthDataFragment extends Fragment implements
        EditTargetWeightDialogFragment.TargetWeightListener,
        EditCurrentWeightHealthDialogFragment.HealthCurrentWeightListener,
        View.OnClickListener {


    public HealthDataFragment() {
        // Required empty public constructor
    }

    private View health_data_weight;
    private View health_data_target_weight;

    private TextView curWeightData;
    private TextView targetWeight;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_health_data, container, false);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        FragmentActivity activity = requireActivity();

        health_data_weight = activity.findViewById(R.id.current_weight);
        health_data_weight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditCurrentWeightHealthDialogFragment editCurrentWeightHealthDialogFragment = new EditCurrentWeightHealthDialogFragment();
                editCurrentWeightHealthDialogFragment.show(getChildFragmentManager(), editCurrentWeightHealthDialogFragment.TAG);
            }
        });

        health_data_target_weight = activity.findViewById(R.id.target_weight);
        health_data_target_weight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditTargetWeightDialogFragment editTargetWeightDialogFragment = new EditTargetWeightDialogFragment();
                editTargetWeightDialogFragment.show(getChildFragmentManager(), editTargetWeightDialogFragment.TAG);
            }
        });

        curWeightData = activity.findViewById(R.id.cur_weight_health_data);
        targetWeight = activity.findViewById(R.id.target_weight_health_data);

        FirestoreUtil util = new FirestoreUtil();
        util.getWeightGoal(targetWeight);
        util.getCurrentWeight(curWeightData);
    }


    public void editHealthCurrentWeight(Double weight) {
        FirestoreUtil util = new FirestoreUtil();
        util.setCurrentWeight(weight, curWeightData);
    }

    public void editTargetWeight(Double weight){
        FirestoreUtil util = new FirestoreUtil();
        util.setWeightGoal(weight, targetWeight);
    }
}
