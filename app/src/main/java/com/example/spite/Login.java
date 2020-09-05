package com.example.spite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Login extends AppCompatActivity {

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

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(login() == true) {
                    Intent intent = new Intent(Login.this, MainActivity.class);
                    Login.this.startActivity(intent);
                }
                else {
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

    private boolean login()
    {
        password = passwordET.getText().toString();
        email = userET.getText().toString();
        String msg = "email = " + email + " Password = " + password;
        Log.d( "MAD", msg);

        return true;
    }
}