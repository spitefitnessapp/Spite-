//Currently displays values from hardcoded week, 21-09-2020. Searches for doc titles based on current date.
//Attempt to feed in current week as a doc title causes problems when needing to go into previous week

package com.example.spite.fragmentscreens;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.spite.R;
import com.example.spite.models.DailyWorkout;
import com.example.spite.models.WeeklyWorkout;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.DefaultLabelFormatter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import static android.graphics.Color.BLUE;

public class FragmentProgress extends Fragment {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private TextView userWeeklyProg;
    private GraphView graph;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private String USER_UID = user.getUid();
    private final String USERNAME_KEY = "username";
    private final String GOAL_KEY = "goal";
    private final String PROGRESS_KEY = "dailyTimeLogged";

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
                userWeeklyProg.setText(username + "'s Weekly Progress:");

                final LineGraphSeries<DataPoint> goalSeries = new LineGraphSeries<>(new DataPoint[] {
                        new DataPoint(0, goal),
                        new DataPoint(1, goal),
                        new DataPoint(2, goal),
                        new DataPoint(3, goal),
                        new DataPoint(4, goal),
                        new DataPoint(5, goal),
                        new DataPoint(6, goal) });

                goalSeries.setDrawDataPoints(false);
                graph.addSeries(goalSeries);


                final LineGraphSeries<DataPoint> userSeries = new LineGraphSeries<>();
                userSeries.setDrawDataPoints(true);
                userSeries.setDataPointsRadius(10);

                //Aesthetics
                goalSeries.setColor(Color.GRAY);
                goalSeries.setTitle("Goal");
                userSeries.setColor(Color.argb(255, 2, 202, 162));
                userSeries.setTitle("Prog");
                graph.getLegendRenderer().setVisible(true);
                graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
                //graph.getGridLabelRenderer().setVerticalLabelsVisible(false);
                //graph.getGridLabelRenderer().setHorizontalLabelsVisible(false);
                graph.getGridLabelRenderer().setGridColor(Color.WHITE);
                graph.getGridLabelRenderer().setHighlightZeroLines(false);

                //Below two lines change the label color
                graph.getGridLabelRenderer().setVerticalLabelsColor(Color.WHITE);
                graph.getGridLabelRenderer().setHorizontalLabelsColor(Color.WHITE);
                graph.getGridLabelRenderer().reloadStyles();


                //sets X axis label
                graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter(){
                    @Override
                    public String formatLabel( double value, boolean isValueX ){
                        if(isValueX){
                            return "Day " + super.formatLabel(value, isValueX);
                        }
                        return super.formatLabel(value, isValueX);
                    }
                });

                //getting current date for progress
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DATE, -6);
                Date date = cal.getTime();
                DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                String strDate = dateFormat.format(date);
                DateFormat dayFormat = new SimpleDateFormat("EEE");
                String day = dayFormat.format(date);
                final String title = strDate+day;
                Log.d("MAD", "Title -6 passed in is: " + title);


                DocumentReference docRef6 = db.collection("User").document(USER_UID).collection("WeeklyWorkout").document("21-09-2020")
                        .collection("DailyWorkout").document(title);
                docRef6.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d("MAD", "docRef6 docSnap data");
                                double progress = document.getDouble(PROGRESS_KEY);
                                Log.d("MAD", "Progress is: " + progress );
                                userSeries.appendData( new DataPoint( 0, progress ), true, 7, false);

                            } else {
                                Log.d("MAD", "D6: Access previous week");
                                userSeries.appendData( new DataPoint( 0, 0 ), true, 7, false);
                            }

                            Calendar cal = Calendar.getInstance();
                            cal.add(Calendar.DATE, -5);
                            Date date = cal.getTime();
                            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                            String strDate = dateFormat.format(date);
                            DateFormat dayFormat = new SimpleDateFormat("EEE");
                            String day = dayFormat.format(date);
                            final String title = strDate+day;
                            Log.d("MAD", "Title -5 passed in is: " + title);


                            DocumentReference docRef5 = db.collection("User").document(USER_UID).collection("WeeklyWorkout").document("21-09-2020")
                                    .collection("DailyWorkout").document(title);
                            docRef5.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        if (document.exists()) {
                                            Log.d("MAD", "docRef5 docSnap data");
                                            double progress = document.getDouble(PROGRESS_KEY);
                                            Log.d("MAD", "Progress is " + progress );
                                            userSeries.appendData( new DataPoint( 1, progress ), true, 7, false);

                                        } else {
                                            Log.d("MAD", "D5: Access previous week");
                                            userSeries.appendData( new DataPoint( 1, 0 ), true, 7, false);
                                        }

                                        Calendar cal = Calendar.getInstance();
                                        cal.add(Calendar.DATE, -4);
                                        Date date = cal.getTime();
                                        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                                        String strDate = dateFormat.format(date);
                                        DateFormat dayFormat = new SimpleDateFormat("EEE");
                                        String day = dayFormat.format(date);
                                        final String title = strDate+day;
                                        Log.d("MAD", "Title -4 passed in is: " + title);


                                        DocumentReference docRef4 = db.collection("User").document(USER_UID).collection("WeeklyWorkout").document("21-09-2020")
                                                .collection("DailyWorkout").document(title);
                                        docRef4.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    DocumentSnapshot document = task.getResult();
                                                    if (document.exists()) {
                                                        Log.d("MAD", "docRef4 docSnap data");
                                                        double progress = document.getDouble(PROGRESS_KEY);
                                                        Log.d("MAD", "Progress is " + progress );
                                                        userSeries.appendData( new DataPoint( 2, progress ), true, 7, false);

                                                    } else {
                                                        Log.d("MAD", "D4: Access previous week");
                                                        userSeries.appendData( new DataPoint( 2, 0 ), true, 7, false);
                                                    }

                                                    Calendar cal = Calendar.getInstance();
                                                    cal.add(Calendar.DATE, -3);
                                                    Date date = cal.getTime();
                                                    DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                                                    String strDate = dateFormat.format(date);
                                                    DateFormat dayFormat = new SimpleDateFormat("EEE");
                                                    String day = dayFormat.format(date);
                                                    final String title = strDate+day;
                                                    Log.d("MAD", "Title -3 passed in is: " + title);


                                                    DocumentReference docRef3 = db.collection("User").document(USER_UID).collection("WeeklyWorkout").document("21-09-2020")
                                                            .collection("DailyWorkout").document(title);
                                                    docRef3.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                            if (task.isSuccessful()) {
                                                                DocumentSnapshot document = task.getResult();
                                                                if (document.exists()) {
                                                                    Log.d("MAD", "docRef3 docSnap data");
                                                                    double progress = document.getDouble(PROGRESS_KEY);
                                                                    Log.d("MAD", "Progress is " + progress );
                                                                    userSeries.appendData( new DataPoint( 3, progress ), true, 7, false);

                                                                } else {
                                                                    Log.d("MAD", "D3: Access previous week");
                                                                    userSeries.appendData( new DataPoint( 3, 0 ), true, 7, false);
                                                                }

                                                                Calendar cal = Calendar.getInstance();
                                                                cal.add(Calendar.DATE, -2);
                                                                Date date = cal.getTime();
                                                                DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                                                                String strDate = dateFormat.format(date);
                                                                DateFormat dayFormat = new SimpleDateFormat("EEE");
                                                                String day = dayFormat.format(date);
                                                                final String title = strDate+day;
                                                                Log.d("MAD", "Title -2 passed in is: " + title);


                                                                DocumentReference docRef2 = db.collection("User").document(USER_UID).collection("WeeklyWorkout").document("21-09-2020")
                                                                        .collection("DailyWorkout").document(title);
                                                                docRef2.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                        if (task.isSuccessful()) {
                                                                            DocumentSnapshot document = task.getResult();
                                                                            if (document.exists()) {
                                                                                Log.d("MAD", "docRef2 docSnap data");
                                                                                double progress = document.getDouble(PROGRESS_KEY);
                                                                                Log.d("MAD", "Progress is " + progress );
                                                                                userSeries.appendData( new DataPoint( 4, progress ), true, 7, false);

                                                                            } else {
                                                                                Log.d("MAD", "D2: Access previous week");
                                                                                userSeries.appendData( new DataPoint( 4, 0 ), true, 7, false);
                                                                            }

                                                                            Calendar cal = Calendar.getInstance();
                                                                            cal.add(Calendar.DATE, -1);
                                                                            Date date = cal.getTime();
                                                                            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                                                                            String strDate = dateFormat.format(date);
                                                                            DateFormat dayFormat = new SimpleDateFormat("EEE");
                                                                            String day = dayFormat.format(date);
                                                                            final String title = strDate+day;
                                                                            Log.d("MAD", "Title -1 passed in is: " + title);


                                                                            DocumentReference docRef1 = db.collection("User").document(USER_UID).collection("WeeklyWorkout").document("21-09-2020")
                                                                                    .collection("DailyWorkout").document(title);
                                                                            docRef1.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                                @Override
                                                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                                    if (task.isSuccessful()) {
                                                                                        DocumentSnapshot document = task.getResult();
                                                                                        if (document.exists()) {
                                                                                            Log.d("MAD", "docRef1 docSnap data");
                                                                                            double progress = document.getDouble(PROGRESS_KEY);
                                                                                            Log.d("MAD", "Progress is " + progress );
                                                                                            userSeries.appendData( new DataPoint( 5, progress ), true, 7, false);

                                                                                        } else {
                                                                                            Log.d("MAD", "D1: Access previous week");
                                                                                            userSeries.appendData( new DataPoint( 5, 0 ), true, 7, false);
                                                                                        }

                                                                                        Calendar cal = Calendar.getInstance();
                                                                                        Date date = cal.getTime();
                                                                                        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                                                                                        String strDate = dateFormat.format(date);
                                                                                        DateFormat dayFormat = new SimpleDateFormat("EEE");
                                                                                        String day = dayFormat.format(date);
                                                                                        final String title = strDate+day;
                                                                                        Log.d("MAD", "Title -0 passed in is: " + title);


                                                                                        DocumentReference docRef0 = db.collection("User").document(USER_UID).collection("WeeklyWorkout").document("21-09-2020")
                                                                                                .collection("DailyWorkout").document(title);
                                                                                        docRef0.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                                            @Override
                                                                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                                                if (task.isSuccessful()) {
                                                                                                    DocumentSnapshot document = task.getResult();
                                                                                                    if (document.exists()) {
                                                                                                        Log.d("MAD", "docRef0 docSnap data TODAY");
                                                                                                        double progress = document.getDouble(PROGRESS_KEY);
                                                                                                        Log.d("MAD", "Progress is " + progress );
                                                                                                        userSeries.appendData( new DataPoint( 6, progress ), true, 7, false);

                                                                                                    } else {
                                                                                                        Log.d("MAD", "D0: TODAY");
                                                                                                        userSeries.appendData( new DataPoint( 6, 0 ), true, 7, false);
                                                                                                    }
                                                                                                    //add userSeries to the graph
                                                                                                    graph.addSeries(userSeries);

                                                                                                } else {
                                                                                                    //end of docRef0
                                                                                                    Log.d("MAD", "get failed with ", task.getException());
                                                                                                }
                                                                                            }
                                                                                        });

                                                                                    } else {
                                                                                        //end of docRef1
                                                                                        Log.d("MAD", "get failed with ", task.getException());
                                                                                    }
                                                                                }
                                                                            });

                                                                        } else {
                                                                            //end of docRef2
                                                                            Log.d("MAD", "get failed with ", task.getException());
                                                                        }
                                                                    }
                                                                });

                                                            } else {
                                                                //end of docRef3
                                                                Log.d("MAD", "get failed with ", task.getException());
                                                            }
                                                        }
                                                    });

                                                } else {
                                                    //end of docRef4
                                                    Log.d("MAD", "get failed with ", task.getException());
                                                }
                                            }
                                        });

                                    } else {
                                        //end of docRef5
                                        Log.d("MAD", "get failed with ", task.getException());
                                    }
                                }
                            });

                        } else {
                            //end of docRef6
                            Log.d("MAD", "get failed with ", task.getException());
                        }

                    }
                });

            } //out of first documentSnapshot
        });
    }
}
