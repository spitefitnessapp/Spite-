package com.example.spite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Settings extends AppCompatActivity {

    Button chngEmail = null;
    Button chngPassword = null;
    Button chngKyle = null;
    Button logout = null;
    Button settingToMainBtn = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        chngEmail = (Button) findViewById(R.id.chngEmailBtn);
        chngPassword = (Button) findViewById(R.id.chngePasswordBtn);
        chngKyle = (Button) findViewById(R.id.kyleSettingsBtn);
        logout = (Button) findViewById(R.id.logoutBtn);
        settingToMainBtn = (Button) findViewById(R.id.settingToMainBtn);

        logout.setEnabled(false);

        chngEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Settings.this, ChangeEmail.class);
                Settings.this.startActivity(intent);
            }
        });

        chngPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Settings.this, ChangePassword.class);
                Settings.this.startActivity(intent);
            }
        });

        chngKyle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Settings.this, KyleSettings.class);
                Settings.this.startActivity(intent);
            }
        });

        settingToMainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Settings.this, MainActivity.class);
                Settings.this.startActivity(intent);
            }
        });
    }
}