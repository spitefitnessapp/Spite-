package com.example.spite;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import static android.graphics.Color.BLUE;

public class FragmentProgress extends Fragment {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private TextView userWeeklyProg;
    private GraphView graph;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private String USER_UID = user.getUid();
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
        graph = requireView().findViewById(R.id.userGraph);


        //Access DB for User progress
        DocumentReference mDocRef = db.collection("User").document(USER_UID);
        mDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                double goal = documentSnapshot.getDouble(GOAL_KEY);
                String username = documentSnapshot.getString(USERNAME_KEY);
                userWeeklyProg.setText(username + "'s weekly progress.");


                LineGraphSeries<DataPoint> userSeries = new LineGraphSeries<>(new DataPoint[] {
                        new DataPoint(0, 8),
                        new DataPoint(1, 1),
                        new DataPoint(2, 2),
                        new DataPoint(3, 3),
                        new DataPoint(4, 4),
                        new DataPoint(5, 5),
                        new DataPoint(6, 6),
                        new DataPoint(7, 2),
                });graph.addSeries(userSeries);

                userSeries.setDrawDataPoints(true);
                userSeries.setDataPointsRadius(10);
/*
                graph.getViewport().setBackgroundColor(Color.argb(255, 222, 222, 222));
                graph.getViewport().setDrawBorder(true);
                graph.getViewport().setBorderColor(BLUE);

 */

            }
        });
    }
}