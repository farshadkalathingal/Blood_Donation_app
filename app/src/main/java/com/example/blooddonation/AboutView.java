package com.example.blooddonation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class AboutView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_view);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
        Intent myIntent = new Intent(AboutView.this, HomeNavigation.class);
        startActivity(myIntent);
        this.finish();
    }
}
