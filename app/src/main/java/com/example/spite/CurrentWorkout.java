package com.example.spite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

public class CurrentWorkout extends AppCompatActivity {

    private Button pauseBtn;
    private Button endBtn;
    private Button resumeBtn;
    private Chronometer stopwatch;
    private TextView countDownText;

    //For the stopwatch
    private boolean stRunning;
    private Handler handler;
    private long millisec, start, buff, update = 0;
    private int sec, min, mSec;

    // Fetching the data from main activity

    int hour; /*= getIntent().getIntExtra("hour", 0);*/
    int minute; /*= getIntent().getIntExtra("minutes", 0);*/

    //For the countdown timer
    private CountDownTimer countDownTimer;

    //Convert the values to millisecond
    private long counter = Long.valueOf(hour)/3600000 + Long.valueOf(minute)/60000 ;
    private boolean timerRunning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_workout);

        //Create an intent to retrieve set workout time
        Intent workoutTime = getIntent();
        hour = workoutTime.getIntExtra("hour", 0);
        minute = workoutTime.getIntExtra("minute", 0);

        pauseBtn = (Button) findViewById(R.id.pause);
        endBtn = (Button) findViewById(R.id.endWorkout);
        resumeBtn = (Button) findViewById(R.id.resume);
        stopwatch = findViewById(R.id.stopwatch);
        countDownText = findViewById(R.id.countdown);
        handler = new Handler();

        //pauseBtn.setEnabled(false);
        startStopW();
        startCounter();
        updateTimer();


        endBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CurrentWorkout.this, EndWorkout.class);
                CurrentWorkout.this.startActivity(intent);
            }
        });
    }

    public void startStopW()
    {
        if(!stRunning)
        {
            start = SystemClock.uptimeMillis();
            handler.postDelayed(run, 0);
            stopwatch.start();
            stRunning = true;

        }
    }

    public Runnable run = new Runnable() {
        @Override
        public void run() {
            millisec = SystemClock.uptimeMillis() - start;
            update = buff + millisec;
            sec = (int) (update/1000);
            min = sec/60;
            sec = sec % 60;
            millisec = (int)(update % 100);
            stopwatch.setText(String.format("%02d", min) + ":" + String.format("%02d", sec) + ":" + String.format("%02d", mSec));
            handler.postDelayed(this, 60);
        }
    };

    public void onPause(View view)
    {
        if(stRunning)
           {
               buff += millisec;
               handler.removeCallbacks(run);
               stopwatch.stop();;
               stRunning = false;

           }
    }

    public void startCounter()
    {
        if(timerRunning)
        {
            stopTimer();
        }
        else
        {
            startTimer();
        }
    }

    public void startTimer()
    {
        countDownTimer = new CountDownTimer(counter, 1000) {
            @Override
            public void onTick(long l) {
                counter = 1;
                updateTimer();
            }

            @Override
            public void onFinish() {

            }
        }.start();
    }

    public void stopTimer()
    {
        countDownTimer.cancel();
        timerRunning = false;

    }

    public void updateTimer()
    {
        int mins = (int) counter/60000;
        int secs = (int) counter % 60000/1000;
        String timeLeftText;
        timeLeftText = " " + mins;
        timeLeftText += ":";
        if(secs<10) {
            timeLeftText += "0";
            timeLeftText += secs;
        }

        countDownText.setText(timeLeftText);
    }

}