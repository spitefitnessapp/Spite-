package com.example.spite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CurrentWorkout extends AppCompatActivity {

    Button pauseBtn = null;
    Button endBtn = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_workout);

        pauseBtn = (Button) findViewById(R.id.pauseWorkout);
        endBtn = (Button) findViewById(R.id.endWorkout);

        pauseBtn.setEnabled(false);

        endBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CurrentWorkout.this, EndWorkout.class);
                CurrentWorkout.this.startActivity(intent);
            }
        });
    }
}