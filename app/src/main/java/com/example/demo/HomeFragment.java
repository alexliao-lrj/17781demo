package com.example.demo;


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
        View.OnClickListener,
        EditCurrentWeightDialogFragment.CurrentWeightListener{

    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;
    private DocumentReference userDocRef;
    private String userKey;

    private View home_healthdata;
    private View home_mealplan;
    private View home_fitnessgoal;
    private View home_wellness_weight;
    private NavController navController;

    private TextView userName;
    private TextView calorieIntakeData;
    private TextView calorieBurnData;
    private TextView curWeightData;

    private TextView originalWeight;

    private EditCurrentWeightDialogFragment editCurrentWeightDialogFragment;
    //meal plan notification view
    //这个notification没有初始化，记得在onActivityCreated里findViewById
    private TextView notification;

    public HomeFragment() {
        // Required empty public constructor
    }

    private void initFirestore(){
        mFirestore = FirebaseFirestore.getInstance();
        userKey = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        userDocRef = mFirestore.collection("users").document(userKey);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        originalWeight = v.findViewById(R.id.curWeight);
        System.out.println("----------------before click");
        originalWeight.setOnClickListener(this);
//        originalWeight.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                System.out.println("-----------------before openDialog");
//                openDialog();
//            }
//        });

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    public void onClick(View v){
        System.out.println("-------------get into textClick");
        openDialog();
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

//        home_wellness_weight = activity.findViewById(R.id.home_wellness_weight);
//        home_wellness_weight.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onCurrentWeightClick();
//            }
//        });

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
                        Integer plan = (Integer)snapshot.get("plan");
                        try {
                            setMealPlanNotification(plan, notification);
                        }catch (ParseException e){
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    private void setMealPlanNotification(Integer plan, TextView notification) throws ParseException {
        if(plan < 2){
            return;
        }
        String[] starts = {"10:00:00", "10:00:00"};
        String[] ends = {"18:00:00", "16:00:00"};
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String date = LocalDate.now().toString();
        Date start = ft.parse(date + " " + starts[plan - 2]);
        Date end = ft.parse(date + " " + ends[plan - 2]);
        Date now = new Date();
        if(isBetween(now, start, end)){
            notification.setText("Inside Meal Window, grab sth to Eat!");
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

    private void onCurrentWeightClick(){
        //test
        FirestoreUtil util = new FirestoreUtil();
        util.getCurrentWeight(curWeightData);
        util.getCalorieIntakeByDate("2020-04-10", calorieIntakeData);
        util.getCalorieBurnByDate("2020-04-28", calorieBurnData);
        util.setBurnGoal(300.0, calorieBurnData);
    }

    public void editCurrentWeight(Double weight, TextView curWeightView){
        FirestoreUtil util = new FirestoreUtil();
        util.setCurrentWeight(weight, curWeightView);
    }
}
