package com.example.spite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Progress extends AppCompatActivity {

    Button progToMainBtn = null;
    Button progToKyleBtn = null;
    TextView wklyProgTV = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);

        progToMainBtn = (Button) findViewById(R.id.progToMainBtn);
        progToKyleBtn = (Button) findViewById(R.id.progToKProgBtn);
        wklyProgTV = (TextView) findViewById(R.id.userWkPr);

        progToMainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Progress.this, MainActivity.class);
                Progress.this.startActivity(intent);
            }
        });

        progToKyleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Progress.this, KyleProgress.class);
                Progress.this.startActivity(intent);
            }
        });
    }
}