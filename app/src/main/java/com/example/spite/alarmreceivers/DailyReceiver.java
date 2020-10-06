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
        boolean alarmUp = (PendingIntent.getBroadcast(context, 0,
                new Intent("com.my.package.MY_UNIQUE_ACTION"),
                PendingIntent.FLAG_NO_CREATE) != null);
        if (alarmUp)
        {
            Log.d("myTag", "Alarm is already active");
        }
        else{
            dbWorkoutHandler.createDailyWorkout(uid);
        }
    }
}
