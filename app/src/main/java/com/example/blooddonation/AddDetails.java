package com.example.blooddonation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddDetails extends AppCompatActivity {

    private Spinner spinner;
    private EditText Name, DOB;
    private RadioButton Gender;
    private RadioGroup radioGroup;
    private Button Add;
    private DatabaseReference mReff;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_details);

        Add = (Button) findViewById(R.id.addDetails);
        Name = (EditText) findViewById(R.id.fName);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);

        spinner = (Spinner) findViewById(R.id.bloodGroup);
        DOB = (EditText) findViewById(R.id.dob);

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(AddDetails.this,
                R.layout.color_spinner_layout, getResources().getStringArray(R.array.bloodgroups));
        myAdapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        spinner.setAdapter(myAdapter);


        DOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddDetails.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month = month + 1;
                        String date = day+"/"+month+"/"+year;
                        DOB.setText(date);
                    }
                },year,month,day);
                datePickerDialog.show();
            }
        });

        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String group,gender,dob;

                Gender = (RadioButton) findViewById(radioGroup.getCheckedRadioButtonId());

                group = spinner.getSelectedItem().toString();
                gender = Gender.getText().toString();
                dob = DOB.getText().toString();



                Details details = new Details(group, gender, dob);

                FirebaseDatabase.getInstance().getReference("Users").child(Log.id).push().setValue(details).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){

                            Toast.makeText(AddDetails.this, "Data Added Successfully.", Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(AddDetails.this, "Data Not Addes Successfully.", Toast.LENGTH_LONG).show();
                        }
                    }
                });


            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        mReff = FirebaseDatabase.getInstance().getReference().child("Users").child(Log.id);
        System.out.println(Log.id);
        mReff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String name = dataSnapshot.child("name").getValue().toString();
                Name.setText(name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
