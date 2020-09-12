package com.example.spite;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    private FirebaseAuth auth;
    EditText userET = null;
    EditText passwordET = null;
    Button loginBtn = null;
    Button logToRegBtn = null;
    private String password = null;
    private String email = null;

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
        String msg = "email = " + email + " Password = " + password;
        Log.d("MAD", msg);

        return true;
    }


}