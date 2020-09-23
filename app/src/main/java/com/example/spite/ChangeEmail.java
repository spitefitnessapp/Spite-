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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private String USER_UID = user.getUid();
    private static final String EMAIL_KEY = "email";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_email);

        chngEMtoMainBtn = (Button) findViewById(R.id.chngEMtoMainBtn);
        chngEmailBtn = (Button) findViewById(R.id.changeEmail);
        newEmailET = (EditText) findViewById(R.id.newEmail);
        confirmNewEmailET = (EditText) findViewById(R.id.confirmNewEmail);
        passwordET = (EditText) findViewById(R.id.changeEmailPassword);

        chngEmailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newEmail = newEmailET.getText().toString();
                String password = passwordET.getText().toString();
                changeEmail( newEmail, password );
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

    }

    private void changeEmail( String email, String password )
    {
        if( user.getEmail().equals(email) )
        {
            Log.d("MAD", "Emails are the same");
            return;
        }
        if( !email.equals(confirmNewEmailET.getText().toString() ) )
        {
            Log.d("MAD", "Emails supplied don't match");
            return;
        }

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        user.updateEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("MAD", "User email address updated.");
                        }
                    }
                });

        DocumentReference mDocRef = db.collection("User").document(USER_UID);
        mDocRef.update(EMAIL_KEY, email)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("MAD", "Email successfully updated in Firestore!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("MAD", "Error updating email in Firestore", e);
                    }
                });
    }
}