package com.example.blooddonation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;



public class LoginForm extends AppCompatActivity {



    private EditText emailField;
    private EditText passwordField;
    private Button loginButton;
    private Button registerButton;
    private TextView fPassword;
    private CardView progressBar;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    ConnectionDetector connectionDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_form);

        mAuth = FirebaseAuth.getInstance();
        connectionDetector = new ConnectionDetector(this);

        emailField = (EditText) findViewById(R.id.email);
        passwordField = (EditText) findViewById(R.id.password);
        loginButton = (Button) findViewById(R.id.Login);
        registerButton = (Button) findViewById(R.id.Register);
        progressBar = (CardView) findViewById(R.id.progressBar);
        fPassword = (TextView) findViewById(R.id.forgotPassword);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() != null){
                    finish();
                    startActivity(new Intent(LoginForm.this, HomeNavigation.class));
                    finish();
                }
            }
        };

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startSignIn();


            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent myIntent = new Intent(LoginForm.this, SignupForm.class);
                startActivity(myIntent);
                finish();
            }
        });
        emailField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b){
                    String email = emailField.getText().toString().trim();
                    if(!Verifications.isValid(email)){
                        Toast.makeText(LoginForm.this, "Invalid Email id.", Toast.LENGTH_LONG).show();
                        emailField.setText("");
                    } else{
                        emailField.setText(email);
                    }
                }
            }
        });

        fPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent myIntent = new Intent(LoginForm.this, ForgotForm.class);
                startActivity(myIntent);
                finish();
            }
        });

    }



    @Override
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(mAuthListener);
    }

    private void startSignIn(){

        if(connectionDetector.isConnected()) {

            String email = emailField.getText().toString();
            String password = passwordField.getText().toString();

            EmailTrim(email);

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {

                Toast.makeText(LoginForm.this, "Fields are Empty", Toast.LENGTH_LONG).show();
            } else {
                progressBar.setVisibility(View.VISIBLE);
                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        if (!task.isSuccessful()) {

                            Toast.makeText(LoginForm.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();

                        } else {
                            Toast.makeText(LoginForm.this, "Log in Successfully.", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        } else {
            Toast.makeText(LoginForm.this, "Not Connected to Internet.", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onBackPressed() {
        this.finish();
        Intent myIntent = new Intent(LoginForm.this, FrontView.class);
        startActivity(myIntent);
        this.finish();
        super.onBackPressed();
    }

    public void EmailTrim(String str){
        String text = str.replaceAll("[^a-zA-Z0-9]", "");
        Log.id = text;
    }
}
