package com.example.spite;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FragmentProgress extends Fragment {

    private TextView userWeeklyProg;

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
    }
}