package com.example.spite;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class EndWorkout extends AppCompatActivity {

    private Button toMainBtn;
    private Button toProgressBtn;
    private TextView goalTitleTV;
    private TextView loggedTitleTV;
    private TextView workoutGoalTV;
    private TextView loggedTimeTV;

    private String emailIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_workout);

        toMainBtn = findViewById(R.id.endToMainBtn);
        toProgressBtn = findViewById(R.id.endToProgressBtn);
        goalTitleTV = findViewById(R.id.endGoalTitle);
        loggedTitleTV = findViewById(R.id.endLoggedTitle);
        workoutGoalTV = findViewById(R.id.endWorkoutGoal);
        loggedTimeTV = findViewById(R.id.endLoggedTime);


        /*Sends user back to the FragmentHome*/
        toMainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EndWorkout.this, MainActivity.class);
                EndWorkout.this.startActivity(intent);
            }
        });

        /*TODO: Send user to FragmentProgress. Temporarily sends user to FragmentHome*/
        toProgressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EndWorkout.this, MainActivity.class);
                intent.putExtra("TabView", "EndWorkoutToProgress");
                startActivity(intent);
            }
        });

        /*Shows the user their Set Workout Goal and their Logged Time for their workout for comparison*/
        Intent getWorkoutGoal = getIntent();
        int hour = getWorkoutGoal.getIntExtra("hour",0);
        int minute = getWorkoutGoal.getIntExtra("minute", 0);
        String goal = hour + ":" + minute;
        workoutGoalTV.setText(goal);

        String loggedTime = getWorkoutGoal.getStringExtra("loggedTime");
        loggedTimeTV.setText(loggedTime);

    }
}