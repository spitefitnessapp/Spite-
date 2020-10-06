package com.example.spite.dbhandlers;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.spite.models.DailyWorkout;
import com.example.spite.models.User;
import com.example.spite.models.WeeklyWorkout;
import com.example.spite.models.WorkoutLog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class DBWorkoutHandler {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public static final String USERNAME_KEY = "userID";
    public static final String DATE_KEY = "date";
    public static final String DAY_KEY = "day";
    public static final String DAILY_TIME_LOGGED_KEY = "dailyTimeLogged";
    public static final String TIME_LOGGED = "timeLogged";

    private WeeklyWorkout weeklyWorkout;
    private DocumentReference weeklyWorkoutRef;
    private Map<String, Object> saveWeeklyWorkout;

    private DailyWorkout dailyWorkout;
    private DocumentReference dailyWorkoutRef;
    Map<String, Object> saveDailyWorkout;

    private WorkoutLog workoutLog;
    private DocumentReference workoutLogRef;
    private Map<String, Object> saveWorkoutLog;

    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private String uid = user.getUid();
    private UserDBHandler dbh = new UserDBHandler();

    private void resetKyle() {
        CollectionReference userCR = db.collection("User");
        userCR.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<String> UserList = new ArrayList<>();
                    for (DocumentSnapshot document : task.getResult()) {
                        User use = document.toObject(User.class);
                        String u = document.getId();
                        UserList.add(u);
                    }

                    int userListSize = UserList.size() - 1;
                    boolean done = false;
                    while (!done) {

                        int ran = new Random().nextInt(userListSize);
                        String randomUser = UserList.get(ran);

                        if (randomUser.equals(user.getUid())) {
                            Log.d("MAD", "Kyle cannot be current user");
                        } else {
                            dbh.changeKyle(db, user.getUid(), randomUser);
                            String ids = user.getUid() + " is now paired with Kyle: " + randomUser;
                            Log.d("MAD", ids);
                            done = true;
                        }
                    }

                } else {
                    Log.d("MAD", "Error getting documents: ", task.getException());
                }
            }
        });
    }

    /*Create document within the WeeklyWorkout collection for the current week*/
    public void createWeeklyWorkout(final String userID){
        Task<QuerySnapshot> mostRecentWeek = db.collection("User").document(userID)
                .collection("WeeklyWorkout")
                .orderBy("date", Query.Direction.DESCENDING)
                .limit(1)
                .get()

                /*On success, we store the date of the most recent week and proceed to create a DailyWorkout document within*/
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        String recentWeekDate = "";

                        /*Go through the queries to retrieve the Date string of the most recent week*/
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            WeeklyWorkout recentWeek = documentSnapshot.toObject(WeeklyWorkout.class);

                            String date = recentWeek.getDateString();

                            recentWeekDate += date;
                        }

                        /*Create instance of WeeklyWorkout model*/
                        weeklyWorkout = new WeeklyWorkout(userID);

                        if(!weeklyWorkout.getDateString().equals(recentWeekDate)){
                            /*Create field variables in the document*/
                            saveWeeklyWorkout = new HashMap<>();
                            saveWeeklyWorkout.put(DATE_KEY, weeklyWorkout.getDate());
                            saveWeeklyWorkout.put(USERNAME_KEY, weeklyWorkout.getUserID());

                            /*Add the document within WeeklyWorkout collection*/
                            weeklyWorkoutRef = db
                                    .collection("User").document(userID)
                                    .collection("WeeklyWorkout").document(weeklyWorkout.getDateString());

                            /*Testing to see whether method has succeeded*/
                            weeklyWorkoutRef.set(saveWeeklyWorkout)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d("Weekly Workout Ref:", "Successfully added weekly workout");
                                            resetKyle();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.d("Weekly Workout Ref:", e.toString());
                                        }
                                    });
                            /*Create instance of DailyWorkout model*/
                            dailyWorkout = new DailyWorkout(userID);

                            /*Create field variables in the document*/
                            saveDailyWorkout = new HashMap<>();
                            saveDailyWorkout.put(USERNAME_KEY, dailyWorkout.getUserID());
                            saveDailyWorkout.put(DATE_KEY, dailyWorkout.getDate());
                            saveDailyWorkout.put(DAY_KEY, dailyWorkout.getDay());
                            saveDailyWorkout.put(DAILY_TIME_LOGGED_KEY, dailyWorkout.getDailyTimeLogged());

                            /*Add the document within DailyWorkout collection*/
                            dailyWorkoutRef = db
                                    .collection("User").document(userID)
                                    .collection("WeeklyWorkout").document(weeklyWorkout.getDateString())
                                    .collection("DailyWorkout").document(dailyWorkout.getDailyWorkoutId());

                            /*Testing to see whether method has succeeded*/
                            dailyWorkoutRef.set(saveDailyWorkout)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d("Daily Workout Ref:", "Successfully added daily workout");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.d("Daily Workout Ref:", e.toString());
                                        }
                                    });
                        }
                    }
                })
                /*On failure, we catch the error*/
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Recent Week: ", e.toString());
                    }
                });
    }

    /*Creating a document called DailyWorkout which stores all date information, total time logged
     that day, as well as WorkoutLog docs*/
    public void createDailyWorkout(final String userID){

        /*Query the database to find the most recently created WeeklyWorkout doc for the specified user*/
        Task<QuerySnapshot> mostRecentWeek = db.collection("User").document(userID)
                .collection("WeeklyWorkout")
                .orderBy("date", Query.Direction.DESCENDING)
                .limit(1)
                .get()

                /*On success, we store the date of the most recent week and proceed to create a DailyWorkout document within*/
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        String recentWeekDate = "";

                        /*Go through the queries to retrieve the Date string of the most recent week*/
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            WeeklyWorkout recentWeek = documentSnapshot.toObject(WeeklyWorkout.class);

                            String date = recentWeek.getDateString();

                            recentWeekDate += date;
                        }

                            /*Create the DailyWorkout within the WeeklyWorkout doc with recentWeekDate string*/
                        /*Create instance of DailyWorkout model*/
                        dailyWorkout = new DailyWorkout(userID);
                        final String finalRecentWeekDate = recentWeekDate;

                        Task<QuerySnapshot> mostRecentDay = db.collection("User").document(userID)
                                .collection("WeeklyWorkout").document(recentWeekDate)
                                .collection("DailyWorkout")
                                .orderBy("date", Query.Direction.DESCENDING)
                                .limit(1)
                                .get()

                                /*On success, we store the date of the most recent week and proceed to create a DailyWorkout document within*/
                                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                        String dailyWorkoutID = "";
                                        long dailyTimeLogged = 0;

                                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                            DailyWorkout recentDayObject = documentSnapshot.toObject(DailyWorkout.class);

                                            String dailyWorkoutDocID = recentDayObject.getDateString() + recentDayObject.getDay();
                                            dailyWorkoutID += dailyWorkoutDocID;

                                            double dayTimeLogged = recentDayObject.getDailyTimeLogged();
                                        }

                                        if (!dailyWorkout.getDailyWorkoutId().equals(dailyWorkoutID)) {
                                            /*Create field variables in the document*/
                                            saveDailyWorkout = new HashMap<>();
                                            saveDailyWorkout.put(USERNAME_KEY, dailyWorkout.getUserID());
                                            saveDailyWorkout.put(DATE_KEY, dailyWorkout.getDate());
                                            saveDailyWorkout.put(DAY_KEY, dailyWorkout.getDay());
                                            saveDailyWorkout.put(DAILY_TIME_LOGGED_KEY, dailyWorkout.getDailyTimeLogged());

                                            /*Add the document within DailyWorkout collection*/
                                            dailyWorkoutRef = db
                                                    .collection("User").document(userID)
                                                    .collection("WeeklyWorkout").document(finalRecentWeekDate)
                                                    .collection("DailyWorkout").document(dailyWorkout.getDailyWorkoutId());

                                            /*Testing to see whether method has succeeded*/
                                            dailyWorkoutRef.set(saveDailyWorkout)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Log.d("Daily Workout Ref:", "Successfully added daily workout");
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Log.d("Daily Workout Ref:", e.toString());
                                                        }
                                                    });
                                        }
                                    }
                                    })
                                            /*On failure, we catch the error*/
                                            .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.d("Recent Week: ", e.toString());
                                        }
                                    });
                    }
                })

                /*On failure, we catch the error*/
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Recent Week: ", e.toString());
                    }
                });
    }

    //Inaccurate with WorkoutLogs under a minute. Needs find tuning.
    public void updateDailyTimeLogged(String userID, String recentWeekDate, String dailyWorkoutID, double oldTimeLogged, long newTimeLogged){
        double updatedTimeLogged = (oldTimeLogged * 60000) + newTimeLogged;

        updatedTimeLogged = updatedTimeLogged / 60000;
        updatedTimeLogged = Math.round(updatedTimeLogged);

        db.collection("User").document(userID)
                .collection("WeeklyWorkout").document(recentWeekDate)
                .collection("DailyWorkout").document(dailyWorkoutID)
                .update(DAILY_TIME_LOGGED_KEY, updatedTimeLogged);
    }

    /*Creating a document called WorkoutLog that stores time spent doing a workout*/
    public void createWorkoutLog(final String userID, final long timeLogged){

        /*Query the database to find the most recently created WeeklyWorkout doc for the specified user*/
        Task<QuerySnapshot> mostRecentWeek = db.collection("User").document(userID)
                .collection("WeeklyWorkout")
                .orderBy("date", Query.Direction.DESCENDING)
                .limit(1)
                .get()

                /*On success, we store the date of the most recent week and proceed to find the most recent DailyWorkout doc*/
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        String recentWeekDate = "";

                        /*Go through the queries to retrieve the Date string of the most recent week*/
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            WeeklyWorkout recentWeek = documentSnapshot.toObject(WeeklyWorkout.class);

                            String date = recentWeek.getDateString();

                            recentWeekDate += date;
                        }

                        /*Date of the most recent week*/
                        final String finalRecentWeekDate = recentWeekDate;

                        Task<QuerySnapshot> mostRecentDay = db.collection("User").document(userID)
                                .collection("WeeklyWorkout").document(recentWeekDate)
                                .collection("DailyWorkout")
                                .orderBy("date", Query.Direction.DESCENDING)
                                .limit(1)
                                .get()

                                /*On success, we store the date of the most recent week and proceed to create a DailyWorkout document within*/
                                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                        String dailyWorkoutID = "";
                                        long dailyTimeLogged = 0;

                                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                            DailyWorkout recentDayObject = documentSnapshot.toObject(DailyWorkout.class);

                                            String dailyWorkoutDocID = recentDayObject.getDateString() + recentDayObject.getDay();
                                            dailyWorkoutID += dailyWorkoutDocID;
                                            Log.d("Recent Week: ", dailyWorkoutID);

                                            double dayTimeLogged = recentDayObject.getDailyTimeLogged();
                                            dailyTimeLogged += dayTimeLogged;
                                        }

                                        /*Create the WorkoutLog within the DailyWorkout doc with recentDay string*/
                                        /*Create instance of WorkoutLog model*/
                                        workoutLog = new WorkoutLog(userID, timeLogged);

                                        /*Create field variables in the document*/
                                        saveWorkoutLog = new HashMap<>();
                                        saveWorkoutLog.put(USERNAME_KEY, workoutLog.getUserID());
                                        saveWorkoutLog.put(DATE_KEY, workoutLog.getDate());
                                        saveWorkoutLog.put(TIME_LOGGED, workoutLog.getTimeLogged());

                                        /*Add the document within WorkoutLog collection*/
                                        workoutLogRef = db
                                                .collection("User").document(userID)
                                                .collection("WeeklyWorkout").document(finalRecentWeekDate)
                                                .collection("DailyWorkout").document(dailyWorkoutID)
                                                .collection("WorkoutLog").document(workoutLog.getTime());

                                        /*Testing to see whether method has succeeded*/
                                        final String finalDailyWorkoutID = dailyWorkoutID;
                                        final double finalDailyTimeLogged = dailyTimeLogged;
                                        workoutLogRef.set(saveWorkoutLog)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Log.d("Workout Log Ref:", "Successfully added workout log");
                                                        /*Updating DailyWorkout's dailyTimeLogged after the successful creation of a WorkoutLog*/
                                                        updateDailyTimeLogged(userID, finalRecentWeekDate, finalDailyWorkoutID, finalDailyTimeLogged, timeLogged);
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.d("Workout Log Ref:", e.toString());
                                                    }
                                                });
                                    }
                                })

                                /*On failure, we catch the error*/
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d("Recent Week: ", e.toString());
                                    }
                                });
                    }
                })

                /*On failure, we catch the error*/
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Recent Week: ", e.toString());
                    }
                });
    }
}
