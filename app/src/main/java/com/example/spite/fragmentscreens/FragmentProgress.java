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
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

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
    private final String PROGRESS_KEY = "timeLogged";

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
/*
                graph.getViewport().setBackgroundColor(Color.argb(255, 222, 222, 222));
                graph.getViewport().setDrawBorder(true);
                graph.getViewport().setBorderColor(BLUE);

 */


                //Query the database to find the most recently created WeeklyWorkout doc for the specified user
                Task<QuerySnapshot> mostRecentWeek = db.collection("User").document("user01")
                        .collection("WeeklyWorkout")
                        .orderBy("date", Query.Direction.DESCENDING)
                        .limit(1)
                        .get()

                        //On success, we store the date of the most recent week and proceed to create a DailyWorkout document within
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                                //Go through the queries to retrieve the Date string of the most recent week
                                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                    WeeklyWorkout recentWeek = documentSnapshot.toObject(WeeklyWorkout.class);

                                    final String recentWeekDate = recentWeek.getDateString();
                                    Log.d("MAD", "Here's the date of the week: " + recentWeekDate);

                                    Calendar cal = Calendar.getInstance();
                                    cal.add(Calendar.DATE, -6);
                                    Date date = cal.getTime();
                                    DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                                    String strDate = dateFormat.format(date);
                                    DateFormat dayFormat = new SimpleDateFormat("EEE");
                                    String day = dayFormat.format(date);
                                    final String title = strDate+day;
                                    Log.d("MAD", "Title -6 passed in is: " + title);

                                    //gets progress from 6 days ago
                                    DocumentReference docRef6 = db.collection("User").document("user01").collection("WeeklyWorkout").document(recentWeekDate)
                                            .collection("DailyWorkout").document(title);
                                    docRef6.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                            if (task.isSuccessful()) {
                                                DocumentSnapshot document = task.getResult();
                                                if (document.exists()) {

                                                    double progress = document.getDouble(PROGRESS_KEY);
                                                    Log.d("MAD", "6: x is: " + 0 );
                                                    userSeries.appendData( new DataPoint( 0, progress ), true, 7, false);

                                                } else {
                                                    Log.d("MAD","Access previous week document d6");
                                                    /*Task<QuerySnapshot> mostRecentWeek = db.collection("User").document("user01")
                                                            .collection("WeeklyWorkout")
                                                            .orderBy("date", Query.Direction.DESCENDING)
                                                            .limit(2)
                                                            .get()

                                                            //On success, we store the date of the most recent week and proceed to find the most recent DailyWorkout doc
                                                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                                @Override
                                                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                                    String WeekDate = "";

                                                                    //Go through the queries to retrieve the Date string of the most recent week
                                                                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                                                        WeeklyWorkout recentWeek = documentSnapshot.toObject(WeeklyWorkout.class);

                                                                        String date = recentWeek.getDateString();

                                                                        WeekDate = date;
                                                                    }

                                                                    //Date of the most recent week
                                                                    final String secondRecentWeekDate = WeekDate;

                                                                    DocumentReference docRef = db.collection("User").document("user01").collection("WeeklyWorkout").document(secondRecentWeekDate)
                                                                            .collection("DailyWorkout").document(title);
                                                                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                            if (task.isSuccessful()) {
                                                                                DocumentSnapshot document = task.getResult();
                                                                                if (document.exists()) {

                                                                                    double progress = document.getDouble(PROGRESS_KEY);
                                                                                    Log.d("MAD", "6.2 x value is: " + 0 );
                                                                                    userSeries.appendData( new DataPoint( 0, progress ), true, 7, false);

                                                                                } else {
                                                                                    Log.d("MAD", "No such document");
                                                                                }
                                                                            } else {
                                                                                Log.d("MAD", "get failed with ", task.getException());
                                                                            }
                                                                        }
                                                                    });
                                                                }
                                                            })

                                                            //On failure, we catch the error
                                                            .addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    Log.d("Recent Week: ", e.toString());
                                                                }
                                                            });
                                                */}
                                                //graph.addSeries(userSeries);

                                                Calendar cal = Calendar.getInstance();
                                                cal.add(Calendar.DATE, -5);
                                                Date date = cal.getTime();
                                                DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                                                String strDate = dateFormat.format(date);
                                                DateFormat dayFormat = new SimpleDateFormat("EEE");
                                                String day = dayFormat.format(date);
                                                final String title = strDate+day;
                                                Log.d("MAD", "Title -5 passed in is: " + title);

                                                //progress from 5 days ago
                                                DocumentReference docRef5 = db.collection("User").document("user01").collection("WeeklyWorkout").document(recentWeekDate)
                                                        .collection("DailyWorkout").document(title);
                                                docRef5.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                                        if (task.isSuccessful()) {
                                                            DocumentSnapshot document = task.getResult();
                                                            if (document.exists()) {

                                                                double progress = document.getDouble(PROGRESS_KEY);
                                                                Log.d("MAD", "5 x value is: " + 1 );
                                                                userSeries.appendData( new DataPoint( 1, progress ), true, 7, false);

                                                            } else {
                                                                Log.d("MAD","Access previous week document");
                                                                /*Task<QuerySnapshot> mostRecentWeek = db.collection("User").document("user01")
                                                                        .collection("WeeklyWorkout")
                                                                        .orderBy("date", Query.Direction.DESCENDING)
                                                                        .limit(2)
                                                                        .get()

                                                                        //On success, we store the date of the most recent week and proceed to find the most recent DailyWorkout doc
                                                                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                                            @Override
                                                                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                                                String WeekDate = "";

                                                                                //Go through the queries to retrieve the Date string of the most recent week
                                                                                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                                                                    WeeklyWorkout recentWeek = documentSnapshot.toObject(WeeklyWorkout.class);

                                                                                    String date = recentWeek.getDateString();
                                                                                    WeekDate = date;
                                                                                }

                                                                                //Date of the most recent week
                                                                                final String secondRecentWeekDate = WeekDate;

                                                                                DocumentReference docRef = db.collection("User").document("user01").collection("WeeklyWorkout").document(secondRecentWeekDate)
                                                                                        .collection("DailyWorkout").document(title);
                                                                                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                                    @Override
                                                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                                        if (task.isSuccessful()) {
                                                                                            DocumentSnapshot document = task.getResult();
                                                                                            if (document.exists()) {

                                                                                                double progress = document.getDouble(PROGRESS_KEY);
                                                                                                Log.d("MAD", "5.2 x value is: " + 1 );
                                                                                                userSeries.appendData( new DataPoint( 1, progress ), true, 7, false);

                                                                                            } else {
                                                                                                Log.d("MAD", "No such document");
                                                                                            }
                                                                                        } else {
                                                                                            Log.d("MAD", "get failed with ", task.getException());
                                                                                        }
                                                                                    }
                                                                                });
                                                                            }
                                                                        })

                                                                        //On failure, we catch the error
                                                                        .addOnFailureListener(new OnFailureListener() {
                                                                            @Override
                                                                            public void onFailure(@NonNull Exception e) {
                                                                                Log.d("Recent Week: ", e.toString());
                                                                            }
                                                                        });
                                                                 */
                                                            }
                                                            //graph.addSeries(userSeries);

                                                            Calendar cal = Calendar.getInstance();
                                                            cal.add(Calendar.DATE, -4);
                                                            Date date = cal.getTime();
                                                            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                                                            String strDate = dateFormat.format(date);
                                                            DateFormat dayFormat = new SimpleDateFormat("EEE");
                                                            String day = dayFormat.format(date);
                                                            final String title = strDate+day;
                                                            Log.d("MAD", "Title -4 passed in is: " + title);

                                                            //progress from 4 days ago
                                                            DocumentReference docRef4 = db.collection("User").document("user01").collection("WeeklyWorkout").document(recentWeekDate)
                                                                    .collection("DailyWorkout").document(title);
                                                            docRef4.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                                                    if (task.isSuccessful()) {
                                                                        DocumentSnapshot document = task.getResult();
                                                                        if (document.exists()) {

                                                                            double progress = document.getDouble(PROGRESS_KEY);
                                                                            Log.d("MAD", "4 x value is: " + 2 );
                                                                            userSeries.appendData( new DataPoint( 2, progress ), true, 7, false);

                                                                        } else {
                                                                            Log.d("MAD","Access previous week document");
                                                                            /*Task<QuerySnapshot> mostRecentWeek = db.collection("User").document("user01")
                                                                                    .collection("WeeklyWorkout")
                                                                                    .orderBy("date", Query.Direction.DESCENDING)
                                                                                    .limit(2)
                                                                                    .get()

                                                                                    //On success, we store the date of the most recent week and proceed to find the most recent DailyWorkout doc
                                                                                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                                                        @Override
                                                                                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                                                            String WeekDate = "";

                                                                                            //Go through the queries to retrieve the Date string of the most recent week
                                                                                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                                                                                WeeklyWorkout recentWeek = documentSnapshot.toObject(WeeklyWorkout.class);

                                                                                                String date = recentWeek.getDateString();
                                                                                                WeekDate = date;
                                                                                            }

                                                                                            //Date of the most recent week
                                                                                            final String secondRecentWeekDate = WeekDate;

                                                                                            DocumentReference docRef = db.collection("User").document("user01").collection("WeeklyWorkout").document(secondRecentWeekDate)
                                                                                                    .collection("DailyWorkout").document(title);
                                                                                            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                                                @Override
                                                                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                                                    if (task.isSuccessful()) {
                                                                                                        DocumentSnapshot document = task.getResult();
                                                                                                        if (document.exists()) {

                                                                                                            double progress = document.getDouble(PROGRESS_KEY);
                                                                                                            Log.d("MAD", "4.2 x value is: " + 2 );
                                                                                                            userSeries.appendData( new DataPoint( 2, progress ), true, 7, false);

                                                                                                        } else {
                                                                                                            Log.d("MAD", "No such document");
                                                                                                        }
                                                                                                    } else {
                                                                                                        Log.d("MAD", "get failed with ", task.getException());
                                                                                                    }
                                                                                                }
                                                                                            });
                                                                                        }
                                                                                    })

                                                                                    //On failure, we catch the error
                                                                                    .addOnFailureListener(new OnFailureListener() {
                                                                                        @Override
                                                                                        public void onFailure(@NonNull Exception e) {
                                                                                            Log.d("Recent Week: ", e.toString());
                                                                                        }
                                                                                    });
                                                                                    */
                                                                        }
                                                                        //graph.addSeries(userSeries);

                                                                        Calendar cal = Calendar.getInstance();
                                                                        cal.add(Calendar.DATE, -3);
                                                                        Date date = cal.getTime();
                                                                        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                                                                        String strDate = dateFormat.format(date);
                                                                        DateFormat dayFormat = new SimpleDateFormat("EEE");
                                                                        String day = dayFormat.format(date);
                                                                        final String title = strDate+day;
                                                                        Log.d("MAD", "Title -3 passed in is: " + title);

                                                                        //progress from 3 days ago
                                                                        DocumentReference docRef3 = db.collection("User").document("user01").collection("WeeklyWorkout").document(recentWeekDate)
                                                                                .collection("DailyWorkout").document(title);
                                                                        docRef3.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                                                                if (task.isSuccessful()) {
                                                                                    DocumentSnapshot document = task.getResult();
                                                                                    if (document.exists()) {

                                                                                        double progress = document.getDouble(PROGRESS_KEY);
                                                                                        Log.d("MAD", "3 x value is: " + 3 );
                                                                                        userSeries.appendData( new DataPoint( 3, progress ), true, 7, false);

                                                                                    } else {
                                                                                        Log.d("MAD","Access previous week document");
                                                                                        /*Task<QuerySnapshot> mostRecentWeek = db.collection("User").document("user01")
                                                                                                .collection("WeeklyWorkout")
                                                                                                .orderBy("date", Query.Direction.DESCENDING)
                                                                                                .limit(2)
                                                                                                .get()

                                                                                                //On success, we store the date of the most recent week and proceed to find the most recent DailyWorkout doc
                                                                                                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                                                                    @Override
                                                                                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                                                                        String WeekDate = "";

                                                                                                        //Go through the queries to retrieve the Date string of the most recent week
                                                                                                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                                                                                            WeeklyWorkout recentWeek = documentSnapshot.toObject(WeeklyWorkout.class);

                                                                                                            String date = recentWeek.getDateString();
                                                                                                            WeekDate = date;
                                                                                                        }

                                                                                                        //Date of the most recent week
                                                                                                        final String secondRecentWeekDate = WeekDate;

                                                                                                        DocumentReference docRef = db.collection("User").document("user01").collection("WeeklyWorkout").document(secondRecentWeekDate)
                                                                                                                .collection("DailyWorkout").document(title);
                                                                                                        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                                                            @Override
                                                                                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                                                                if (task.isSuccessful()) {
                                                                                                                    DocumentSnapshot document = task.getResult();
                                                                                                                    if (document.exists()) {

                                                                                                                        double progress = document.getDouble(PROGRESS_KEY);
                                                                                                                        Log.d("MAD", "3.2 x value is: " + 3 );
                                                                                                                        userSeries.appendData( new DataPoint( 3, progress ), true, 7, false);

                                                                                                                    } else {
                                                                                                                        Log.d("MAD", "No such document");
                                                                                                                    }
                                                                                                                } else {
                                                                                                                    Log.d("MAD", "get failed with ", task.getException());
                                                                                                                }
                                                                                                            }
                                                                                                        });
                                                                                                    }
                                                                                                })

                                                                                                //On failure, we catch the error
                                                                                                .addOnFailureListener(new OnFailureListener() {
                                                                                                    @Override
                                                                                                    public void onFailure(@NonNull Exception e) {
                                                                                                        Log.d("Recent Week: ", e.toString());
                                                                                                    }
                                                                                                });
                                                                                                */
                                                                                    }
                                                                                    //graph.addSeries(userSeries);

                                                                                    Calendar cal = Calendar.getInstance();
                                                                                    cal.add(Calendar.DATE, -2);
                                                                                    Date date = cal.getTime();
                                                                                    DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                                                                                    String strDate = dateFormat.format(date);
                                                                                    DateFormat dayFormat = new SimpleDateFormat("EEE");
                                                                                    String day = dayFormat.format(date);
                                                                                    final String title = strDate+day;
                                                                                    Log.d("MAD", "Title -2 passed in is: " + title);

                                                                                    //progress from 2 days ago
                                                                                    DocumentReference docRef2 = db.collection("User").document("user01").collection("WeeklyWorkout").document(recentWeekDate)
                                                                                            .collection("DailyWorkout").document(title);
                                                                                    docRef2.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                                        @Override
                                                                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                                                                            if (task.isSuccessful()) {
                                                                                                DocumentSnapshot document = task.getResult();
                                                                                                if (document.exists()) {

                                                                                                    double progress = document.getDouble(PROGRESS_KEY);
                                                                                                    Log.d("MAD", "2 x value is: " + 4 );
                                                                                                    userSeries.appendData( new DataPoint( 4, progress ), true, 7, false);


                                                                                                } else {
                                                                                                    Log.d("MAD","Access previous week document");
                                                                                                    /*Task<QuerySnapshot> mostRecentWeek = db.collection("User").document("user01")
                                                                                                            .collection("WeeklyWorkout")
                                                                                                            .orderBy("date", Query.Direction.DESCENDING)
                                                                                                            .limit(2)
                                                                                                            .get()

                                                                                                            //On success, we store the date of the most recent week and proceed to find the most recent DailyWorkout doc
                                                                                                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                                                                                @Override
                                                                                                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                                                                                    String WeekDate = "";

                                                                                                                    //Go through the queries to retrieve the Date string of the most recent week
                                                                                                                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                                                                                                        WeeklyWorkout recentWeek = documentSnapshot.toObject(WeeklyWorkout.class);

                                                                                                                        String date = recentWeek.getDateString();
                                                                                                                        WeekDate = date;
                                                                                                                    }

                                                                                                                    //Date of the most recent week
                                                                                                                    final String secondRecentWeekDate = WeekDate;

                                                                                                                    DocumentReference docRef = db.collection("User").document("user01").collection("WeeklyWorkout").document(secondRecentWeekDate)
                                                                                                                            .collection("DailyWorkout").document(title);
                                                                                                                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                                                                        @Override
                                                                                                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                                                                            if (task.isSuccessful()) {
                                                                                                                                DocumentSnapshot document = task.getResult();
                                                                                                                                if (document.exists()) {

                                                                                                                                    double progress = document.getDouble(PROGRESS_KEY);
                                                                                                                                    Log.d("MAD", "2.2 x value is: " + 4 );
                                                                                                                                    userSeries.appendData( new DataPoint( 4, progress ), true, 7, false);

                                                                                                                                } else {
                                                                                                                                    Log.d("MAD", "No such document");
                                                                                                                                }
                                                                                                                            } else {
                                                                                                                                Log.d("MAD", "get failed with ", task.getException());
                                                                                                                            }
                                                                                                                        }
                                                                                                                    });
                                                                                                                }
                                                                                                            })

                                                                                                            //On failure, we catch the error
                                                                                                            .addOnFailureListener(new OnFailureListener() {
                                                                                                                @Override
                                                                                                                public void onFailure(@NonNull Exception e) {
                                                                                                                    Log.d("Recent Week: ", e.toString());
                                                                                                                }
                                                                                                            });
                                                                                                            */
                                                                                                }
                                                                                                //graph.addSeries(userSeries);

                                                                                                Calendar cal = Calendar.getInstance();
                                                                                                cal.add(Calendar.DATE, -1);
                                                                                                Date date = cal.getTime();
                                                                                                DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                                                                                                String strDate = dateFormat.format(date);
                                                                                                DateFormat dayFormat = new SimpleDateFormat("EEE");
                                                                                                String day = dayFormat.format(date);
                                                                                                final String title = strDate+day;
                                                                                                Log.d("MAD", "Title -1 passed in is: " + title);

                                                                                                //progress from yesterday
                                                                                                DocumentReference docRef1 = db.collection("User").document("user01").collection("WeeklyWorkout").document(recentWeekDate)
                                                                                                        .collection("DailyWorkout").document(title);
                                                                                                docRef1.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                                                    @Override
                                                                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                                                                                        if (task.isSuccessful()) {
                                                                                                            DocumentSnapshot document = task.getResult();
                                                                                                            if (document.exists()) {

                                                                                                                double progress = document.getDouble(PROGRESS_KEY);
                                                                                                                Log.d("MAD", "1 x value is: " + 5 );
                                                                                                                userSeries.appendData( new DataPoint( 5, progress ), true, 7, false);

                                                                                                            } else {
                                                                                                                Log.d("MAD","Access previous week document");
                                                                                                                /*Task<QuerySnapshot> mostRecentWeek = db.collection("User").document("user01")
                                                                                                                        .collection("WeeklyWorkout")
                                                                                                                        .orderBy("date", Query.Direction.DESCENDING)
                                                                                                                        .limit(2)
                                                                                                                        .get()

                                                                                                                        //On success, we store the date of the most recent week and proceed to find the most recent DailyWorkout doc
                                                                                                                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                                                                                            @Override
                                                                                                                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                                                                                                String WeekDate = "";

                                                                                                                                //Go through the queries to retrieve the Date string of the most recent week
                                                                                                                                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                                                                                                                    WeeklyWorkout recentWeek = documentSnapshot.toObject(WeeklyWorkout.class);

                                                                                                                                    String date = recentWeek.getDateString();
                                                                                                                                    WeekDate = date;
                                                                                                                                }

                                                                                                                                //Date of the most recent week
                                                                                                                                final String secondRecentWeekDate = WeekDate;

                                                                                                                                DocumentReference docRef = db.collection("User").document("user01").collection("WeeklyWorkout").document(secondRecentWeekDate)
                                                                                                                                        .collection("DailyWorkout").document(title);
                                                                                                                                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                                                                                    @Override
                                                                                                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                                                                                        if (task.isSuccessful()) {
                                                                                                                                            DocumentSnapshot document = task.getResult();
                                                                                                                                            if (document.exists()) {

                                                                                                                                                double progress = document.getDouble(PROGRESS_KEY);
                                                                                                                                                Log.d("MAD", "1 x value is: " + 5 );
                                                                                                                                                userSeries.appendData( new DataPoint( 5, progress ), true, 7, false);

                                                                                                                                            } else {
                                                                                                                                                Log.d("MAD", "No such document");
                                                                                                                                            }
                                                                                                                                        } else {
                                                                                                                                            Log.d("MAD", "get failed with ", task.getException());
                                                                                                                                        }
                                                                                                                                    }
                                                                                                                                });
                                                                                                                            }
                                                                                                                        })
                                                                                                                        //On failure, we catch the error
                                                                                                                        .addOnFailureListener(new OnFailureListener() {
                                                                                                                            @Override
                                                                                                                            public void onFailure(@NonNull Exception e) {
                                                                                                                                Log.d("Recent Week: ", e.toString());
                                                                                                                            }
                                                                                                                        });
                                                                                                                        */
                                                                                                            }
                                                                                                            //graph.addSeries(userSeries);

                                                                                                            Calendar cal = Calendar.getInstance();
                                                                                                            Date date = cal.getTime();
                                                                                                            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                                                                                                            String strDate = dateFormat.format(date);
                                                                                                            DateFormat dayFormat = new SimpleDateFormat("EEE");
                                                                                                            String day = dayFormat.format(date);
                                                                                                            final String title = strDate+day;
                                                                                                            Log.d("MAD", "Title 0 passed in is: " + title);

                                                                                                            //progress from today
                                                                                                            DocumentReference docRef = db.collection("User").document("user01").collection("WeeklyWorkout").document(recentWeekDate)
                                                                                                                    .collection("DailyWorkout").document(title);
                                                                                                            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                                                                @Override
                                                                                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                                                                                                    if (task.isSuccessful()) {
                                                                                                                        DocumentSnapshot document = task.getResult();
                                                                                                                        if (document.exists()) {

                                                                                                                            double progress = document.getDouble(PROGRESS_KEY);
                                                                                                                            Log.d("MAD", "0 x value is: " + 6 );
                                                                                                                            userSeries.appendData( new DataPoint( 6, progress ), true, 7, false);

                                                                                                                        } else {
                                                                                                                            Log.d("MAD","Can't find today's progress");
                                                                                                                            Log.d("MAD", "0.2 x value is: " + 6 );
                                                                                                                            userSeries.appendData( new DataPoint( 6, 0 ), true, 7, false);
                                                                                                                        }
                                                                                                                        graph.addSeries(userSeries);

                                                                                                                    } else {
                                                                                                                        Log.d("MAD", "get failed with ", task.getException());
                                                                                                                    }
                                                                                                                }
                                                                                                            });
                                                                                                        } else {
                                                                                                            Log.d("MAD", "get failed with ", task.getException());
                                                                                                        }
                                                                                                    }
                                                                                                });
                                                                                            } else {
                                                                                                Log.d("MAD", "get failed with ", task.getException());
                                                                                            }
                                                                                        }
                                                                                    });
                                                                                } else {
                                                                                    Log.d("MAD", "get failed with ", task.getException());
                                                                                }
                                                                            }
                                                                        });
                                                                    } else {
                                                                        Log.d("MAD", "get failed with ", task.getException());
                                                                    }
                                                                }
                                                            });
                                                        } else {
                                                            Log.d("MAD", "get failed with ", task.getException());
                                                        }
                                                    }
                                                });
                                            } else {
                                                Log.d("MAD", "get failed with ", task.getException());
                                            }
                                        }
                                    });
                                }
                            }
                        })
                        //On failure, we catch the error
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("Recent Week: ", e.toString());
                            }
                        });

    } //out of first documentSnapshot
});
    }
}
