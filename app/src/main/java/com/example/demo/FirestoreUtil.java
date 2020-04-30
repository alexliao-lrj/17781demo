package com.example.demo;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class FirestoreUtil {
    private static final String TAG = "FirestoreUtil";

    private FirebaseFirestore mFirestore;
    private DocumentReference userDocRef;
    private String userKey;

    public FirestoreUtil(){
        mFirestore = FirebaseFirestore.getInstance();
        userKey = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        userDocRef = mFirestore.collection("users").document(userKey);
    }

    //dateKey format: "2020-04-28"
    private CollectionReference getCollectionRefByDate(String dateKey){
        return userDocRef.collection(dateKey);
    }

    public void setMealPlan(Integer plan, TextView planView){
        DocumentReference mdoc = userDocRef.collection("goals").document("mealPlan");
        Map<String, Integer> map = new HashMap<>();
        map.put("plan", plan);
        String mealPlan = getMealPlanStr(plan);
        mdoc.set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                planView.setText(mealPlan);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println(TAG + "set mealplan failed");
            }
        });
    }

    public String getMealPlanStr(Integer plan){
        //0
        String str = "Balanced Diet";
        switch (plan){
            case 1:
                str = "5 : 2 Fast Diet";
                break;
            case 2:
                str = "16 : 8 Intermittent Fasting";
                break;
            case 3:
                str = "18 : 6 Intermittent Fasting";
                break;
            case 4:
                str = "20 : 4 Intermittent Fasting";
                break;
            default:
                break;
        }
        return str;
    }

    public void getMealPlan(TextView planView){
        DocumentReference mdoc = userDocRef.collection("goals").document("mealPlan");
        mdoc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot snapshot = task.getResult();
                    if(snapshot.exists()){
//                        Integer plan = (Integer)snapshot.get("plan");
                        Integer plan = (int)(long)snapshot.get("plan");
                        String planStr = getMealPlanStr(plan);
                        planView.setText(planStr);
                    }
                }
            }
        });
    }

    public void setCurrentWeight(Double weight, TextView curWeightView){
        String dateKey = LocalDate.now().toString();
        DocumentReference weightByDate = userDocRef.collection("weights").document(dateKey);
        Map<String, Double> map = new HashMap<>();
        map.put("weight", weight);
        weightByDate
                .set(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        curWeightView.setText(String.valueOf(weight.intValue()) + " lbs");
                        System.out.println(TAG + "set weight success");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println(TAG + "set weight failed");
                    }
                });
    }

    public void getCurrentWeight(TextView weightView){
        String dateKey = LocalDate.now().toString();
        DocumentReference weightByDate = userDocRef.collection("weights").document(dateKey);
        weightByDate.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot snapshot = task.getResult();
                    if(snapshot.exists()){
                        Double cur = snapshot.getDouble("weight");
                        weightView.setText(String.valueOf(cur.intValue()) + " lbs");
                    }
                }
            }
        });
    }

    public void setIntakeGoal(Double intakeGoal, TextView intakeView){
        DocumentReference igDoc = userDocRef.collection("goals").document("intakeGoal");
        Map<String, Double> map = new HashMap<>();
        map.put("intake", intakeGoal);
        igDoc.set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                intakeView.setText("/ " + String.valueOf(intakeGoal.intValue()) + " kcal");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println(TAG + " set intake goal failed");
            }
        });
    }

    public void setBurnGoal(Double burnGoal, TextView burnView){
        DocumentReference bgDoc = userDocRef.collection("goals").document("burnGoal");
        Map<String, Double> map = new HashMap<>();
        map.put("burn", burnGoal);
        bgDoc.set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                burnView.setText("/ " + String.valueOf(burnGoal.intValue()) + " kcal");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println(TAG + " set burn goal failed");
            }
        });
    }

    //set target weight
    public void setWeightGoal(Double weightGoal, TextView weightGoalView){
        DocumentReference wgDoc = userDocRef.collection("goals").document("weightGoal");
        Map<String, Double> map = new HashMap<>();
        map.put("target", weightGoal);
        wgDoc.set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                weightGoalView.setText(String.valueOf(weightGoal.intValue()) + " lbs");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println(TAG + " set target weight failed");
            }
        });
    }

    //get target weight
    public void getWeightGoal(TextView targetView){
        DocumentReference wgDoc = userDocRef.collection("goals").document("weightGoal");
        wgDoc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot snapshot = task.getResult();
                    if(snapshot.exists()){
                        Double target = snapshot.getDouble("target");
                        targetView.setText(String.valueOf(target.intValue()) + " lbs");
                    }
                }
            }
        });
    }

    public void getBurnGoal(TextView burnView){
        DocumentReference bgDoc = userDocRef.collection("goals").document("burnGoal");
        bgDoc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()){
                        Double burn = (Double)document.get("burn");
                        burnView.setText("/ "+String.valueOf(burn.intValue()) + " kcal");
                    }
                }
            }
        });
    }

    public void getIntakeGoal(TextView intakeView){
        DocumentReference igDoc = userDocRef.collection("goals").document("intakeGoal");
        igDoc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot snapshot = task.getResult();
                    if(snapshot.exists()){
                        Double intake = (Double)snapshot.get("intake");
                        intakeView.setText("/ "+String.valueOf(intake.intValue()) + " kcal");
                    }
                }
            }
        });
    }

    //get total calorie intake on day 'dateKey', set 'intakeView' content to the total intake data
    public void getCalorieIntakeByDate(String dateKey, TextView intakeView){
        DocumentReference df = getCollectionRefByDate(dateKey).document("calorieIntake");
        df.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Double totalIntake = (Double)document.get("Total");
                        //set totalIntake here
                        intakeView.setText(String.valueOf(totalIntake.intValue()));
                        System.out.println(TAG + " DocumentSnapshot data: " + document.getData());
                    } else {
                        System.out.println(TAG + " No such document");
                    }
                } else {
                    System.out.println(TAG + " get failed with " + task.getException());
                }
            }
        });
    }

    public void getCalorieBurnByDate(String dateKey, TextView burnView){
        DocumentReference df = getCollectionRefByDate(dateKey).document("calorieBurn");
        df.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Double totalBurn = (Double)document.get("Total");
                        //set totalBurn here
                        burnView.setText(String.valueOf(totalBurn.intValue()));

                        System.out.println(TAG + " DocumentSnapshot data: " + document.getData());
                    } else {
                        System.out.println(TAG + " No such document");
                    }
                } else {
                    System.out.println(TAG + " get failed with " + task.getException());
                }
            }
        });
    }

}
