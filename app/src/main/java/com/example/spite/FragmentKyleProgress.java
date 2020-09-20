package com.example.spite;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class FragmentKyleProgress extends Fragment {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private TextView kyleWeeklyProg;
    private TextView kyleProgPHTV;

    private String USER_UID = "user01";
    private final String GOAL_KEY = "goal";
    private final String KYLE_UID_KEY = "kyleUID";
    private final String KYLE_NAME_KEY = "kyle";

    private double kGoal = 0.0;

    //Display fragment with layout res file fragment_progress
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_kyle_progress, container, false);
    }

    //Initialising Kyle Weekly Progress View inside the Fragment
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        kyleWeeklyProg = requireView().findViewById(R.id.kyleWeeklyProg);
        kyleProgPHTV = requireView().findViewById(R.id.kyleProgPlaceHoldTV);

        DocumentReference mDocRef = db.collection("User").document(USER_UID);
        mDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                String kyleName = documentSnapshot.getString(KYLE_NAME_KEY);
                String kyleID = documentSnapshot.getString(KYLE_UID_KEY);

                kyleWeeklyProg.setText( kyleName + "'s Weekly Progress");

                DocumentReference kDocRef = db.collection("User").document(kyleID);
                kDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        double kGoal = documentSnapshot.getDouble(GOAL_KEY);
                        String progress = "Kyle's current goal is: " + kGoal + "\nInsert some math about goal and time for percent. \ninsert a graph";
                        kyleProgPHTV.setText( progress );
                    }
                });
            }
        });
    }
}
