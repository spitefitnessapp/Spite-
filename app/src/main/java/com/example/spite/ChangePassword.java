package com.example.spite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ChangePassword extends AppCompatActivity {

    //NOT CURRENTLY IN USE


    Button chngPWtoMainBtn = null;
    Button chngPWBtn = null;
    EditText oldPWET = null;
    EditText newPW01 = null;
    EditText newPW02 = null;
    private String password = "123"; //call this from somewhere?????????


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        chngPWtoMainBtn = (Button) findViewById(R.id.chngPWtoMainBtn);
        chngPWBtn = (Button) findViewById(R.id.changePassword);
        oldPWET = (EditText) findViewById(R.id.currentPassword);
        newPW01 = (EditText) findViewById(R.id.newPassword);
        newPW02 = (EditText) findViewById(R.id.confirmNewPassword);

        chngPWBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View view )
            {
                changePassword();
            }
        });

        chngPWtoMainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChangePassword.this, MainActivity.class);
                ChangePassword.this.startActivity(intent);
            }
        });
    }

    private void changePassword()
    {
        String oldPW = oldPWET.getText().toString();
        String pw1 = newPW01.getText().toString();
        String pw2 = newPW02.getText().toString();
        String msg;

        if ( oldPW.equals(password) ) {
            if (pw1.equals(pw2)) {
               password = pw1;
                msg = "New password: " + pw1 + " " + pw2;
                Log.d("MAD", msg);
            } else {
                msg = "Password not changed";
                Log.d("MAD", newPW02.getText().toString());
            }
        }
        else
        {Log.d("MAD", "Old Password incorrect");}
    }
}