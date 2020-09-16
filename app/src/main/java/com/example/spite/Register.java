package com.example.spite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.ui.auth.data.model.User;

import org.w3c.dom.Text;

public class Register extends AppCompatActivity {

    EditText UserName;
    EditText dateOfBirth;
    EditText weight;
    EditText height;

    Button regBtn;;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        UserName = findViewById(R.id.userName);
        dateOfBirth = findViewById(R.id.dateOfBirth);
        weight = findViewById(R.id.weight);
        height = findViewById(R.id.height);
        regBtn = findViewById(R.id.register);

    }
    private void register(View view) {
        Intent toHome = new Intent(this, MainActivity.class);
        startActivity(toHome);
        //String msg = "Welcome to Spike " + UserName.getText().toString();
        //Log.d("MAD", msg);
    }

}