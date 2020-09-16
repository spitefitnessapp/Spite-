package com.example.spite;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Login extends AppCompatActivity {

    private FirebaseAuth auth;
    EditText userET;
    EditText passwordET;
    Button loginBtn;
    Button logToRegBtn;
    private String password;
    private String email;

    private boolean login;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userET = (EditText) findViewById(R.id.enterUsernameTextView);
        passwordET = (EditText) findViewById(R.id.loginTextPassword);
        loginBtn = (Button) findViewById(R.id.loginButton);
        logToRegBtn = (Button) findViewById(R.id.logToRegBtn);
        auth = FirebaseAuth.getInstance();



        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (login()) {
                    Intent intent = new Intent(Login.this, MainActivity.class);
                    Login.this.startActivity(intent);
                } else {
                    Log.d("MAD", "unsuccessful log in");
                }
            }
        });

        logToRegBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, Register.class);
                Login.this.startActivity(intent);
            }
        });
    }

    private boolean login() {
        password = passwordET.getText().toString();
        email = userET.getText().toString();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference mDocRef = db.collection("User").document(email);

        mDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()) {
                    String pw = documentSnapshot.getString("password");

                    //how to set boolean login to true/false depending on whether passwords match???
                    if(!pw.equals(password)) {
                        Log.d("MAD", "email found, passwords dont match");
                        login = false;
                    }
                    else{Log.d("MAD", "email found, Passwords match");
                    login = true;}
                }
                else { Log.d("MAD", "Email not found?");
                    login = false; }
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Login.this, "Didnt access document!", Toast.LENGTH_SHORT).show();
                        Log.d("MAD", e.toString());

                    }
                });

        return login;
    }

}