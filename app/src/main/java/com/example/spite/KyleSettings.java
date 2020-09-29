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

import com.example.spite.dbhandlers.UserDBHandler;
import com.example.spite.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class KyleSettings extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private UserDBHandler dbh = new UserDBHandler();
    Button toMainBtn = null;
    Button updateKyleNameBtn = null;
    EditText newKyleNameET = null;
    TextView renameAntag = null;
    TextView antagCurrentName = null;

    private String kyleName = "Kyle";
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private String USER_UID = user.getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kyle_settings);

        toMainBtn = (Button) findViewById(R.id.chngKyleToMainBtn);
        updateKyleNameBtn = (Button) findViewById(R.id.changeKyleBtn);
        newKyleNameET = (EditText) findViewById(R.id.newKyleName);
        renameAntag = (TextView) findViewById(R.id.kyles);
        antagCurrentName = (TextView) findViewById(R.id.kyleName);

        //resetKyle();

        //Sets current Kyle name in antagCurrentName TextView
        DocumentReference mDocRef = db.collection("User").document(USER_UID);
        mDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String kyle = documentSnapshot.getString("kyle");
                antagCurrentName.setText(kyle);

            }
        });

        //to change Kyle name
        updateKyleNameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateKyle();
                antagCurrentName.setText(kyleName);

            }
        });

        //return to Home
        toMainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(KyleSettings.this, MainActivity.class);
                KyleSettings.this.startActivity(intent);
            }
        });

    }

    //To update the KyleName in DB
    private void updateKyle(){
        String newName = newKyleNameET.getText().toString();
        String msg = "nothing in the msg yet";

        if(newName.length() > 0)
        {
            kyleName = newName;
            dbh.changeKyleName( db, USER_UID, kyleName );
        }
        else
        {
            msg = "No name entered";
            Log.d("MAD", msg);
        }
    }
}