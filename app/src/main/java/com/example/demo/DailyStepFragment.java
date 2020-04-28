package com.example.demo;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Random;


/**
 * A simple {@link Fragment} subclass.
 */
public class DailyStepFragment extends Fragment {

    TextView step;
    TextView step_progress;
    TextView calorieStep;

    public DailyStepFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_daily_step, container, false);
        View v = inflater.inflate(R.layout.fragment_daily_step, container, false);
        step = v.findViewById(R.id.textView2);
        step_progress = v.findViewById(R.id.step_progress);
        calorieStep = v.findViewById(R.id.textView3);
        Random random = new Random();
        int maxV = 5012;
        int minV = 5011;
        int stepCount = random.nextInt(maxV) % (maxV - minV + 1) + minV;
        double caloriePerMile = 0.078;
        step.setText("Walk " + stepCount + " steps");
        step_progress.setText(String.valueOf(stepCount));
        calorieStep.setText("Burned " + (int)(stepCount * caloriePerMile) + " kcal");

        ProgressBar progress = v.findViewById(R.id.circle_progress_bar);
        int prog = 66;
        progress.setProgress(prog);

        return v;
    }


//    public void setText(String text){
//        step.setText("000");
//    }


}
