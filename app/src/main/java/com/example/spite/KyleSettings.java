package com.example.spite;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
    private ImageButton toSettingsBtn;
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

        toSettingsBtn = findViewById(R.id.toSettingsBtn);
        updateKyleNameBtn = (Button) findViewById(R.id.changeKyleBtn);
        newKyleNameET = (EditText) findViewById(R.id.newKyleName);
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
                Intent intent = new Intent(KyleSettings.this, Settings.class);
                KyleSettings.this.startActivity(intent);
            }
        });

        //return to Home
        toSettingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(KyleSettings.this, Settings.class);
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

    //to change Kyle User weekly, in conjunction with AlarmManager
    //for larger userbase- store IDs in a document on Firestore- ArrayList??
    private void resetKyle() {
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