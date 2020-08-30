package com.example.spite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Login extends AppCompatActivity {

    TextView userTV = null;
    TextView passwordTV = null;
    Button loginBtn = null;
    Button logToRegBtn = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userTV = (TextView) findViewById(R.id.enterUsernameTextView);
        passwordTV = (TextView) findViewById(R.id.loginTextPassword);
        loginBtn = (Button) findViewById(R.id.loginButton);
        logToRegBtn = (Button) findViewById(R.id.logToRegBtn);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, MainActivity.class);
                Login.this.startActivity(intent);
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
}