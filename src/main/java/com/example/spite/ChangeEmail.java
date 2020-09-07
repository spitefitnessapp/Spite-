package com.example.spite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ChangeEmail extends AppCompatActivity {

    Button chngEMtoMainBtn = null;
    Button chngEmailBtn = null;
    EditText newEmailET = null;
    EditText confirmNewEmailET = null;
    EditText passwordET = null;
    private String password = "123";
    private String email = "rogue";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_email);

        chngEMtoMainBtn = (Button) findViewById(R.id.chngEMtoMainBtn);
        chngEmailBtn = (Button) findViewById(R.id.changeEmail);
        newEmailET = (EditText) findViewById(R.id.newEmail);
        confirmNewEmailET = (EditText) findViewById(R.id.confirmNewEmail);
        passwordET = (EditText) findViewById(R.id.changeEmailPassword);

        chngEmailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeEmail();
            }
        });

        chngEMtoMainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChangeEmail.this, MainActivity.class);
                ChangeEmail.this.startActivity(intent);
            }
        });
    }

    private void changeEmail()
    {
        if( passwordET.getText().toString().equals(password) ) {
            if (newEmailET.getText().toString().equals(confirmNewEmailET.getText().toString())) {
                email = newEmailET.getText().toString();
                Log.d("MAD", email);
            } else {
                Log.d("MAD", "Email unchanged");
            }
        }
        else
        {Log.d("MAD", "Password doesnt match");}

    }
}