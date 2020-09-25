package com.example.spite.models;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class WeeklyWorkout{
    String userID;
    Date date;
    Map<String, DailyWorkout> dailyWorkouts;

    public WeeklyWorkout(){
        //Public no-arg constructor needed
    }

    public WeeklyWorkout(String userID){
        this.userID = userID;
        setDate();
    }

    public String getUserID(){
        return userID;
    }

    public Date getDate(){
        return date;
    }

    public String getDateString(){
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String strDate = dateFormat.format(date);
        return strDate;
    }

    private void setDate(){
        Calendar cal = Calendar.getInstance();
        this.date = cal.getTime();
    }

    public Map<String, DailyWorkout> getDailyWorkouts(){
        return dailyWorkouts;
    }

    public void addDailyWorkout(DailyWorkout newDay){
        //If newWorkout is from the same username and is after the set date time.
        if(dailyWorkouts.size() != 7) {
            dailyWorkouts.put(newDay.getDay(), newDay);
        }
    }

}
