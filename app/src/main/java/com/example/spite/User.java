package com.example.spite;

/*Methods list:
User()
User( String uid, String un, String em, String pw, String kl, double gl, String kid )
User( String uid )
String getUID(){
String getUsername()
String getPassword()
String getEmail()
double getGoal()
String getKyleName()
String getKyleUID()
void setUsername( String un )
void setPassword( String pw)
void setEmail( String em )
void setGoal( double gl)
void setKyleName( String kyle )
void setKyleUID( String kid )

Methods to add:
public double getDailyProgress(){}
public WeeklyWorkout<> getWeeklyProgress() {} //double sufficient or need weeklyworkoutclass
public void setNewKyle(){}
public WeeklyWorkout<> getKyleWeeklyProgress()
startNewWeek() {}
addWorkout() {}
 */

public class User {
    private String userUID;
    private String username;
    private String email;
    private String password; //do we need password?
    private double goal;
    private String kyleName;
    private String kyleUID;

    private User kyle;
    private String firstName;
    private String lastName;

    public User(){
        userUID = "JiggleUID";
        username = "username";
        email = "email";
        password = "password";
        goal = 0.0;
        kyleName = "Antagonist";
        kyleUID = "No Kyle ID";
    }
    public User( String uid, String un, String em, String pw, double gl, String kl, String kid ){
        userUID = uid;
        username = un;
        email = em;
        password = pw;
        kyleName = kl;
        goal = gl;
        kyleUID = kid;
    }

    public User( String uid ) { //call in conjunction with UserDBHandler getUser method
        userUID = uid;
        username = "username";
        email = "email";
        password = "password";
        goal = 0.0;
        kyleName = "Antagonist";
        kyleUID = "No Kyle ID";
    }

    public String getUID(){ return userUID; }
    public String getUsername(){ return username; }
    public String getPassword(){ return password; }
    public String getEmail(){ return email; }
    public double getGoal(){ return goal; }
    public String getKyleName(){ return kyleName; }
    public String getKyleUID(){ return kyleUID; }

    public void setUsername( String un ){ this.username = un; }
    public void setPassword( String pw){ this.password = pw; }
    public void setEmail( String em ){  this.email = em; }
    public void setGoal( double gl){  this.goal = gl; }
    public void setKyleName( String kyle ){ this.kyleName = kyle; }
    public void setKyleUID( String kid ){ this.kyleUID = kid; }



public String toString(){
    String user = "UID: " + userUID + "/Username: " + username + " /Email: " + email + " /Password: " + password;
    return user;
}

}
