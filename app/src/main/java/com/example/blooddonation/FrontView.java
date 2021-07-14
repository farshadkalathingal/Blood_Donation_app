package com.example.blooddonation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class FrontView extends AppCompatActivity {

    private LinearLayout layout1, searchDonor;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    ConnectionDetector connectionDetector;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_front_view);

        mAuth = FirebaseAuth.getInstance();

        connectionDetector = new ConnectionDetector(this);
        layout1 = (LinearLayout) findViewById(R.id.LoginForm);
        searchDonor =(LinearLayout) findViewById(R.id.searchDonor);

        layout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent myIntent = new Intent(FrontView.this, LoginForm.class);
                startActivity(myIntent);
                finish();
            }
        });
        searchDonor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent myIntent = new Intent(FrontView.this, DonorView.class);
                startActivity(myIntent);
                finish();
            }
        });
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() != null){
                    finish();
                    startActivity(new Intent(FrontView.this, HomeNavigation.class));
                    finish();
                }
            }
        };

    }

    @Override
    protected void onStart() {
        super.onStart();

        if(connectionDetector.isConnected()) {
            mAuth.addAuthStateListener(mAuthListener);
        }
        else {
            Toast.makeText(FrontView.this, "Not Connected to Internet.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.MyDatePickerDialogStyle);

        builder.setMessage("Are you sure you want to Exit?").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
               // moveTaskToBack(true);
             //   android.os.Process.killProcess(andoid.os.Process.myPid());
                System.exit(1);
                finish();
                FrontView.super.onBackPressed();

            }
        })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
