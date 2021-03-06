package com.example.demo;


import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.demo.viewmodel.MainActivityViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements
        EditCalorieIntakeDialogFragment.IntakeGoalListener,
        EditCalorieBurnDialogFragment.BurnGoalListener,
        EditCurrentWeightDialogFragment.CurrentWeightListener{

    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;
    private DocumentReference userDocRef;
    private String userKey = "calmroger0@gmail.com";

    private View home_healthdata;
    private View home_mealplan;
    private View home_fitnessgoal;
    private View home_wellness_weight;
    private View home_wellness_intake;
    private View home_wellness_burn;
    private NavController navController;

    private TextView userName;
    private TextView calorieIntakeData;
    private TextView calorieBurnData;
    private TextView curWeightData;
    private TextView curIntakeGoal;
    private TextView curBurnGoal;

    //meal plan notification view
    private TextView notification;

    public HomeFragment() {
        // Required empty public constructor
    }

    private void initFirestore(){
        mFirestore = FirebaseFirestore.getInstance();
        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            userKey = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        }
        userDocRef = mFirestore.collection("users").document(userKey);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        // Inflate the layout for this fragment
        return v;
    }


    public void openDialog(){
        EditCurrentWeightDialogFragment editCurrentWeightDialogFragment = new EditCurrentWeightDialogFragment();
        editCurrentWeightDialogFragment.show(getChildFragmentManager(), editCurrentWeightDialogFragment.TAG);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        FragmentActivity activity = requireActivity();

        initFirestore();

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
                openDialog();
            }
        });

        home_wellness_intake = activity.findViewById(R.id.home_wellness_intake);
        home_wellness_intake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditCalorieIntakeDialogFragment editCalorieIntakeDialogFragment = new EditCalorieIntakeDialogFragment();
                editCalorieIntakeDialogFragment.show(getChildFragmentManager(), editCalorieIntakeDialogFragment.TAG);
            }
        });

        home_wellness_burn = activity.findViewById(R.id.home_wellness_burn);
        home_wellness_burn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditCalorieBurnDialogFragment editCalorieBurnDialogFragment = new EditCalorieBurnDialogFragment();
                editCalorieBurnDialogFragment.show(getChildFragmentManager(), editCalorieBurnDialogFragment.TAG);
            }
        });

        calorieIntakeData = activity.findViewById(R.id.intake_data);
        calorieBurnData = activity.findViewById(R.id.burn_data);
        curWeightData = activity.findViewById(R.id.cur_weight_data);
        curIntakeGoal = activity.findViewById(R.id.intake_data_goal);
        curBurnGoal = activity.findViewById(R.id.burn_data_goal);
        notification = activity.findViewById(R.id.meal_plan_detail);

        FirestoreUtil util = new FirestoreUtil();
        SimpleDateFormat sdf = new SimpleDateFormat();// standard time
        sdf.applyPattern("yyyy-MM-dd");// a --> am/pm
        Date date = new Date();// current time
        util.getCalorieIntakeByDate(sdf.format(date), calorieIntakeData);
        util.getCalorieBurnByDate(sdf.format(date), calorieBurnData);
        util.getBurnGoal(curBurnGoal);
        util.getIntakeGoal(curIntakeGoal);
        util.getCurrentWeight(curWeightData);
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

    @Override
    public void onResume() {
        super.onResume();
        //set notification text
        DocumentReference mdoc = userDocRef.collection("goals").document("mealPlan");
        mdoc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot snapshot = task.getResult();
                    if(snapshot.exists()){
//                        Integer plan = (Integer)snapshot.get("plan");
                        Integer plan = (int)(long)snapshot.get("plan");
                        try {
                            setMealPlanNotification(plan, notification);
                        }catch (ParseException e){
                            e.printStackTrace();
                        }
                    }else {
                        notification.setText("Welcome!");
                    }
                }
            }
        });
    }

    private void setMealPlanNotification(Integer plan, TextView notification) throws ParseException {
        if(plan < 2){
            String reminder = "Current Diet: " + FirestoreUtil.getMealPlanStr(plan);
            notification.setText(reminder);
            return;
        }
        String[] starts = {"10:00:00", "10:00:00", "11:00:00"};
        String[] ends = {"18:00:00", "16:00:00", "15:00:00"};
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String date = LocalDate.now().toString();
        Date start = ft.parse(date + " " + starts[plan - 2]);
        Date end = ft.parse(date + " " + ends[plan - 2]);
        Date now = new Date();
        if(isBetween(now, start, end)){
            notification.setText("Inside the Meal Window, grab something to Eat!");
        }else{
            notification.setText("Intermittent Fasting: Stop Eating!");
        }
    }

    private boolean isBetween(Date now, Date start, Date end){
        if (now.getTime() == start.getTime() || now.getTime() == end.getTime()) {
            return true;
        }

        Calendar date = Calendar.getInstance();
        date.setTime(now);

        Calendar begin = Calendar.getInstance();
        begin.setTime(start);

        Calendar ends = Calendar.getInstance();
        ends.setTime(end);

        if (date.after(begin) && date.before(ends)) {
            return true;
        } else {
            return false;
        }
    }

//    private void onCurrentWeightClick(){
//        //test
//        FirestoreUtil util = new FirestoreUtil();
//        util.getCurrentWeight(curWeightData);
//        util.getCalorieIntakeByDate("2020-04-10", calorieIntakeData);
//        util.getCalorieBurnByDate("2020-04-28", calorieBurnData);
//        util.setBurnGoal(300.0, calorieBurnData);
//    }

    public void editCurrentWeight(Double weight){
        FirestoreUtil util = new FirestoreUtil();
        util.setCurrentWeight(weight, curWeightData);
    }

    public void editIntakeGoal(Double calorie){
        FirestoreUtil util = new FirestoreUtil();
        util.setIntakeGoal(calorie, curIntakeGoal);
    }

    public void editBurnGoal(Double calorie){
        FirestoreUtil util = new FirestoreUtil();
        util.setBurnGoal(calorie, curBurnGoal);
    }
}
