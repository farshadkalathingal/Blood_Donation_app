package com.example.blooddonation;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeNavigation extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private LinearLayout searchDonor, addDetails, updateDnations, logoutForm, viewDetails;
    private FirebaseAuth mAuth;
    public TextView Name;
    FirebaseUser mUser;
    private DatabaseReference mReff;
    ConnectionDetector connectionDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_navigation);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //toolbar.setNavigationIcon(R.drawable.ic_toolbar);
        toolbar.setTitle("");
        toolbar.setSubtitle("");
        connectionDetector = new ConnectionDetector(this);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.getMenu().getItem(0).setChecked(true);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        EmailTrim(mUser.getEmail());

        searchDonor = (LinearLayout) findViewById(R.id.searchDonor);
        addDetails = (LinearLayout) findViewById(R.id.addDetails);
        updateDnations = (LinearLayout) findViewById(R.id.updateDonations);
        logoutForm = (LinearLayout) findViewById(R.id.logoutForm);
        viewDetails = (LinearLayout) findViewById(R.id.viewDetails);
        Name = headerView.findViewById(R.id.navName);

        searchDonor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent myIntent = new Intent(HomeNavigation.this, DonorView.class);
                startActivity(myIntent);
                finish();
            }
        });

        addDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent myIntent = new Intent(HomeNavigation.this, FAQs.class);
                startActivity(myIntent);
                finish();
            }
        });

        updateDnations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent myIntent = new Intent(HomeNavigation.this, DonationView.class);
                startActivity(myIntent);
                finish();
            }
        });

        logoutForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(connectionDetector.isConnected()) {
                    mAuth.signOut();
                    finish();
                    Intent myIntent = new Intent(HomeNavigation.this, LoginForm.class);
                    startActivity(myIntent);
                    finish();
                }else {
                    Toast.makeText(HomeNavigation.this, "Not Connected to Internet.", Toast.LENGTH_LONG).show();
                }
            }
        });

        viewDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent myIntent = new Intent(HomeNavigation.this, UserView.class);
                startActivity(myIntent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.MyDatePickerDialogStyle);

            builder.setMessage("Are you sure you want to Exit?").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                    System.exit(0);
                    HomeNavigation.super.onBackPressed();

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

    @Override
    protected void onStart() {
        super.onStart();
        if(connectionDetector.isConnected()) {
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
        } else {
            Toast.makeText(HomeNavigation.this, "Not Connected to Internet.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_navigation, menu);
        return true;
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_gallery) {
            this.finish();
            Intent myIntent = new Intent(HomeNavigation.this, DonorView.class);
            startActivity(myIntent);
            finish();

        } else if (id == R.id.nav_view) {
            this.finish();
            Intent myIntent = new Intent(HomeNavigation.this, UserView.class);
            startActivity(myIntent);
            finish();

        } else if (id == R.id.nav_donation) {
            this.finish();
            Intent myIntent = new Intent(HomeNavigation.this, DonationView.class);
            startActivity(myIntent);
            finish();

        } else if (id == R.id.nav_logout) {
            mAuth.signOut();
            finish();
            Intent myIntent = new Intent(HomeNavigation.this, LoginForm.class);
            startActivity(myIntent);
            finish();

        } else if (id == R.id.nav_share) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT,
                    "Hey check out my app at: https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID);
            sendIntent.setType("text/plain");
            startActivity(sendIntent);

        } else if (id == R.id.nav_send) {
            this.finish();
            Intent myIntent = new Intent(HomeNavigation.this, AboutView.class);
            startActivity(myIntent);
            finish();

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void EmailTrim(String str){
        String text = str.replaceAll("[^a-zA-Z0-9]", "");
        Log.id = text;
    }
}
