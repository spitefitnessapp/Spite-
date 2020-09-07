package com.example.spite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Profile extends AppCompatActivity {

    Button profToMainBtn = null;
    Button profToSetBtn = null;
    Button confirmChangeBtn = null;
    TextView userNameTV = null;
    TextView goalTV = null;
    TextView newGoalTV = null;
    TextView currentGoalNumTV = null;
    EditText userGoalET = null; //cannot use decimal point.

    private String goal = "0.0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profToMainBtn = (Button) findViewById(R.id.profileToMainBtn);
        profToSetBtn = (Button) findViewById(R.id.profToSettBtn);
        confirmChangeBtn = (Button) findViewById(R.id.confirmGoalChangeBtn);
        userGoalET = (EditText) findViewById(R.id.userGoalTimeView);
        userNameTV = (TextView) findViewById(R.id.userName);
        goalTV = (TextView) findViewById(R.id.workoutGoalTV);
        newGoalTV = (TextView) findViewById(R.id.newGoalTV);
        currentGoalNumTV = (TextView) findViewById(R.id.currentGoalTV);

        currentGoalNumTV.setText(goal + " minutes.");

        profToMainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Profile.this, MainActivity.class);
                Profile.this.startActivity(intent);
            }
        });

        profToSetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Profile.this, Settings.class);
                Profile.this.startActivity(intent);
            }
        });

        confirmChangeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            changeWorkoutGoal();
            }
        });
    }

    private void changeWorkoutGoal()
    {
        goal = userGoalET.getText().toString();
        currentGoalNumTV.setText(goal + " minutes.");
    }
}