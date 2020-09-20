package com.example.spite;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class FragmentProfile extends Fragment {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Button profToMainBtn;
    private Button profToSetBtn;
    private Button confirmChangeBtn;
    private TextView userNameTV;
    private TextView goalTV;
    private TextView newGoalTV ;
    private TextView currentGoalNumTV;
    private EditText userGoalET; //cannot use decimal point.

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private String USER_UID = user.getUid();
    private final String GOAL_KEY = "goal";
    private final String USERNAME_KEY = "username";

    private String goal = "0.0";
    private String goalTimeFrame = "per week.";

    //Display fragment with layout res file fragment_profile
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    //Set up buttons and views inside the fragment
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        profToMainBtn = requireView().findViewById(R.id.profileToMainBtn);
        profToSetBtn = requireView().findViewById(R.id.profToSettBtn);
        confirmChangeBtn = requireView().findViewById(R.id.confirmGoalChangeBtn);
        userGoalET = requireView().findViewById(R.id.userGoalTimeView);
        userNameTV = requireView().findViewById(R.id.userName);
        goalTV = requireView().findViewById(R.id.workoutGoalTV);
        newGoalTV = requireView().findViewById(R.id.newGoalTV);
        currentGoalNumTV = requireView().findViewById(R.id.currentGoalTV);

        profToMainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                FragmentProfile.this.startActivity(intent);
            }
        });

        profToSetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Settings.class);
                FragmentProfile.this.startActivity(intent);
            }
        });

        confirmChangeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            changeWorkoutGoal();
            }
        });

        DocumentReference kDocRef = db.collection("User").document(USER_UID);
        kDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                String username = documentSnapshot.getString(USERNAME_KEY);
                double goal = documentSnapshot.getDouble(GOAL_KEY);
                userNameTV.setText(username);
                currentGoalNumTV.setText(" " + goal + " minutes " + goalTimeFrame);
            }
        });

    }

    //Method to change the user's weekly workout goal
    private void changeWorkoutGoal()
    {
        goal = userGoalET.getText().toString();
        currentGoalNumTV.setText(goal + " minutes.");

        DocumentReference mDocRef = db.collection("User").document(USER_UID);

        mDocRef
                .update(GOAL_KEY, goal)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("MAD", "goal successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("MAD", "Error updating goal", e);
                    }
                });


    }
}