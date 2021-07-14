package com.example.blooddonation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeView extends AppCompatActivity {

    private LinearLayout searchDonor, addDetails, updateDnations, logoutForm, viewDetails;
    private FirebaseAuth mAuth;
    public TextView Name;
    FirebaseUser mUser;
    private DatabaseReference mReff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_view);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        EmailTrim(mUser.getEmail());

        searchDonor = (LinearLayout) findViewById(R.id.searchDonor);
        addDetails = (LinearLayout) findViewById(R.id.addDetails);
        updateDnations = (LinearLayout) findViewById(R.id.updateDonations);
        logoutForm = (LinearLayout) findViewById(R.id.logoutForm);
        viewDetails = (LinearLayout) findViewById(R.id.viewDetails);
        Name = findViewById(R.id.dName);

        searchDonor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent myIntent = new Intent(HomeView.this, DonorView.class);
                startActivity(myIntent);
            }
        });

        addDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent myIntent = new Intent(HomeView.this, FAQs.class);
                startActivity(myIntent);
            }
        });

        updateDnations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent myIntent = new Intent(HomeView.this, DonationView.class);
                startActivity(myIntent);
            }
        });

        logoutForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                finish();
                Intent myIntent = new Intent(HomeView.this, LoginForm.class);
                startActivity(myIntent);
            }
        });

        viewDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent myIntent = new Intent(HomeView.this, UserView.class);
                startActivity(myIntent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mReff = FirebaseDatabase.getInstance().getReference().child("Users").child(Log.id);
        mReff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Name.setText(dataSnapshot.child("userName").getValue().toString());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.MyDatePickerDialogStyle);

        builder.setMessage("Are you sure you want to Exit?").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
                System.exit(0);
                HomeView.super.onBackPressed();

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

    public void EmailTrim(String str){
        String text = str.replaceAll("[^a-zA-Z0-9]", "");
        Log.id = text;
    }
}
