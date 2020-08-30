package com.example.spite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class KyleProgress extends AppCompatActivity {

    Button kyleProgToMainBtn = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kyle_progress);

        kyleProgToMainBtn = (Button) findViewById(R.id.kyleProgtoMainBtn);

        kyleProgToMainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(KyleProgress.this, MainActivity.class);
                KyleProgress.this.startActivity(intent);
            }
        });
    }
}