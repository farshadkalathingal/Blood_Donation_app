package com.example.blooddonation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DonationView extends AppCompatActivity {

    private Button donateButton;
    private DatabaseReference mReff;
    public Date startDate, endDate;
    public String name,email,phone,blood,dob,gender,status,image;
    private FirebaseAuth mAuth;
    private CardView progressBar;
    FirebaseUser mUser;
    ConnectionDetector connectionDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation_view);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        EmailTrim(mUser.getEmail());
        donateButton = findViewById(R.id.donated_today);
        progressBar = (CardView) findViewById(R.id.progressBar);

        connectionDetector = new ConnectionDetector(this);

        donateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bloodDonated();
            }
        });


    }

    private void bloodDonated() {
        if( connectionDetector.isConnected()){
            endDate = new Date();
            User user = new User(name,email,phone,dob,gender,blood,"Not Available",image,endDate.toString());
            FirebaseDatabase.getInstance().getReference("Users").child(Log.id).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    //  progressBar.setVisibility(View.GONE);
                    if(task.isSuccessful()){

                        Toast.makeText(DonationView.this, "Data Added Successfully.", Toast.LENGTH_LONG).show();
                        finish();
                        Intent myIntent = new Intent(DonationView.this, HomeNavigation.class);
                        startActivity(myIntent);
                        finish();

                    }else{
                        Toast.makeText(DonationView.this, "Data Not Added Successfully.", Toast.LENGTH_LONG).show();
                    }
                }
            });
        } else {
            Toast.makeText(DonationView.this, "Not Connected to Internet.", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

        if(connectionDetector.isConnected()) {

            progressBar.setVisibility(View.VISIBLE);
            mReff = FirebaseDatabase.getInstance().getReference().child("Users").child(Log.id);
            mReff.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    progressBar.setVisibility(View.GONE);
                    name = dataSnapshot.child("userName").getValue().toString();
                    email = dataSnapshot.child("userEmail").getValue().toString();
                    phone = dataSnapshot.child("userPhone").getValue().toString();
                    blood = dataSnapshot.child("userBlood").getValue().toString();
                    dob = dataSnapshot.child("userDob").getValue().toString();
                    gender = dataSnapshot.child("userGender").getValue().toString();
                    status = dataSnapshot.child("userStatus").getValue().toString();
                    image = dataSnapshot.child("userImage").getValue().toString();
                    String sdate = dataSnapshot.child("userDonationDate").getValue().toString();
                    System.out.println("Date : " + sdate);
                    SimpleDateFormat frt = new SimpleDateFormat("EE MMM dd HH:mm:ss z yyyy");

                    if (!sdate.equals("")) {

                        try {
                            startDate = frt.parse(sdate);
                            System.out.println(startDate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        long difference = printDifference(startDate);
                        if (difference < 56) {
                            Toast.makeText(DonationView.this, "Your Time Period is not Over Yet.", Toast.LENGTH_LONG).show();
                            finish();
                            Intent myIntent = new Intent(DonationView.this, HomeNavigation.class);
                            startActivity(myIntent);
                            donateButton.setEnabled(false);
                            finish();

                        } else {
                            donateButton.setEnabled(true);
                        }

                    } else {
                        donateButton.setEnabled(true);
                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } else {
            Toast.makeText(DonationView.this, "Not Connected to Internet.", Toast.LENGTH_LONG).show();
        }
    }

    private long printDifference(Date sDate) {
        endDate = new Date();
        long different = endDate.getTime() - sDate.getTime();


        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;

        return elapsedDays;
    }

    public void EmailTrim(String str){
        String text = str.replaceAll("[^a-zA-Z0-9]", "");
        Log.id = text;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
        Intent myIntent = new Intent(DonationView.this, HomeNavigation.class);
        startActivity(myIntent);
        this.finish();
    }
}
