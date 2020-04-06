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
    private NavController navController;

    TextView userName;

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
}
