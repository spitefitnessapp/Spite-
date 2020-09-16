package com.example.spite;

public class User {
    private String username;
    private String email;
    private String password; //do we need password?
    private double goal;
    private String kyleName;

    private User kyle;
    private String firstName;
    private String lastName;

    public User(){
        username = "username";
        email = "email";
        password = "password";
        goal = 0.0;
        kyleName = "Antagonist";
    }
    public User( String uName ){
        //use username to access firestore, fill attributes
    }
    public String getUsername(){ return username; }
    public String getPassword(){ return password; }
    public String getEmail(){ return email; }
    public double getGoal(){ return goal; }
    public String getKyleName(){ return kyleName; }

    public void setUsername( String un ){ this.username = un; }
    public void setPassword( String pw){ this.password = pw; }
    public void setEmail( String em ){  this.email = em; }
    public void setGoal( double gl){  this.goal = gl; }
    public void setKyleName( String kyle ){ this.kyleName = kyle; }

/*
    public double getDailyProgress(){}

    public WeeklyWorkout<> getWeeklyProgress() {} //double sufficient or need weeklyworkoutclass

    public void setNewKyle(){}

    public double getKyleDailyProgress()

    public WeeklyWorkout<> getKyleWeeklyProgress()
*/
/*
    startNewWeek() {}
    setNewKyle() {}
    addWorkout() {}
*/


}
