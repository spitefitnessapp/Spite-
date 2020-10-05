package com.example.spite;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.spite.dbhandlers.DBWorkoutHandler;
import com.example.spite.dbhandlers.UserDBHandler;
import com.example.spite.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Register extends AppCompatActivity {

    EditText usernameET = null;
    EditText kyleNameET = null;
    EditText goalET = null;
    Button regBtn = null;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private UserDBHandler dbh = new UserDBHandler();
    private DBWorkoutHandler dbWorkoutHandler = new DBWorkoutHandler();

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private String USER_UID = user.getUid();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        usernameET = (EditText) findViewById(R.id.usernameET);
        kyleNameET = (EditText) findViewById(R.id.kyleNameET);
        goalET = (EditText) findViewById(R.id.registerGoalET);
        regBtn = (Button) findViewById(R.id.registerBtn);

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
                Intent intent = new Intent(Register.this, MainActivity.class);
                Register.this.startActivity(intent);

            }
        });
    }
    //Take in username, Kyle name, user goal. Set Kyle. Save user to DB.
    private void registerUser()
    {
        String username = usernameET.getText().toString();
        String kyleName = kyleNameET.getText().toString();
        double goal = Double.parseDouble( goalET.getText().toString() );

        User use = new User(USER_UID, username, user.getEmail(), "password", goal, kyleName, "user01");
        dbh.addUser(db, use);

        dbWorkoutHandler.createWeeklyWorkout(USER_UID);
        dbWorkoutHandler.createDailyWorkout(USER_UID);

        CollectionReference userCR = db.collection("User");
        userCR.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<String> UserList = new ArrayList<>();
                    for (DocumentSnapshot document : task.getResult()) {
                        User use = document.toObject(User.class);
                        String u = document.getId();
                        UserList.add( u );
                    }

                    int userListSize = UserList.size() - 1;
                    boolean done = false;
                    while ( !done ) {

                        int ran = new Random().nextInt(userListSize);

                        String randomUser = UserList.get(ran);

                        if (randomUser.equals(user.getUid())) {
                            Log.d("MAD", "Kyle cannot be current user");
                        }

                        else {
                            dbh.changeKyle(db, user.getUid(), randomUser);
                            String ids = user.getUid() + " is now paired with Kyle: " + randomUser;
                            Log.d("MAD", ids);
                            done = true;
                        }
                    }

                } else {
                    Log.d("MAD", "Error getting documents: ", task.getException());
                }
            }
        });
    }
}