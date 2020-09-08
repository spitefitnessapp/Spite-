package com.example.spite;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;

public class MainActivity extends AppCompatActivity {

    //Home Screen Variables
    private Button MtoSBtn;
    private Button MtoProfBtn;
    private Button startWorkout;

    //Set Workout Dialog Variables
    private NumberPicker setWorkoutHr;
    private NumberPicker setWorkoutMin;
    private Button setWorkoutBtn;
    private Button cancelBtn;
    private String[] hourValues;
    private String[] minValues;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startWorkout = (Button) findViewById(R.id.startWorkout);
        MtoSBtn = (Button) findViewById(R.id.MainToSettingsBtn);
        MtoProfBtn = (Button) findViewById(R.id.mainToProfBtn);

        startWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSetWorkoutDialog();
            }
        });

        MtoProfBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Profile.class);
                MainActivity.this.startActivity(intent);
            }
        });

        MtoSBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Settings.class);
                MainActivity.this.startActivity(intent);
            }
        });

    }


    /*Opens a number picker dialog to set workout time and passes the variable to Start Workout Activity*/
    public void showSetWorkoutDialog()
    {
        /*Enables the Build of a Custom Dialog Layout*/
        AlertDialog.Builder setWorkoutBuilder = new AlertDialog.Builder(MainActivity.this);
        View setWorkoutView = getLayoutInflater().inflate(R.layout.dialog_set_workout, null);

        setWorkoutHr = setWorkoutView.findViewById(R.id.setWorkoutHr);
        setWorkoutMin = setWorkoutView.findViewById(R.id.setWorkoutMin);
        setWorkoutBtn = setWorkoutView.findViewById(R.id.setWorkoutBtn);
        cancelBtn = setWorkoutView.findViewById(R.id.cancelBtn);

        /*Set Max and Min values displayed, and Customise Selector Wheel in Hour*/
        setWorkoutHr.setMinValue(0);
        setWorkoutHr.setMaxValue(12);
                /* Code to display double digits. Will reassess later.*/
                hourValues  = new String[] {"00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
                setWorkoutHr.setDisplayedValues(hourValues);

        setWorkoutHr.setWrapSelectorWheel(true);

        /*Set Max and Min values displayed, and Customise Selector Wheel in Minutes*/
        setWorkoutMin.setMinValue(0);
        setWorkoutMin.setMaxValue(59);
                /* Code to display double digits*/
                minValues  = new String[] {"00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12",
                "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25",
                "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38",
                "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51",
                "52", "53", "54", "55", "56", "57", "58", "59"};
                setWorkoutMin.setDisplayedValues(minValues);

        setWorkoutMin.setWrapSelectorWheel(true);


        setWorkoutBuilder.setView(setWorkoutView);
        final AlertDialog setWorkoutDialog = setWorkoutBuilder.create();
        setWorkoutDialog.show();

        /*setWorkoutBtn, when pressed, will take the values in the number picker, send it to an activity, and exit*/

        setWorkoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Retrieve and store user workout time*/
                int finalHr = Integer.parseInt(String.valueOf(setWorkoutHr.getValue()));
                int finalMin = Integer.parseInt(String.valueOf(setWorkoutHr.getValue()));

                /*Sends set hour and minutes user inputted into StartWorkout (Temporarily EndWorkout)*/
                Intent sendWorkoutTime = new Intent(MainActivity.this, CurrentWorkout.class);
                sendWorkoutTime.putExtra("hour", finalHr);
                sendWorkoutTime.putExtra("minutes", finalMin);

                /*Takes user to StartWorkout (Temporarily EndWorkout)*/
                Intent homeToStartWorkout = new Intent(MainActivity.this, CurrentWorkout.class);
                MainActivity.this.startActivity(homeToStartWorkout);
                startActivity(homeToStartWorkout);

                /*Test to see if values are correct in Logcat*/
                Log.d("picker value", String.valueOf(finalHr));
                Log.d("picker value", String.valueOf(finalMin));
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*Exits 'Set Workout Time' dialog without closing the activity*/
                setWorkoutDialog.cancel();
            }
        });



    }
}