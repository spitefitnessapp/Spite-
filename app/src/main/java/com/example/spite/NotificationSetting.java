package com.example.spite;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.NotificationCompat;
import android.app.NotificationManager;
import android.app.RemoteAction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.google.android.gms.tasks.OnFailureListener;

import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Toast;
import com.example.spite.dbhandlers.UserDBHandler;
import com.google.android.gms.tasks.OnCompleteListener;
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

    private SwitchCompat notibtn;
    private SwitchCompat getReminder;
    private ImageButton toSettingsBtn;

    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseUser user = auth.getCurrentUser();
    private String USER_UID = user.getUid();
    private boolean NotificationOn;
    private boolean ReminderOn;
    private static final String NOTIFICATION = "Notification Enable";
    private static final String REMINDER = "Reminder Enable";
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
        toSettingsBtn = findViewById(R.id.toSettingsBtn);

        //Receive data from Firestore
        DocumentReference DocRef = db.collection("User").document(USER_UID);
        DocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                String Noti = documentSnapshot.getString(NOTIFICATION);
                String Rem = documentSnapshot.getString(REMINDER);
                NotificationOn = Boolean.parseBoolean(Noti);
                ReminderOn = Boolean.parseBoolean(Rem);
                setButton();
                Log.d("CloudMsg", "Subscribed to daily reminder " + ReminderOn);

                getReminder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                        if(isChecked) {
                            FirebaseMessaging.getInstance().subscribeToTopic("Daily_Reminder");
                            ReminderOn = true;
                            Toast.makeText(NotificationSetting.this, "Daily reminder activated", Toast.LENGTH_SHORT).show();
                        } else {
                            FirebaseMessaging.getInstance().unsubscribeFromTopic("Daily_Reminder");
                            ReminderOn = false;
                            Toast.makeText(NotificationSetting.this, "Daily reminder Deactivated", Toast.LENGTH_SHORT).show();
                        }
                        updateRemToCloud(ReminderOn);
                        setButton();

                    }
                });
                notibtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked){
                        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                            @Override
                            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                if (isChecked) {
                                    if(!task.isSuccessful()){
                                        Log.d("MAD", "Cannot receive device Token", task.getException());
                                    }
                                    String token = task.getResult().getToken();
                                    Log.d("MAD", "Device Token"+token);
                                    FirebaseMessaging.getInstance().subscribeToTopic("Notification");
                                    NotificationOn = true;
                                    Toast.makeText(NotificationSetting.this, "Notification Activated", Toast.LENGTH_SHORT).show();
                                } else {
                                    NotificationOn = false;
                                    Log.d("CloudMsg", "Subscribed to Notification " + Notification);
                                    FirebaseMessaging.getInstance().unsubscribeFromTopic("Notification");
                                    Toast.makeText(NotificationSetting.this, "Notification disabled.", Toast.LENGTH_SHORT).show();
                                }
                                updateNotiToCloud(NotificationOn);
                                Log.d("CloudMsg", "Notification turned on " + NotificationOn);
                                setButton();
                            }

                        });
                    }
                });


            }
        });

        toSettingsBtn.setOnClickListener(new View.OnClickListener() {
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
    private void setButton() {
        if (NotificationOn){
            notibtn.setChecked(true);
        }
        else {
            notibtn.setChecked(false);
        }
        if (ReminderOn){
            getReminder.setChecked(true);
        }
        else {
            getReminder.setChecked(false);
        }

    }
    //update data to firestore
    private void updateNotiToCloud(boolean notification)
    {
        Notification = Boolean.toString(notification);
        Log.d("Update Firestore", "Subscribed to Notification " + Notification);
        UserDBHandler firestoreHandler = new UserDBHandler();
        firestoreHandler.changeNotiSetting(db, USER_UID, Notification);
    }

    private  void updateRemToCloud(boolean reminder)
    {
        Reminder = Boolean.toString(reminder);
        Log.d("Update Firestore", "Subscribed to daily reminder " + Reminder);
        UserDBHandler firestoreHandler = new UserDBHandler();
        firestoreHandler.changeDailyRem(db, USER_UID, Reminder);
    }
}