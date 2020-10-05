package com.example.spite.fragmentscreens;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.spite.MainActivity;
import com.example.spite.R;
import com.example.spite.Settings;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class FragmentProfile extends Fragment {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ImageButton settingsBtn;
    private Button confirmChangeBtn;
    private TextView userNameTV;
    private TextView goalTV;
    private TextView newGoalTV ;
    private TextView currentGoalNumTV;
    private TextView timeFrame;
    private EditText userGoalET; //cannot use decimal point.

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private String USER_UID = user.getUid();
    private final String GOAL_KEY = "goal";
    private final String USERNAME_KEY = "username";

    private double goal = 0.0;
    private String goalTimeFrame = "Daily Workout Goal";

    //Display fragment with layout res file fragment_profile
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    //Set up buttons and views inside the fragment
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        settingsBtn = requireView().findViewById(R.id.settingsBtn);
        confirmChangeBtn = requireView().findViewById(R.id.confirmGoalChangeBtn);
        userGoalET = requireView().findViewById(R.id.userGoalTimeView);
        userNameTV = requireView().findViewById(R.id.userName);
        timeFrame = requireView().findViewById(R.id.timeFrame);
        currentGoalNumTV = requireView().findViewById(R.id.currentGoalTV);
        timeFrame.setText(goalTimeFrame);

        settingsBtn.setOnClickListener(new View.OnClickListener() {
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

        //Access DB for current user stats
        DocumentReference kDocRef = db.collection("User").document(USER_UID);
        kDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                String username = documentSnapshot.getString(USERNAME_KEY);
                double goal = documentSnapshot.getDouble(GOAL_KEY);
                userNameTV.setText(username);
                currentGoalNumTV.setText(" " + goal + " min ");
            }
        });

    }

    //Method to change the user's weekly workout goal
    private void changeWorkoutGoal()
    {
        String g = userGoalET.getText().toString();
        goal = Double.parseDouble(g);
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