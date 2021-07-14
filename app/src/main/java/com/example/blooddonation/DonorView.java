package com.example.blooddonation;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class DonorView extends AppCompatActivity {

    private Spinner spinner;
    private DatabaseReference mReff;
    private RecyclerView recyclerView;
    private static final int REQUEST_CALL = 1;
    public String Phone;
    private CardView progressBar;
    private ArrayList<Donor> list;
    public RecyclerView.Adapter mAdapter;
    ConnectionDetector connectionDetector;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_view);

        progressBar = (CardView) findViewById(R.id.progressBar);
        spinner = (Spinner) findViewById(R.id.bloodGroup);
        recyclerView = (RecyclerView) findViewById(R.id.donorList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        final ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(DonorView.this,
                R.layout.color_spinner_layout, getResources().getStringArray(R.array.bloodgroups));
        myAdapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        spinner.setAdapter(myAdapter);

        connectionDetector = new ConnectionDetector(this);

        mReff = FirebaseDatabase.getInstance().getReference().child("Users");

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (connectionDetector.isConnected()) {
                    //Toast.makeText(DonorView.this, "Connected to Internet.", Toast.LENGTH_LONG).show();
                    list = new ArrayList<Donor>();
                    mReff.orderByChild("userBlood").equalTo(spinner.getSelectedItem().toString()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                                if (!snapshot.child("userStatus").getValue().toString().equals("Not Available")) {

                                    Donor donor = new Donor(snapshot.child("userName").getValue().toString(), snapshot.child("userGender").getValue().toString(),
                                            snapshot.child("userPhone").getValue().toString(), snapshot.child("userImage").getValue().toString());

                                    list.add(donor);
                                    System.out.println(donor);
                                }

                            }
                            mAdapter = new DonorAdapter(list);
                            recyclerView.setAdapter(mAdapter);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }


                    });
                } else {
                    Toast.makeText(DonorView.this, "Not Connected to Internet.", Toast.LENGTH_LONG).show();
                }
            }
                @Override
                public void onNothingSelected (AdapterView < ? > adapterView){

                }

        });

    }



    void makePhoneCall(String phone){
        String number = phone;

        if(ContextCompat.checkSelfPermission(DonorView.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED ){
            ActivityCompat.requestPermissions(DonorView.this,new String[] {Manifest.permission.CALL_PHONE}, REQUEST_CALL);
        } else {
            String dial = "tel: +91" + number;
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
        }
    }


    @Override
    public void onBackPressed() {
        this.finish();
        Intent myIntent = new Intent(DonorView.this, FrontView.class);
        startActivity(myIntent);
        this.finish();
        super.onBackPressed();

    }


    public class DonorAdapter extends RecyclerView.Adapter<DonorAdapter.ViewHolder> {

        private ArrayList<Donor> mDonor;
        private static final int REQUEST_CALL = 1;

        public DonorAdapter(ArrayList<Donor> mDonor) {
            this.mDonor = mDonor;
        }

        @NonNull
        @Override
        public DonorAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent, false);
            return new DonorAdapter.ViewHolder(view);
        }



        @Override
        public void onBindViewHolder(@NonNull DonorAdapter.ViewHolder holder, final int position) {

            holder.tName.setText(mDonor.get(position).getUserName());
            holder.tGender.setText(mDonor.get(position).getUserGender());
            holder.tPhone.setText(mDonor.get(position).getUserPhone());
            Picasso.get().load(mDonor.get(position).getUserImage()).into(holder.tImage);

        }

        @Override
        public int getItemCount() {
            return mDonor.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView tName,tGender,tPhone;
            public CircleImageView tImage;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                tName = itemView.findViewById(R.id.cName);
                tGender = itemView.findViewById(R.id.cGender);
                tPhone = itemView.findViewById(R.id.cMobile);
                tImage = itemView.findViewById(R.id.viewImage);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.M)
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onClick(View view) {
                        Phone = tPhone.getText().toString();
                        makePhoneCall(Phone);


                    }
                });
            }


        }
    }

}
