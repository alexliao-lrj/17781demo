package com.example.demo;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.demo.model.Firesport;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDate;


/**
 * A simple {@link Fragment} subclass.
 */
public class UpdateSportDialogFragment extends DialogFragment implements View.OnClickListener {

    public static final String TAG = "Update Sport Dialog";

    public static final String KEY_SPORT_ID = "key_sport_id";

    private FirebaseFirestore mFirestore;
    private DocumentReference mSportRef;
    private CollectionReference mSportsCollectionRef;
    private Firesport mSport;
    private String sportId;

    private TextView sportName;
    private TextView sportCal;
    private Button cancel, delete;

    interface UpdateSportListener{
        void onSportDeleting(CollectionReference sportsRef, String sportId);
    }

    private UpdateSportListener updateSportListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_update_sport_dialog, container, false);
        sportName = v.findViewById(R.id.sport_name);
        sportCal = v.findViewById(R.id.sport_cal);
        cancel = v.findViewById(R.id.cancel);
        delete = v.findViewById(R.id.delete_sport);
        cancel.setOnClickListener(this);
        delete.setOnClickListener(this);

        mFirestore = FirebaseFirestore.getInstance();
        String userKey = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        String dateKey = LocalDate.now().toString();
        sportId = getArguments().getString(KEY_SPORT_ID);
        mSportsCollectionRef = mFirestore
                .collection("users")
                .document(userKey)
                .collection(dateKey)
                .document("calorieBurn")
                .collection("sports");
        mSportRef = mSportsCollectionRef.document(sportId);
        mSportRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                mSport = documentSnapshot.toObject(Firesport.class);
                sportName.setText(mSport.getName());
                sportCal.setText(String.valueOf(mSport.getTotalCal()));
            }
        });
        return v;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof UpdateSportListener){
            updateSportListener = (UpdateSportListener) context;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cancel:
                onCancelClicked(v);
                break;
            case R.id.delete_sport:
                onDeleteClicked(v);
                break;
        }
    }

    public void onCancelClicked(View view) {
        dismiss();
    }

    public void onDeleteClicked(View view){
        if(updateSportListener != null){
            updateSportListener.onSportDeleting(mSportsCollectionRef, sportId);
        }
        dismiss();
    }
}
