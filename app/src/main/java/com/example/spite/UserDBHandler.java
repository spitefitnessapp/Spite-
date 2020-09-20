package com.example.spite;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

/*Methods list:
UserDBHandler()
void addUser( FirebaseFirestore db, String uid, String pw ) WHAT INFO DO WE TAKE UPON REGISTER?
void addUser(FirebaseFirestore db, User user )
boolean checkUserExists( FirebaseFirestore db, String uid)
User getUser(FirebaseFirestore db, String uid)
void changeUsername( FirebaseFirestore db, String uid, String username)
void changeKyleName( FirebaseFirestore db, String uid, String newKyleName)
void changeGoal(FirebaseFirestore db, String uid, double goal)
User getKyle( FirebaseFirestore db, String kyleID )

Needed methods:
addKyleUID()
updateKyleUID()
 */

public class UserDBHandler {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "SPITE";
    private static final String USER_UID_KEY = "userID";
    private static final String USERNAME_KEY = "username";
    private static final String EMAIL_KEY = "email";
    private static final String PASSWORD_KEY = "password";
    private static final String KYLE_NAME_KEY = "kyle";
    private static final String GOAL_KEY = "goal";
    private static final String KYLE_UID_KEY = "kyleUID";

    boolean exists;
    User user;
    private String kyleID;


    public UserDBHandler() {
    }

    //For Register. Sets uid, email and password
    public void addUser(FirebaseFirestore db, String uid, String email, String password) {
        User user = new User( uid );
        user.setPassword(password);
        user.setEmail( email );

        Map<String, Object> saveUser = new HashMap<>();
        saveUser.put(EMAIL_KEY, user.getEmail());
        saveUser.put(PASSWORD_KEY, user.getPassword());
        saveUser.put(USERNAME_KEY, user.getUsername());
        saveUser.put(GOAL_KEY, user.getGoal());
        saveUser.put(KYLE_NAME_KEY, user.getKyleName());
        saveUser.put(KYLE_UID_KEY, user.getKyleUID());

        db.collection("User").document(uid).set(saveUser)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("MAD", "Successfully added user (UserdbHandler class)");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Log.d("MAD", e.toString());
                    }
                });
        Log.d("MAD", uid );

    }

    public void addUser(FirebaseFirestore db, User user ) {
        Map<String, Object> saveUser = new HashMap<>();
        saveUser.put(USER_UID_KEY, user.getUID());
        saveUser.put(EMAIL_KEY, user.getEmail());
        saveUser.put(PASSWORD_KEY, user.getPassword());
        saveUser.put(USERNAME_KEY, user.getUsername());
        saveUser.put(GOAL_KEY, user.getGoal());
        saveUser.put(KYLE_NAME_KEY, user.getKyleName());
        saveUser.put(KYLE_UID_KEY, user.getKyleUID());

        db.collection("User").document(user.getUID()).set(saveUser)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("MAD", "Successfully added user (UserdbHandler class)");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Log.d("MAD", e.toString());
                    }
                });
        Log.d("MAD", user.getUID() );

    }

    public boolean checkUserExists(FirebaseFirestore db, String uid) {

        DocumentReference mDocRef = db.collection("User").document(uid);

        mDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    exists = false;
                    Log.d("MAD", "checkUsermethod should hit trueeeeee");
                } else {
                    exists = true;
                    Log.d("MAD", "check user falseeee");
                }
            }

        });

        return exists;
    }


    public User getUser(FirebaseFirestore db, String uid ) {
        DocumentReference mDocRef = db.collection("User").document(uid);

        mDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                String id = documentSnapshot.getString(USER_UID_KEY);
                String email = documentSnapshot.getString(EMAIL_KEY);
                String password = documentSnapshot.getString(PASSWORD_KEY);
                String username = documentSnapshot.getString(USERNAME_KEY);
                String kyleName = documentSnapshot.getString(KYLE_NAME_KEY);
                double goal = documentSnapshot.getDouble(GOAL_KEY);
                String kyleID = documentSnapshot.getString(KYLE_UID_KEY);

                user = new User( id, username, email, password, goal, kyleName, kyleID );
            }
        });
        return user;
    }

    public void changeUsername( FirebaseFirestore db, String uid, String username) {

        DocumentReference mDocRef = db.collection("User").document(uid);

        mDocRef
                .update(USERNAME_KEY, username)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("MAD", "Username successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("MAD", "Error updating username", e);
                    }
                });
    }

    public void changeKyleName( FirebaseFirestore db, String uid, String newKyleName) {
        DocumentReference mDocRef = db.collection("User").document(uid);

        mDocRef
                .update(KYLE_NAME_KEY, newKyleName)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("MAD", "Kyle name successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("MAD", "Error updating kyle name", e);
                    }
                });
    }

    public void changeGoal(FirebaseFirestore db, String uid, double goal) {
        DocumentReference mDocRef = db.collection("User").document(uid);

        mDocRef
                .update(GOAL_KEY, goal)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("MAD", "Goal successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("MAD", "Error updating goal", e);
                    }
                });
    }

    public User getKyle( FirebaseFirestore db, String kyleID ) {
        user = new User( kyleID );
        DocumentReference mDocRef = db.collection("User").document(kyleID);
        mDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                double goal = documentSnapshot.getDouble(GOAL_KEY);
                //get progress, set progress etc
                user.setGoal(goal);
            }
        });
        return user;

    }

    /*
    public double getDailyProgress() {
    }

    public ____ getWeeklyProgress() {
    }

    public void changeKyleUser() {
    }

*/

}