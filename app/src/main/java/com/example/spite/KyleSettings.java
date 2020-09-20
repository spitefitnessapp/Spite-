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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class KyleSettings extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    Button toMainBtn = null;
    Button updateKyleNameBtn = null;
    EditText newKyleNameET = null;
    TextView renameAntag = null;
    TextView antagCurrentName = null;

    private final String KYLE_NAME_KEY = "kyle";
    private String kyleName = "Kyle";
    private String USER_UID = "user01";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kyle_settings);

        toMainBtn = (Button) findViewById(R.id.chngKyleToMainBtn);
        updateKyleNameBtn = (Button) findViewById(R.id.changeKyleBtn);
        newKyleNameET = (EditText) findViewById(R.id.newKyleName);
        renameAntag = (TextView) findViewById(R.id.kyles);
        antagCurrentName = (TextView) findViewById(R.id.kyleName);


        updateKyleNameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateKyle();
                antagCurrentName.setText(kyleName);

            }
        });

        toMainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(KyleSettings.this, MainActivity.class);
                KyleSettings.this.startActivity(intent);
            }
        });

        DocumentReference mDocRef = db.collection("User").document(USER_UID);
        mDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String kyle = documentSnapshot.getString("kyle");
                antagCurrentName.setText(kyle);

            }
        });
    }

    private void updateKyle(){
        String newName = newKyleNameET.getText().toString();
        String msg = "nothing in the msg yet";

    if(newName.length() > 0)
    {
        kyleName = newName;
        msg = "New name: " + kyleName;
        Log.d("MAD", msg);
        DocumentReference mDocRef = db.collection("User").document(USER_UID);
        mDocRef.update("kyle", newName)
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
    else
    {
        msg = "No name entered";
        Log.d("MAD", msg);
    }
    }
}