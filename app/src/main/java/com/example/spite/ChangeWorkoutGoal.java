package com.example.spite;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class ChangeWorkoutGoal extends AppCompatActivity {

    private ImageButton toSettingsBtn;
    private Button confirmGoalChangeBtn;
    private EditText userGoalET;

    private double goal;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private String USER_UID = user.getUid();
    private final String GOAL_KEY = "goal";
    private final String USERNAME_KEY = "username";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_workout_goal);

        toSettingsBtn = findViewById(R.id.toSettingsBtn);
        confirmGoalChangeBtn = findViewById(R.id.confirmGoalChangeBtn);
        userGoalET = findViewById(R.id.userGoalTimeView);

        confirmGoalChangeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View view )
            {
                changeWorkoutGoal();
                Intent intent = new Intent(ChangeWorkoutGoal.this, Settings.class);
                ChangeWorkoutGoal.this.startActivity(intent);
            }
        });

        toSettingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChangeWorkoutGoal.this, Settings.class);
                ChangeWorkoutGoal.this.startActivity(intent);
            }
        });
    }

    //Method to change the user's weekly workout goal
    private void changeWorkoutGoal()
    {
        String g = userGoalET.getText().toString();
        goal = Double.parseDouble(g);

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