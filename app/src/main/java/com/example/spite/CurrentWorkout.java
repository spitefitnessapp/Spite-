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
import android.widget.ProgressBar;
import android.widget.TextView;
import java.util.Locale;

public class CurrentWorkout extends AppCompatActivity {

    private Button endBtn;
    private Button resumeBtn;
    private Chronometer stopwatch;
    private TextView countDownText;
    private ProgressBar userPB;

    //For the stopwatch
    private boolean stRunning;
    private Handler handler;
    private long millisec, start, buff, update = 0;
    private int sec, min, mSec, hrs;

    //For the countdown timer
    private CountDownTimer countDownTimer;
    private int hour;
    private int minute;
    private int PROGRESS_START = 0;

    //Convert the values to millisecond
    private long counter;
    //private int sec, min, mSec;
    private boolean timerRunning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_workout);

        userPB = (ProgressBar) findViewById(R.id.UserProgressMainScreen);
        userPB.setProgress(PROGRESS_START);

        //Receive Intent from Set Workout Time Pop-Up and instantiate countdown values
        Intent workoutTime = getIntent();
        hour = workoutTime.getIntExtra("hour",0);
        minute = workoutTime.getIntExtra("minute", 0);
        counter =  ((long)(hour))*3600000 + ((long)(minute))*60000;

        /* TODO: Remove this later. Testing to see if intent is received*/
        Log.d("picker hour", String.valueOf(hour));
        Log.d("picker min", String.valueOf(minute));
        Log.d("picker counter", String.valueOf(counter));

        endBtn = findViewById(R.id.endWorkout);
        resumeBtn = findViewById(R.id.resume);
        stopwatch = findViewById(R.id.stopwatch);
        countDownText = findViewById(R.id.countdown);
        handler = new Handler();

        startStopW();
        updateTimer();

        resumeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                stopTimer();

                if(!stRunning)
                {
                    start = SystemClock.uptimeMillis();
                    handler.postDelayed(run,0);
                    stopwatch.start();
                    stRunning = true;
                    resumeBtn.setText(R.string.Pause);
                    startTimer();

                }
                else
                {
                    buff += millisec;
                    handler.removeCallbacks(run);
                    stopwatch.stop();
                    stRunning = false;
                    resumeBtn.setText(R.string.Resume);
                    stopTimer();
                }
            }

        });

        endBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Sends set hour and minutes user inputted into EndWorkout to display at the end*/
                Intent sendWorkoutGoal = new Intent(CurrentWorkout.this, EndWorkout.class);
                sendWorkoutGoal.putExtra("hour", hour);
                sendWorkoutGoal.putExtra("minute", minute);

                /*Send final logged time of workout to EndWorkout*/
                String finalLoggedTime = String.format("%02d", min) + ":" + String.format("%02d", sec) + ":" + String.format("%02d", mSec);
                Log.d("final logged time", String.valueOf(finalLoggedTime));
                sendWorkoutGoal.putExtra("loggedTime", finalLoggedTime);
                startActivity(sendWorkoutGoal);
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

        startTimer();
    }

    public Runnable run = new Runnable() {
        @Override
        public void run() {
            millisec = SystemClock.uptimeMillis() - start;
            update = buff + millisec;

            int seconds = (int) (update/1000);
            min = seconds/60;
            sec = seconds % 60;
            mSec = (int)(update % 100);

            if(min>60)
            {
                hrs =+1;
                min = min % 60;

            }

            if(hrs<1) {
                stopwatch.setText(String.format("%02d", min) + ":" + String.format("%02d", sec) + ":" + String.format("%02d", mSec));
            }
            else
            {
                stopwatch.setText(String.format("%02d", hrs) + ":" + String.format("%02d", min) + ":" + String.format("%02d", sec) + ":" + String.format("%02d", mSec));

            }
            handler.postDelayed(this, 60);
        }
    };

    public void startTimer()
    {
        countDownTimer = new CountDownTimer(counter, 1000) {
            int startTime = (int) (counter/1000);
            @Override
            public void onTick(long l) {
                counter = l;
                int count = (int) counter/1000;
                int timeUsed = (int) (((startTime - count)/ (double) startTime)*100);
                userPB.setProgress(timeUsed);
                updateTimer();
            }

            @Override
            public void onFinish() {
                timerRunning = false;
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
        // added hours to the stopwatch
        int hoursToUpdate = (int) (counter/1000)/3600;
        int minToUpdate = (int) (counter/1000)%3600/60;
        int secToUpdate = (int) (counter/1000)%60;
        int secToUpdate2 = (int) ((counter-1000)/1000)%60;


        String timeText;
        if(hoursToUpdate > 0){
            timeText =  String.format(Locale.getDefault(),
                    "%d:%02d:%02d", hoursToUpdate, minToUpdate, secToUpdate);
        }
        else
        {
            timeText =  String.format(Locale.getDefault(),
                    "%02d:%02d", minToUpdate, secToUpdate);
        }

        //Update text for  countdown timer once it finises the user input time
       /* if(secToUpdate == 1)
        {
            timeText =  String.format(Locale.getDefault(),
                    "%02d:%02d", minToUpdate, secToUpdate2);
        }*/
        countDownText.setText(timeText);
    }
}