package com.example.spite;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.spite.alarmreceivers.DailyReceiver;
import com.example.spite.alarmreceivers.WeeklyReceiver;
import com.example.spite.dbhandlers.DBWorkoutHandler;
import com.example.spite.fragmentscreens.FragmentHome;
import com.example.spite.fragmentscreens.FragmentKyleProgress;
import com.example.spite.fragmentscreens.FragmentProfile;
import com.example.spite.fragmentscreens.FragmentProgress;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNav;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*Setting up alarms to crete DailyWorkout docs and WeeklyWorkout docs*/
        setWeeklyAlarm();
        setDailyAlarm();

        /* Display the Bottom Navigation Bar*/
        bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        /*Switch statement to display specific fragments outside of the navigation tab*/
        Intent whichView = getIntent();
        String source = whichView.getStringExtra("TabView");

        if (source != null) {
            /*If source != null, the fragment that sent the intent is shown*/
            Log.d("TabView", source);
            changeFragmentView(source);
        }
        else {
            /*Display FragmentHome by default upon opening*/
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,
                    new FragmentHome()).commit();
        }
    }

    /*Set up menu in the Bottom Navigation Bar and change fragments upon clicking*/
    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    /*Set up menu as a switch*/
                    switch (item.getItemId()){
                        case R.id.nav_home:
                            selectedFragment = new FragmentHome();
                            break;
                        case R.id.nav_progress:
                            selectedFragment = new FragmentProgress();
                            break;
                        case R.id.nav_kyle_progress:
                            selectedFragment = new FragmentKyleProgress();
                            break;
                        case R.id.nav_profile:
                            selectedFragment = new FragmentProfile();
                            break;
                    }

                    /*Get selectedFragment and display selectedFragment in the switch*/
                    if(selectedFragment != null) {
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                selectedFragment).commit();
                    }
                    return true;
                }
            };

    /*TODO: Change FragmentView is successful. However, navBar icons do not change*/
    public void changeFragmentView(String source){
        /*Store selectedFragment into an empty Fragment to display later*/
        Fragment selectedFragment = null;

        /*If "EndWorkoutToProgress", the selectedFragment shown is Progress*/
        if (source.equals("EndWorkoutToProgress")) {
            selectedFragment = new FragmentProgress();
            bottomNav.setSelectedItemId(R.id.nav_progress);
            Log.d("TabView: ", "EndWorkoutToProgress");
        }

        if (source.equals("SettingsToProfile")) {
            selectedFragment = new FragmentProfile();
            bottomNav.setSelectedItemId(R.id.nav_profile);
            Log.d("TabView: ", "SettingsToProfile");
        }

        /*Get selectedFragment and display selectedFragment in the switch*/
        if(selectedFragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    selectedFragment).commit();
            Log.d("TabView", "Stage 3");
        }
    }

    /*Create WeeklyWorkout for the user at midnight on Monday*/
    private void setWeeklyAlarm(){
        Log.d("Weekly Alarm: ", "Proceeding");

        /*Set up intents to the WeeklyReceiver.class to get broadcasts*/
        Intent weeklyIntent = new Intent(this, WeeklyReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), 0, weeklyIntent, 0);

        /*Instantiate AlarmManager*/
        android.app.AlarmManager alarmManager = (android.app.AlarmManager) getSystemService(ALARM_SERVICE);

        /*Get the time from one week from now on a Monday 12:00AM to turn into milliseconds*/
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 1);

        /*Set alarmManager to repeat the alarm every 7 days on the Monday*/
        alarmManager.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), android.app.AlarmManager.INTERVAL_DAY * 7, pendingIntent);
        Log.d("Weekly Alarm: ", "Succeeded");
    }

    /*Create DailyWorkout for the user at midnight everyday*/
    private void setDailyAlarm(){
        Log.d("Daily Alarm: ", "Proceeding");

        /*Set up intents to the WeeklyReceiver.class to get broadcasts*/
        Intent dailyIntent = new Intent(this, DailyReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), 1, dailyIntent, 0);

        /*Instantiate AlarmManager*/
        android.app.AlarmManager alarmManager = (android.app.AlarmManager) getSystemService(ALARM_SERVICE);

        /*Get the time from one day from now at 12:01 AM*/
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 1);
        calendar.set(Calendar.SECOND, 0);

        /*Set alarmManager to repeat the alarm every 7 days on the Monday*/
        alarmManager.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), android.app.AlarmManager.INTERVAL_DAY, pendingIntent);
        Log.d("Daily Alarm: ", "Succeeded");
    }
}
