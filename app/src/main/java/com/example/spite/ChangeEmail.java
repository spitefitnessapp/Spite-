package com.example.spite;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class ChangeEmail extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private UserDBHandler dbh = new UserDBHandler();
    Button chngEMtoMainBtn;
    Button chngEmailBtn;
    EditText newEmailET;
    EditText confirmNewEmailET;
    EditText passwordET;
    private String password = "123";
    private String email = "rogue";


    private String id = "none";
    private String username ="none";
    private String kyleName = "none";
    private double goal = 0.0;
    private String kyleID = "none";
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_email);

        chngEMtoMainBtn = (Button) findViewById(R.id.chngEMtoMainBtn);
        chngEmailBtn = (Button) findViewById(R.id.changeEmail);
        newEmailET = (EditText) findViewById(R.id.newEmail);
        confirmNewEmailET = (EditText) findViewById(R.id.confirmNewEmail);
        passwordET = (EditText) findViewById(R.id.changeEmailPassword);


        /*Intent intent = getIntent();
        email = intent.getStringExtra("email");
        password = intent.getStringExtra("password");
        Log.d("MAD", email);*/

        chngEmailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeEmail();
                //do we want a notification here to say yes, email is updated?
            }
        });

        chngEMtoMainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChangeEmail.this, MainActivity.class);
                ChangeEmail.this.startActivity(intent);
            }
        });
        DocumentReference user = db.collection("User").document("user01");
        user.get().addOnCompleteListener(new OnCompleteListener < DocumentSnapshot > () {
            @Override
            public void onComplete(@NonNull Task < DocumentSnapshot > task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    id = (String) doc.get("userID");
                    email = (String) doc.get("email");
                    password = (String) doc.get("password");
                    username = (String) doc.get("username");
                    kyleName = (String) doc.get("kyle");
                    goal = (double) doc.get("goal");
                    kyleID = doc.getString("kyleUID");



                }
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
    }

    private void changeEmail()
    {
        if( passwordET.getText().toString().equals(password) ) {
            if (newEmailET.getText().toString().equals(confirmNewEmailET.getText().toString())) {
                String newEmail = newEmailET.getText().toString();

                //dbh.changeEmail( db, email, newEmail );

            } else {
                Log.d("MAD", "Email unchanged");
            }
        }
        else
        {Log.d("MAD", "Password doesnt match");}

    }
}