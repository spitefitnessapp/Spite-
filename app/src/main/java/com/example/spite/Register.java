package com.example.spite;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    EditText emailET = null;
    EditText emailConfirmET = null;
    EditText passwordET = null;
    Button regBtn = null;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
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
                Intent intent = new Intent(Register.this, MainActivity.class); //should be a screen abt verification email?
                //intent.putExtra( EMAIL_KEY, emailET.getText().toString() ); how do intents with fragmentsssssss
                Register.this.startActivity(intent);

            }
        });
    }

    //Still need to check whether email is already in use.
    private void registerUser()
    {
        if( emailET.getText().toString().equals( emailConfirmET.getText().toString() ) )
        {
            User user = new User();
            String email = emailET.getText().toString();
            String password = passwordET.getText().toString();

            Map<String, Object> saveUser = new HashMap<>();
            saveUser.put(EMAIL_KEY, email);
            saveUser.put(PASSWORD_KEY, password);
            saveUser.put(USERNAME_KEY, user.getUsername());
            saveUser.put(GOAL_KEY, user.getGoal());
            saveUser.put(KYLE_NAME_KEY, user.getKyleName());


            db.collection("User").document(email).set(saveUser)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(Register.this, "Success", Toast.LENGTH_SHORT).show();
                            Log.d("MAD", "Successfully added user");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Register.this, "Error!", Toast.LENGTH_SHORT).show();
                            Log.d("MAD", e.toString());
                        }
                    });
            Log.d("MAD", email);


        }
        else
        {
            Log.d("MAD", "Emails don't match uwu");
        }
        Log.d("MAD", "End of registerUser()");
    }
}