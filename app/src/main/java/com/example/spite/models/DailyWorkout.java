package com.example.spite.models;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DailyWorkout {
    private String dailyWorkoutID;
    private String userID;
    private String day;
    private Date date;
    private long dailyTimeLogged;

    public DailyWorkout(){
        //Public no-arg constructor needed
    }

    public DailyWorkout(String userID){
        this.userID = userID;
        this.dailyTimeLogged = 0;
        setDate();
        setDay();

        this.dailyWorkoutID = getDateString() + day;
    }

    public String getDailyWorkoutID() {return dailyWorkoutID;}

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
}
