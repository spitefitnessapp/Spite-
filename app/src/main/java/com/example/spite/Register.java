package com.example.spite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class Register extends AppCompatActivity {

    TextView emailTV = null;
    TextView emailConfirmTV = null;
    TextView passwordTV = null;
    Button regBtn = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        emailTV = (TextView) findViewById(R.id.emailRegTV);
        emailConfirmTV = (TextView) findViewById(R.id.confirmEmailRegTV);
        passwordTV = (TextView) findViewById(R.id.registerTextPassword);
        regBtn = (Button) findViewById(R.id.registerBtn);

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Register.this, Login.class);
                Register.this.startActivity(intent);
            }
        });
    }
}