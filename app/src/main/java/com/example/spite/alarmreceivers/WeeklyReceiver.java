package com.example.spite.alarmreceivers;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.spite.dbhandlers.DBWorkoutHandler;
import com.example.spite.dbhandlers.UserDBHandler;
import com.example.spite.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class WeeklyReceiver extends BroadcastReceiver {


    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private String uid = user.getUid();
    private UserDBHandler dbh = new UserDBHandler();
    private DBWorkoutHandler dbWorkoutHandler = new DBWorkoutHandler();


    @Override
    public void onReceive(Context context, Intent intent) {
        dbWorkoutHandler.createWeeklyWorkout(uid);
    }
}
