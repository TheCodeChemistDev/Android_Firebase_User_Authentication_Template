package com.thekemistlabs.loginandsignupdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    EditText etEmail, etPassword;
    Button btnSignUp, btnLogIn;
    private FirebaseAuth mAuth;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnSignUp = findViewById(R.id.btnSignUp);
        btnLogIn = findViewById(R.id.btnLogIn);

        currentUser = mAuth.getCurrentUser();
        if(currentUser != null) {
            redirect();
        }

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateData()) {
                    signUpUser();
                }
            }
        });

        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logInUser();
            }
        });

    }

    public void redirect() {
        Intent i = new Intent(MainActivity.this, WelcomeActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    public boolean validateData() {

        boolean dataIsValid = true;
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String error = "";

        if(TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            error += "Please enter a valid email address.\n";
            dataIsValid = false;
        }

        if(password.length() < 8) {
            error += "Password must be 8 or more characters";
            dataIsValid = false;
        }

        if(error != "") {
            Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT).show();
        }

        return dataIsValid;
    }

    public void signUpUser() {

        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            //Sign up was a success, redirect to Welcome Activity
                            currentUser = mAuth.getCurrentUser();
                            redirect();
                        } else {
                            //Sign up failed, alert user
                            Toast.makeText(MainActivity.this, "Sign up failed, please try again", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void logInUser() {

        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            currentUser = mAuth.getCurrentUser();
                            redirect();
                        } else {
                            Toast.makeText(MainActivity.this, "Log in failed, please try again", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}