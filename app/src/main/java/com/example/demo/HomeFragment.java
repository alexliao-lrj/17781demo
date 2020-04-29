package com.example.demo;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private FirebaseAuth mAuth;

    private View home_healthdata;
    private View home_mealplan;
    private View home_fitnessgoal;
    private View home_wellness_weight;
    private NavController navController;

    private TextView userName;
    private TextView calorieIntakeData;
    private TextView calorieBurnData;
    private TextView curWeightData;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        FragmentActivity activity = requireActivity();

        home_healthdata = activity.findViewById(R.id.home_healthdata);
        home_healthdata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController = Navigation.findNavController(v);
                navController.navigate(R.id.healthDataFragment);
            }
        });

        home_mealplan = activity.findViewById(R.id.home_mealplan);
        home_mealplan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.mealPlanFragment);
            }
        });

        home_fitnessgoal = activity.findViewById(R.id.home_fitnessgoal);
        home_fitnessgoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.fitnessGoalFragment);
            }
        });

        home_wellness_weight = activity.findViewById(R.id.home_wellness_weight);
        home_wellness_weight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCurrentWeightClick();
            }
        });

        calorieIntakeData = activity.findViewById(R.id.intake_data);
        calorieBurnData = activity.findViewById(R.id.burn_data);
        curWeightData = activity.findViewById(R.id.cur_weight_data);
    }

    @Override
    public void onStart(){
        super.onStart();

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser curUser = mAuth.getCurrentUser();

        if(curUser != null){
            userName = requireActivity().findViewById(R.id.profile_username);
            userName.setText(curUser.getEmail());
        }
    }

    private void onCurrentWeightClick(){
        //test
        FirestoreUtil util = new FirestoreUtil();
        util.setCurrentWeight(110.6, curWeightData);
        util.getCalorieIntakeByDate("2020-04-10", calorieIntakeData);
        util.getCalorieBurnByDate("2020-04-28", calorieBurnData);
        util.setBurnGoal(300.0, calorieBurnData);
    }
}
