package com.example.spite.dbhandlers;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.spite.models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

/*Methods list:
UserDBHandler()
void addUser(FirebaseFirestore db, User user )
void changeUsername( FirebaseFirestore db, String uid, String username)
void changeKyleName( FirebaseFirestore db, String uid, String newKyleName)
void changeGoal(FirebaseFirestore db, String uid, double goal)
deleteUser()

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
    public void changeKyle( FirebaseFirestore db, String uid, String newKyleUID) {
        DocumentReference mDocRef = db.collection("User").document(uid);

        mDocRef
                .update(KYLE_UID_KEY, newKyleUID)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("MAD", "Kyle UID successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("MAD", "Error updating kyle UID", e);
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


    //does not delete subcollections.
    public void deleteUser( FirebaseFirestore db, String uid ){
        db.collection("User").document(uid)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("MAD", "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("MAD", "Error deleting document", e);
                    }
                });
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