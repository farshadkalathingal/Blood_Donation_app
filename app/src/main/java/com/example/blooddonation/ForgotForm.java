package com.example.blooddonation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotForm extends AppCompatActivity {
    private TextView fEmail;
    private Button fButton;
    private CardView progressBar;
    private FirebaseAuth mAuth;
    ConnectionDetector connectionDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_form);

        connectionDetector = new ConnectionDetector(this);
        fEmail = findViewById(R.id.femail);
        fButton = findViewById(R.id.resetButton);
        progressBar = (CardView) findViewById(R.id.progressBar);
        mAuth = FirebaseAuth.getInstance();

        fButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (connectionDetector.isConnected()) {


                    String Email = fEmail.getText().toString().trim();
                    if (TextUtils.isEmpty(Email)) {
                        Toast.makeText(ForgotForm.this, "Field is Empty", Toast.LENGTH_LONG).show();
                    } else {
                        progressBar.setVisibility(View.VISIBLE);
                        mAuth.sendPasswordResetEmail(Email).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                progressBar.setVisibility(View.GONE);
                                if (!task.isSuccessful()) {

                                    Toast.makeText(ForgotForm.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();

                                } else {
                                    Toast.makeText(ForgotForm.this, "Reset Email Send to Your Email.", Toast.LENGTH_LONG).show();
                                    finish();
                                    Intent myIntent = new Intent(ForgotForm.this, LoginForm.class);
                                    startActivity(myIntent);
                                    finish();
                                }

                            }
                        });
                    }
                }else {
                    Toast.makeText(ForgotForm.this, "Not Connected to Internet.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Intent myIntent = new Intent(ForgotForm.this, LoginForm.class);
        startActivity(myIntent);
        this.finish();
    }
}
