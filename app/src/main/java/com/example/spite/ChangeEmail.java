package com.example.spite;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class ChangeEmail extends AppCompatActivity {

    Button chngEMtoMainBtn = null;
    Button chngEmailBtn = null;
    EditText newEmailET = null;
    EditText currentEmail = null;
    EditText confirmNewEmailET = null;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private String USER_UID = user.getUid();
    private static final String EMAIL_KEY = "email";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_email);

        chngEMtoMainBtn = (Button) findViewById(R.id.chngEMtoMainBtn);
        chngEmailBtn = (Button) findViewById(R.id.changeEmail);
        currentEmail = (EditText) findViewById(R.id.currentEmail);
        newEmailET = (EditText) findViewById(R.id.newEmail);
        confirmNewEmailET = (EditText) findViewById(R.id.confirmNewEmail);

        chngEmailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String newEmail = newEmailET.getText().toString();
                String confirmEmail = confirmNewEmailET.getText().toString();
                String cEmail = currentEmail.getText().toString();
                //Won't allow the user to change the email if the new email is same as the old one
                if( cEmail.equals(newEmail) )
                {
                    Log.d("MAD", "Emails are the same");
                    Toast.makeText(ChangeEmail.this, "Emails are the same", Toast.LENGTH_SHORT).show();
                    return;
                }
                //Won't allow the user to change the email if the email current email does'nt match the users current email in the authentication
                if(cEmail.equals(user.getEmail()))
                {
                    //Won't allow the user to change the email if the new email and confirm email don't match
                    if (newEmail.equals(confirmEmail))
                    {
                        changeEmail(confirmEmail);
                    }
                    else
                    {
                        Toast.makeText(ChangeEmail.this, " New email does'nt match confirm email", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                else
                {
                    Toast.makeText(ChangeEmail.this, "Email cannot be updated.", Toast.LENGTH_SHORT).show();
                    return;
                }

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

    private void changeEmail( String email )
    {
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