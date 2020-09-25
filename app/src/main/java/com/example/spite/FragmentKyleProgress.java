package com.example.spite;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

public class FragmentKyleProgress extends Fragment {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private TextView kyleWeeklyProg;
    private GraphView kyleGraph;


    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private String USER_UID = user.getUid();

    private final String GOAL_KEY = "goal";
    private final String KYLE_UID_KEY = "kyleUID";
    private final String KYLE_NAME_KEY = "kyle";

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
        kyleGraph = requireView().findViewById(R.id.kyleGraph);


        //access User in FB, for kyleUID
        DocumentReference mDocRef = db.collection("User").document(USER_UID);
        mDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                String kyleName = documentSnapshot.getString(KYLE_NAME_KEY);
                String kyleID = documentSnapshot.getString(KYLE_UID_KEY);

                kyleWeeklyProg.setText( kyleName + "'s Weekly Progress");

                //Access Kyle's User in FB, for goal/progress
                DocumentReference kDocRef = db.collection("User").document(kyleID);
                kDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        double kGoal = documentSnapshot.getDouble(GOAL_KEY);


                        LineGraphSeries<DataPoint> kyleSeries = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(0, 8),
                                new DataPoint(1, 1),
                                new DataPoint(2, 2),
                                new DataPoint(3, 3),
                                new DataPoint(4, 4),
                                new DataPoint(5, 5),
                                new DataPoint(6, 6),
                                new DataPoint(7, 2),
                        });
                        kyleGraph.addSeries(kyleSeries);

                        kyleSeries.setColor(Color.RED);
                        kyleSeries.setDrawDataPoints(true);
                        kyleSeries.setDataPointsRadius(10);

                    }
                });
            }
        });
    }
}
