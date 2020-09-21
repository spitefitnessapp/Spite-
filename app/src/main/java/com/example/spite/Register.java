package com.example.spite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class Register extends AppCompatActivity {

    EditText usernameET = null;
    EditText kyleNameET = null;
    EditText goalET = null;
    Button regBtn = null;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private UserDBHandler dbh = new UserDBHandler();

    public static final String EMAIL_KEY = "email";
    public static final String PASSWORD_KEY = "password";
    public static final String USERNAME_KEY = "username";
    public static final String GOAL_KEY = "goal";
    public static final String KYLE_NAME_KEY = "kyle";

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



//Send email info to other activity, use DBHandler class?

                Log.d("MAD", "above intent");
                Intent intent = new Intent(Register.this, MainActivity.class); //should be a screen abt verification email?
                //intent.putExtra( EMAIL_KEY, emailET.getText().toString() ); how do intents with fragmentsssssss
                Register.this.startActivity(intent);

            }
        });
    }

    //Still need to check whether email is already in use.
    //allocate a kyle
    private void registerUser()
    {
        String username = usernameET.getText().toString();
        String kyleName = kyleNameET.getText().toString();
        double goal = Double.parseDouble( goalET.getText().toString() );

        User use = new User( USER_UID, username, user.getEmail(), "password", goal, kyleName, "user01");
        dbh.addUser( db, use );
    }
}