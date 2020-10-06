package com.example.spite.alarmreceivers;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.spite.dbhandlers.DBWorkoutHandler;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class DailyReceiver extends BroadcastReceiver {

    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private String uid = user.getUid();
    private DBWorkoutHandler dbWorkoutHandler = new DBWorkoutHandler();


    @Override
    public void onReceive(Context context, Intent intent) {
        dbWorkoutHandler.createDailyWorkout(uid);
    }
}
