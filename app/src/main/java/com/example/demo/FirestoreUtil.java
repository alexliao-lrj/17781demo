package com.example.demo;

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
    private Query mQuery;
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

    public void setCurrentWeight(Double weight){
        String dateKey = LocalDate.now().toString();
        DocumentReference weightByDate = userDocRef.collection("weights").document(dateKey);
        Map<String, Double> map = new HashMap<>();
        map.put("weight", weight);
        weightByDate
                .set(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        System.out.println(TAG + "set weight success");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println(TAG + "set weight failed");
                    }
                });
    }

    public void getCalorieIntakeByDate(String dateKey){
        DocumentReference df = getCollectionRefByDate(dateKey).document("calorieIntake");
        df.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Double totalIntake = (Double)document.get("Total");
                        //set totalIntake here
                        System.out.println(TAG + " Total Intake: " + totalIntake);

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

    public void getCalorieBurnByDate(String dateKey){
        DocumentReference df = getCollectionRefByDate(dateKey).document("calorieBurn");
        df.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Double totalBurn = (Double)document.get("Total");
                        //set totalBurn here
                        System.out.println(TAG + " Total Burn: " + totalBurn);

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
