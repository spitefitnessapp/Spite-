package com.example.spite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ChangePassword extends AppCompatActivity {

    Button chngPWtoMainBtn = null;
    Button chngPWBtn = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        chngPWtoMainBtn = (Button) findViewById(R.id.chngPWtoMainBtn);
        chngPWBtn = (Button) findViewById(R.id.changePassword);

        chngPWBtn.setEnabled(false);

        chngPWtoMainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChangePassword.this, MainActivity.class);
                ChangePassword.this.startActivity(intent);
            }
        });
    }
}