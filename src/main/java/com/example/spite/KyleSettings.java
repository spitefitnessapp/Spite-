package com.example.spite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class KyleSettings extends AppCompatActivity {

    Button toMainBtn = null;
    Button updateKyleNameBtn = null;
    EditText newKyleNameET = null;
    TextView renameAntag = null;
    TextView antagCurrentName = null;
    private String kyleName = "Kyle"; //should it be null? get from user profile upon log in?

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kyle_settings);

        toMainBtn = (Button) findViewById(R.id.chngKyleToMainBtn);
        updateKyleNameBtn = (Button) findViewById(R.id.changeKyleBtn);
        newKyleNameET = (EditText) findViewById(R.id.newKyleName);
        renameAntag = (TextView) findViewById(R.id.kyles);
        antagCurrentName = (TextView) findViewById(R.id.kyleName);

        antagCurrentName.setText(kyleName);

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
    }

    private void updateKyle(){
        String newName = newKyleNameET.getText().toString();
        String msg = null;

    if(newName.length() > 0)
    {
        kyleName = newName;
        msg = "New name: " + kyleName;
        Log.d("MAD", msg);
    }
    else
    {
        msg = "No name entered";
        Log.d("MAD", msg);
    }
    }
}