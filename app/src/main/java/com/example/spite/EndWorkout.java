package com.example.spite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class EndWorkout extends AppCompatActivity {

    private Button toMainBtn;
    private Button toProgressBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_workout);

        toMainBtn = findViewById(R.id.endToMainBtn);
        toProgressBtn = findViewById(R.id.endToProgressBtn);

        toMainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EndWorkout.this, MainActivity.class);
                EndWorkout.this.startActivity(intent);
            }
        });

        toProgressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EndWorkout.this, MainActivity.class);
                EndWorkout.this.startActivity(intent);
            }
        });

    }
}