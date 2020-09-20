package com.example.spite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.firestore.FirebaseFirestore;

public class Register extends AppCompatActivity {

    EditText emailET = null;
    EditText emailConfirmET = null;
    EditText passwordET = null;
    Button regBtn = null;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private UserDBHandler dbh = new UserDBHandler();

    public static final String EMAIL_KEY = "email";
    public static final String PASSWORD_KEY = "password";
    public static final String USERNAME_KEY = "username";
    public static final String GOAL_KEY = "goal";
    public static final String KYLE_NAME_KEY = "kyle";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        emailET = (EditText) findViewById(R.id.emailRegTV);
        emailConfirmET = (EditText) findViewById(R.id.confirmEmailRegTV);
        passwordET = (EditText) findViewById(R.id.registerTextPassword);
        regBtn = (Button) findViewById(R.id.registerBtn);

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();

                Bundle newBundle = new Bundle();
                newBundle.putString( EMAIL_KEY, emailET.getText().toString() );
                FragmentHome objects = new FragmentHome();
                objects.setArguments(newBundle);

//Send email info to other activity, use DBHandler class?

                Log.d("MAD", "above intent");
                Intent intent = new Intent(Register.this, FragmentHome.class); //should be a screen abt verification email?
                //intent.putExtra( EMAIL_KEY, emailET.getText().toString() ); how do intents with fragmentsssssss
                Register.this.startActivity(intent);

            }
        });
    }

    //Still need to check whether email is already in use.
    private void registerUser()
    {
        String email = emailET.getText().toString();
        String password = passwordET.getText().toString();
        String uid = "rogue";

        if( emailET.getText().toString().equals( emailConfirmET.getText().toString() ) )
        {
           dbh.addUser( db, uid, email, password );
        }
        else
        {
            Log.d("MAD", "Emails don't match uwu");
        }
        Log.d("MAD", "End of registerUser()");
    }
}