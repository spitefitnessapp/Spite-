package com.example.spite;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Settings extends AppCompatActivity {

    private Button chngEmail;
    private Button chngWorkoutGoal;
    private Button chngKyle;
    private Button reminderSettings;
    private Button logout;
    private ImageButton toMainBtn;

    private boolean logInCheck;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        chngEmail = findViewById(R.id.chngEmailBtn);
        chngWorkoutGoal = (Button) findViewById(R.id.chngWorkoutGoal);
        chngKyle = (Button) findViewById(R.id.kyleSettingsBtn);
        logout = (Button) findViewById(R.id.logoutBtn);
        toMainBtn = findViewById(R.id.toMainBtn);
        reminderSettings = findViewById(R.id.reminderSettings);
        logout.setEnabled(true);
        Intent intent = getIntent();
        //logInCheck = intent.getBooleanExtra("Logged_in",true);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AuthUI.getInstance()
                        .signOut(Settings.this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                logout.setEnabled(false);
                                //logInCheck = false;
                                Intent sendToLogIn = new Intent(Settings.this, Login.class);
                                startActivity(sendToLogIn);
                                sendToLogIn.addFlags(sendToLogIn.FLAG_ACTIVITY_CLEAR_TOP);
                                sendToLogIn.addFlags(sendToLogIn.FLAG_ACTIVITY_CLEAR_TASK);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Settings.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        chngEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Settings.this, ChangeEmail.class);
                Settings.this.startActivity(intent);
            }
        });

        chngWorkoutGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Settings.this, ChangeWorkoutGoal.class);
                Settings.this.startActivity(intent);
            }
        });

        chngKyle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Settings.this, KyleSettings.class);
                Settings.this.startActivity(intent);
            }
        });

        toMainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Settings.this, MainActivity.class);
                Settings.this.startActivity(intent);
            }
        });

        reminderSettings.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(Settings.this, NotificationSetting.class);
                Settings.this.startActivity(intent);
            }
        });


    }
}