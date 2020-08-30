package com.example.spite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class KyleSettings extends AppCompatActivity {

    Button toMainBtn = null;
    Button updateKyleNameBtn = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kyle_settings);

        toMainBtn = (Button) findViewById(R.id.chngKyleToMainBtn);
        updateKyleNameBtn = (Button) findViewById(R.id.changeKyleBtn);

        updateKyleNameBtn.setEnabled(false);

        toMainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(KyleSettings.this, MainActivity.class);
                KyleSettings.this.startActivity(intent);
            }
        });
    }
}