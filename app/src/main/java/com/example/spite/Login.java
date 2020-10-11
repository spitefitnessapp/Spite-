package com.example.spite;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Login extends AppCompatActivity {

    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseUser user = auth.getCurrentUser();
    private static final int REQUEST_CODE = 101;
    private static final String USER_UID = "userID";
    List<AuthUI.IdpConfig> signUpOp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        signUpOp = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build()
        );
        user = auth.getCurrentUser();
       //Checks to see if user is already signed in if yes then app ones to home screen if not to the sign in screen
       if(user != null)
        {
            Intent resumeActivity = new Intent(this, MainActivity.class);
            startActivity(resumeActivity);
        }
        else{
            SignInOption();
        }
    }

    private void SignInOption(){
        /*Used this code instead to make my stuff work*/
        startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder()
                .setAvailableProviders(signUpOp)
                .setTheme(R.style.LoginTheme).setLogo(R.drawable.spite_logo)
                .build(),REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if (resultCode == RESULT_OK) {
                //Get Users
                //String email = user.getEmail();
                if(response.isNewUser())
                {
                    Toast.makeText(this, "Welcome to Spite!", Toast.LENGTH_SHORT).show();
                    Intent toRegister = new Intent(this, Register.class);
                    startActivity(toRegister);
                }
                else
                {
                    Toast.makeText(this, "Welcome back to Spite!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, MainActivity.class));
                }

                this.finish();

            } else {
                //Sign in fail
                Toast.makeText(this, "" + response.getError().getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}

