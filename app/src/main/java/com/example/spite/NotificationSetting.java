package com.example.spite;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.NotificationManager;
import android.app.RemoteAction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

public class NotificationSetting extends AppCompatActivity {

    Button notibtn;
    Button getReminder;
    Button goBack;

    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseUser user = auth.getCurrentUser();
    private String USER_UID = user.getUid();
    private boolean NotificationOn;
    private boolean ReminderOn;
    private static final String NOTIFICATION = "Notification Enable";
    private static final String REMINDER = "Daily_Reminder Enable";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String Notification;
    private String Reminder;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_setting);
        runtimeEnableAutoInit();
        notibtn = findViewById(R.id.turnOnNoti);
        getReminder = findViewById(R.id.turnOnreminder);
        goBack = findViewById(R.id.goBack);

        //Receive data from firestore
        DocumentReference DocRef = db.collection("User").document(USER_UID);
        DocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                String Noti = documentSnapshot.getString(NOTIFICATION);
                String Rem = documentSnapshot.getString(REMINDER);
                NotificationOn = Boolean.parseBoolean(Noti);
                ReminderOn = Boolean.parseBoolean(Rem);
                setButtonText();

                notibtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                            @Override
                            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                if (!NotificationOn) {
                                    FirebaseMessaging.getInstance().subscribeToTopic("Notification");
                                    NotificationOn = true;
                                    Toast.makeText(NotificationSetting.this, "Notification Activated", Toast.LENGTH_SHORT).show();
                                    updateCloud(NotificationOn, ReminderOn);

                                } else {
                                    NotificationOn = false;
                                    Log.d("CloudMsg", "Subscribed to Notification " + Notification);
                                    Log.d("CloudMsg", "Subscribed to daily reminder " + Reminder);
                                    FirebaseMessaging.getInstance().unsubscribeFromTopic("Notification");
                                    Toast.makeText(NotificationSetting.this, "Notification disabled and reminder disabled", Toast.LENGTH_SHORT).show();
                                    updateCloud(NotificationOn, ReminderOn);
                                }
                                Log.d("CloudMsg", "Notification turned on " + NotificationOn);
                                setButtonText();
                            }

                        });
                    }
                });

                getReminder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                            if (!ReminderOn) {
                                FirebaseMessaging.getInstance().subscribeToTopic("Daily_Reminder");
                                ReminderOn = true;
                                Toast.makeText(NotificationSetting.this, "Daily reminder activated", Toast.LENGTH_SHORT).show();
                                updateCloud(NotificationOn, ReminderOn);
                            } else {
                                ReminderOn = false;
                                FirebaseMessaging.getInstance().unsubscribeFromTopic("Daily_Reminder");
                                Toast.makeText(NotificationSetting.this, "Daily reminder Deactivated", Toast.LENGTH_SHORT).show();
                                updateCloud(NotificationOn, ReminderOn);
                            }
                        Log.d("CloudMsg", "Subscribed to daily reminder " + ReminderOn);
                        setButtonText();
                    }
                });



                //Update Firestore

            }
        });


        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NotificationSetting.this, Settings.class);
                NotificationSetting.this.startActivity(intent);
            }
        });

    }
//sets auto enable for FirebaseMessaging
    public void runtimeEnableAutoInit() {
        FirebaseMessaging.getInstance().setAutoInitEnabled(true);
    }
//Changes text for buttons on call
    private void setButtonText() {
        if (NotificationOn) {
            notibtn.setText("ON");
        } else {
            notibtn.setText("OFF");

        }
        if (ReminderOn) {
            getReminder.setText("ON");

        } else {
            getReminder.setText("OFF");
        }

    }

//update data to firestore
    private void updateCloud(boolean notification, boolean reminder)
    {
        Notification = Boolean.toString(notification);
        Log.d("CloudMsg", "Subscribed to Notification " + Notification);
        Reminder = Boolean.toString(reminder);
        Log.d("CloudMsg", "Subscribed to daily reminder " + Reminder);
        UserDBHandler firestoreHandler = new UserDBHandler();
        firestoreHandler.changeNotiSetting(db,USER_UID,Notification);
        firestoreHandler.changeDailyRem(db,USER_UID,Reminder);
    }

}