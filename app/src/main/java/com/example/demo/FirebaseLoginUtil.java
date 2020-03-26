package com.example.demo;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FirebaseLoginUtil {
    //delete current user
    public static void delete(Context context, String password) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser curUser = mAuth.getCurrentUser();
        if(curUser == null){
            return;
        }

        AuthCredential credential = EmailAuthProvider
                .getCredential(curUser.getEmail(), password);
        curUser.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        System.out.println("User re-authenticated.");
                        curUser.delete()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            System.out.println("User account deleted.");
                                        }
                                    }
                                });
                    }
                });

        //go back to login page
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);

        /*
        AuthUI.getInstance()
                .delete(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        System.out.println("Delete success");
                        System.out.println("Fail: " + FirebaseAuth.getInstance().getCurrentUser().getEmail());
                    }
                });
         */
    }

    //sign out current user
    public static void signOutUser(Context context){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();
        FirebaseUser curUser = mAuth.getCurrentUser();
        if(curUser == null){
            System.out.println("sign out success");
        }

        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }
}
