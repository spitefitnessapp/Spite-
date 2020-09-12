package com.example.spite;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FragmentKyleProgress extends Fragment {

    private TextView kyleWeeklyProg;

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
    }
}
