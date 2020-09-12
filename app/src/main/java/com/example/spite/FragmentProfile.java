package com.example.spite;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FragmentProfile extends Fragment {

    private Button profToMainBtn;
    private Button profToSetBtn;
    private Button confirmChangeBtn;
    private TextView userNameTV;
    private TextView goalTV;
    private TextView newGoalTV ;
    private TextView currentGoalNumTV;
    private EditText userGoalET; //cannot use decimal point.

    private String goal = "0.0";

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

        currentGoalNumTV.setText(goal + " minutes.");

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
    }

    //Method to change the user's weekly workout goal
    private void changeWorkoutGoal()
    {
        goal = userGoalET.getText().toString();
        currentGoalNumTV.setText(goal + " minutes.");
    }
}