package com.example.spite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

public class Register extends AppCompatActivity {

    EditText emailET = null;
    EditText emailConfirmET = null;
    EditText passwordET = null;
    Button regBtn = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        emailET = (EditText) findViewById(R.id.emailRegTV);
        emailConfirmET = (EditText) findViewById(R.id.confirmEmailRegTV);
        passwordET = (EditText) findViewById(R.id.registerTextPassword);
        regBtn = (Button) findViewById(R.id.registerBtn);

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
                Intent intent = new Intent(Register.this, Login.class); //should be a screen abt verification email?
                Register.this.startActivity(intent);
            }
        });
    }

    private void registerUser()
    {
        if( emailET.getText().toString().equals( emailConfirmET.getText().toString() ) )
        {
            String msg = "New user email: " + emailET.getText().toString() + " Password: " + passwordET.getText().toString();
            Log.d("MAD", msg);
        }
        else
        {
            Log.d("MAD", "Emails don't match uwu");
        }
    }
}