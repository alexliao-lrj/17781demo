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
public class MealPlanFragment extends Fragment {

    private TextView curMealPlan;

    private Integer mealPlan_id = 0;

    private View mealplan_balanced_diet;
    private View mealplan_52fast;
    private View mealplan_168fasting;
    private View mealplan_186fasting;
    private View mealplan_204fasting;

    public MealPlanFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_meal_plan, container, false);
        // Inflate the layout for this fragment
        return v;
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FragmentActivity activity = requireActivity();

        mealplan_52fast = activity.findViewById(R.id.mealplan_52fast);
        mealplan_52fast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mealPlan_id = 1;
                FirestoreUtil util = new FirestoreUtil();
                util.setMealPlan(mealPlan_id, curMealPlan);
            }
        });

        mealplan_168fasting = activity.findViewById(R.id.mealplan_168fasting);
        mealplan_168fasting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mealPlan_id = 2;
                FirestoreUtil util = new FirestoreUtil();
                util.setMealPlan(mealPlan_id, curMealPlan);
            }
        });

        mealplan_186fasting = activity.findViewById(R.id.mealplan_186fasting);
        mealplan_186fasting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mealPlan_id = 3;
                FirestoreUtil util = new FirestoreUtil();
                util.setMealPlan(mealPlan_id, curMealPlan);
            }
        });

        mealplan_204fasting = activity.findViewById(R.id.mealplan_204fasting);
        mealplan_204fasting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mealPlan_id = 4;
                FirestoreUtil util = new FirestoreUtil();
                util.setMealPlan(mealPlan_id, curMealPlan);
            }
        });

        curMealPlan = activity.findViewById(R.id.current_mealplan_detail);
        FirestoreUtil util = new FirestoreUtil();
        util.getMealPlan(curMealPlan);
//        util.setMealPlan(mealPlan_id, curMealPlan);
    }
}
