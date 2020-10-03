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

import com.example.spite.dbhandlers.DBWorkoutHandler;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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
    private boolean timerRunning;

    //For saving information to the database
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private String uid = user.getUid();
    private DBWorkoutHandler dbWorkoutHandler = new DBWorkoutHandler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_workout);

        userPB = (ProgressBar) findViewById(R.id.UserProgressCurrentWorkout);
        userPB.setProgress(PROGRESS_START);

        //Receive Intent from Set Workout Time Pop-Up and instantiate countdown values
        Intent workoutTime = getIntent();
        hour = workoutTime.getIntExtra("hour",0);
        minute = workoutTime.getIntExtra("minute", 0);
        //Converts the users input values to the milliseconds
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

        //pauseBtn.setEnabled(false);
        //start the stopwatch and the countdown timer when it the user starts the workout
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
                //countDownTimer.cancel();
            }

        });

        endBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Sends set hour and minutes user inputted into EndWorkout to display at the end*/
                Intent sendWorkoutValues = new Intent(CurrentWorkout.this, EndWorkout.class);
                sendWorkoutValues.putExtra("hour", hour);
                sendWorkoutValues.putExtra("minute", minute);

                /*Send final logged time of workout to EndWorkout*/
                String loggedTimeString = String.format("%02d", hrs) + ":" + String.format("%02d", min) + ":" + String.format("%02d", sec) + ":" + String.format("%02d", mSec);
                Log.d("final logged time", String.valueOf(loggedTimeString));
                sendWorkoutValues.putExtra("loggedTimeString", loggedTimeString);

                /*Send intent*/
                startActivity(sendWorkoutValues);

                /*Save information of workout as a WorkoutLog doc to the database*/
                long totalWorkoutMillisec = ((long) hrs * 3600000) + ((long) min * 60000) + ((long) sec * 1000) +((long) mSec);
                dbWorkoutHandler.createWorkoutLog(uid, totalWorkoutMillisec);
            }
        });
    }
    //Start stopwatch
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
    //Stopwatch set up
    public Runnable run = new Runnable() {
        @Override
        public void run() {
            millisec = SystemClock.uptimeMillis() - start;
            update = buff + millisec;

            int seconds = (int) (update/1000);
            min = seconds/60;
            long testSec = update/1000;
            sec = (int)testSec % 60;

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

    //Start for the countdown timer
    public void startTimer()
    {
        countDownTimer = new CountDownTimer(counter, 1000) {
            int startTime = (int) (counter/1000);
            @Override
            public void onTick(long l) {
                counter = l;
                int count = (int) counter/1000;
                int timeUsed = (int) (((startTime - count)/ (double) startTime)*100);
                String checkk = timeUsed + " " + count;
                Log.d("TAGG", checkk);
                userPB.setProgress(timeUsed);
                updateTimer();
            }

            @Override
            public void onFinish() {
                timerRunning = false;
            }
        }.start();
    }
    // Stops time in countdown timer
    public void stopTimer()
    {
        countDownTimer.cancel();
        timerRunning = false;

    }
    // Updates time in countdown timer
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
        if(secToUpdate == 1)
        {
            timeText =  String.format(Locale.getDefault(),
                    "%02d:%02d", minToUpdate, secToUpdate2);
        }
        countDownText.setText(timeText);
    }
}