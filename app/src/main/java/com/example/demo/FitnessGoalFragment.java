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
public class FitnessGoalFragment extends Fragment {

    private FirebaseAuth mAuth;

    private View me_dailyStep;
//    private View home_mealplan;
//    private View home_fitnessgoal;
    private NavController navController;

    private TextView userName;

    public FitnessGoalFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fitness_goal, container, false);
//        return inflater.inflate(R.layout.activity_me, container, false);

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        FragmentActivity activity = requireActivity();

        me_dailyStep = activity.findViewById(R.id.me_item_reord);
        me_dailyStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.dailyStepFragment);
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
