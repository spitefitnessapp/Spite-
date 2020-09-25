package com.example.spite.models;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class WorkoutLog {
    private String userID;
    private long timeLogged;
    private Date date;
    private String time;

    public WorkoutLog(){
        //Public no-arg constructor needed
    }

    public WorkoutLog(String userID, long timeLogged){
        this.userID = userID;
        this.timeLogged = timeLogged;
        setDate();
    }

    public String getUserID(){
        return userID;
    }
    public long getTimeLogged(){
        return timeLogged;
    }

    public Date getDate(){
        return date;
    }

    public String getTime(){
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss z");
        time = dateFormat.format(date);
        return time;
    }


    private void setDate(){
        Calendar cal = Calendar.getInstance();
        this.date = cal.getTime();
    }
}




