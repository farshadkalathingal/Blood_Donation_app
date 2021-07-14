package com.example.blooddonation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    ConnectionDetector connectionDetector;

    public Date startDate, endDate;
    private DatabaseReference mReff;
    private static int SPLASH_TIME_OUT = 3500;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
                Intent homeIntent = new Intent(MainActivity.this, FrontView.class);
                startActivity(homeIntent);
                finish();
            }
        },SPLASH_TIME_OUT);

        connectionDetector = new ConnectionDetector(this);


    }

    @Override
    protected void onStart() {
        super.onStart();
        if(connectionDetector.isConnected()){
            mReff = FirebaseDatabase.getInstance().getReference().child("Users");
            mReff.orderByChild("userStatus").equalTo("Not Available").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String  uDD;
                        uDD = snapshot.child("userDonationDate").getValue().toString();
                        SimpleDateFormat frt = new SimpleDateFormat("EE MMM dd HH:mm:ss z yyyy");
                        try {
                            startDate = frt.parse(uDD);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        long difference = printDifference(startDate);
                        if( difference > 56) {
                            UpdateStatus updateStatus = new UpdateStatus(snapshot.child("userName").getValue().toString(),snapshot.child("userEmail").getValue().toString(),snapshot.child("userPhone").getValue().toString(),
                                    snapshot.child("userDob").getValue().toString(),snapshot.child("userGender").getValue().toString(),snapshot.child("userBlood").getValue().toString(),
                                    "Available",snapshot.child("userImage").getValue().toString(),uDD);

                            FirebaseDatabase.getInstance().getReference("Users").child(EmailTrim(snapshot.child("userEmail").getValue().toString())).setValue(updateStatus).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    //progressBar.setVisibility(View.GONE);
                                    if(task.isSuccessful()){

                                        // Toast.makeText(SignupForm.this, "User Registered Successfully.", Toast.LENGTH_LONG).show();
                                    }else{
                                        //Toast.makeText(SignupForm.this, "User Not Registered Successfully.", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                    }



                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        else {
            Toast.makeText(MainActivity.this, "Not Connected to Internet.", Toast.LENGTH_LONG).show();
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
    public String EmailTrim(String str){
        String text = str.replaceAll("[^a-zA-Z0-9]", "");
        return text;
    }
}
