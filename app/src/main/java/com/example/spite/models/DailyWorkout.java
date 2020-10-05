package com.example.spite.models;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DailyWorkout {
    private String userID;
    private String day;
    private Date date;
    private long dailyTimeLogged;
    private static final long maxDailyTime = 86400000; /*Milliseconds in one day*/
    private ArrayList<WorkoutLog> workoutLogs;

    public DailyWorkout(){
        //Public no-arg constructor needed
    }

    public DailyWorkout(String userID){
        this.userID = userID;
        this.dailyTimeLogged = 0;
        setDate();
        setDay();
    }

    public String getUserID(){
        return userID;
    }

    public String getDay(){
        return day;
    }

    public Date getDate(){
        return date;
    }

    public String getDateString(){
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String strDate = dateFormat.format(date);
        return strDate;
    }

    public long getDailyTimeLogged(){

        return dailyTimeLogged;
    }

    private void setDay(){
        DateFormat dateFormat = new SimpleDateFormat("EEE");
        this.day = dateFormat.format(date);
    }

    private void setDate(){
        Calendar cal = Calendar.getInstance();
        this.date = cal.getTime();
    }

    public void addWorkout(WorkoutLog newWorkout){
        //If newWorkout is from the same username and is after the set date time.
        if(dailyTimeLogged < maxDailyTime) {
            workoutLogs.add(newWorkout);
            this.dailyTimeLogged += newWorkout.getTimeLogged();
        }
    }
}
