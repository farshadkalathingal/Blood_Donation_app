package com.example.blooddonation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserView extends AppCompatActivity {

    private Button ok;
    private TextView Name , BloodGroup, UserSatus, Phone, Email, Gender, DOB;
    private CardView progressBar;
    private DatabaseReference mReff;
    ConnectionDetector connectionDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_view);

        connectionDetector = new ConnectionDetector(this);
        Name = (TextView) findViewById(R.id.fName);
        BloodGroup = (TextView) findViewById(R.id.bloodGroup);
        UserSatus = (TextView) findViewById(R.id.status);
        Phone = (TextView) findViewById(R.id.phoneNumber);
        Email = (TextView) findViewById(R.id.email);
        Gender = (TextView) findViewById(R.id.gender);
        DOB = (TextView) findViewById(R.id.dob);
        progressBar = (CardView) findViewById(R.id.progressBar);



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
                    String name = dataSnapshot.child("userName").getValue().toString();
                    String email = dataSnapshot.child("userEmail").getValue().toString();
                    String phone = dataSnapshot.child("userPhone").getValue().toString();
                    String blood = dataSnapshot.child("userBlood").getValue().toString();
                    String dob = dataSnapshot.child("userDob").getValue().toString();
                    String gender = dataSnapshot.child("userGender").getValue().toString();
                    String status = dataSnapshot.child("userStatus").getValue().toString();

                    Name.setText(name);
                    Email.setText(email);
                    Phone.setText(phone);
                    Gender.setText(gender);
                    BloodGroup.setText(blood);
                    DOB.setText(dob);
                    UserSatus.setText(status);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } else {
            Toast.makeText(UserView.this, "Not Connected to Internet.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
        Intent myIntent = new Intent(UserView.this, HomeNavigation.class);
        startActivity(myIntent);
        finish();
    }
}
