package com.example.spite;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class FragmentProgress extends Fragment {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private TextView userWeeklyProg;
    private TextView userProgPHTV;

    private String USER_UID = "user01";
    private final String USERNAME_KEY = "username";
    private final String GOAL_KEY = "goal";

    private double goal = 0.0;

    //Display fragment with layout res file fragment_home
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_progress, container, false);
    }

    //Set up views inside the fragment
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        userWeeklyProg = requireView().findViewById(R.id.userWeeklyProg);
        userProgPHTV = requireView().findViewById(R.id.userProgPHTV);

        DocumentReference mDocRef = db.collection("User").document(USER_UID);
        mDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                double goal = documentSnapshot.getDouble(GOAL_KEY);
                String username = documentSnapshot.getString(USERNAME_KEY);
                userWeeklyProg.setText(username + "'s weekly progress.");
                String progress = "User current goal is: " + goal + "\nInsert some math about goal and time for percent. \ninsert a graph";
                userProgPHTV.setText( progress );


            }
        });
    }
}