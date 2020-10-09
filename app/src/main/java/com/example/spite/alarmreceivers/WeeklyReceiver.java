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
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;


public class WeeklyReceiver extends BroadcastReceiver {


    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private UserDBHandler dbh = new UserDBHandler();


    @Override
    public void onReceive(Context context, Intent intent) {
        resetKyle();
    }

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
}
