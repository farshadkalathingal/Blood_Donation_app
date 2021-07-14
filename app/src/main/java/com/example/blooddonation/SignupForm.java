package com.example.blooddonation;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class SignupForm extends AppCompatActivity implements View.OnClickListener {


    private TextView tLogin;
    private EditText eName, eEmail, ePhone, ePassword, DOB;
    private Spinner spinner;
    private RadioButton Gender;
    private RadioGroup radioGroup;
    private Button bRegister;
    private CardView progressBar;
    private FirebaseAuth mAuth;
    ConnectionDetector connectionDetector;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_form);
        connectionDetector = new ConnectionDetector(this);

        tLogin = (TextView) findViewById(R.id.Login);
        eName = (EditText) findViewById(R.id.fname);
        eEmail = (EditText) findViewById(R.id.email);
        ePhone = (EditText) findViewById(R.id.phone);
        ePassword = (EditText) findViewById(R.id.password);
        bRegister = (Button) findViewById(R.id.register);
        progressBar = (CardView) findViewById(R.id.progressBar);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        spinner = (Spinner) findViewById(R.id.bloodGroup);
        DOB = (EditText) findViewById(R.id.dob);
        mAuth = FirebaseAuth.getInstance();

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(SignupForm.this,
                R.layout.color_spinner_layout, getResources().getStringArray(R.array.bloodgroups));
        myAdapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        spinner.setAdapter(myAdapter);
        radioGroup.check(R.id.rMale);

        DOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(SignupForm.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month = month + 1;
                        String date = day+"/"+month+"/"+year;
                        DOB.setText(date);
                    }
                },year,month,day);
                datePickerDialog.show();
                //datePickerDialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
            }
        });

        DOB.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    DatePickerDialog datePickerDialog = new DatePickerDialog(SignupForm.this,
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                    month = month + 1;
                                    String date = day+"/"+month+"/"+year;
                                    DOB.setText(date);
                                }
                            },year,month,day);
                    datePickerDialog.show();
                   // datePickerDialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT , WindowManager.LayoutParams.WRAP_CONTENT);
                    //datePickerDialog.getWindow().setLayout(1000,1400);
                   /* WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                    lp.copyFrom(datePickerDialog.getWindow().getAttributes());
                    lp.width = 150;
                    lp.height = 500;
                    lp.x=-170;
                    lp.y=100;
                    datePickerDialog.getWindow().setAttributes(lp);//
                    DisplayMetrics displayMetrics = new DisplayMetrics();
                    getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                    // The absolute width of the available display size in pixels.
                    int displayWidth = displayMetrics.widthPixels;
                    // The absolute height of the available display size in pixels.
                    int displayHeight = displayMetrics.heightPixels;

                    // Initialize a new window manager layout parameters
                    WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();

                    // Copy the alert dialog window attributes to new layout parameter instance
                    layoutParams.copyFrom(datePickerDialog.getWindow().getAttributes());

                    // Set the alert dialog window width and height
                    // Set alert dialog width equal to screen width 90%
                    // int dialogWindowWidth = (int) (displayWidth * 0.9f);
                    // Set alert dialog height equal to screen height 90%
                    // int dialogWindowHeight = (int) (displayHeight * 0.9f);

                    // Set alert dialog width equal to screen width 70%
                    int dialogWindowWidth = (int) (displayWidth * 0.8f);
                    // Set alert dialog height equal to screen height 70%
                    int dialogWindowHeight = (int) (displayHeight * 0.6f);

                    // Set the width and height for the layout parameters
                    // This will bet the width and height of alert dialog
                    layoutParams.width = dialogWindowWidth;
                    layoutParams.height = dialogWindowHeight;

                    // Apply the newly created layout parameters to the alert dialog window
                    datePickerDialog.getWindow().setAttributes(layoutParams);*/
                }
            }
        });

        bRegister.setOnClickListener(this);

        tLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent myIntent = new Intent(SignupForm.this, LoginForm.class);
                startActivity(myIntent);
                finish();
            }
        });

        eEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b){
                    String email = eEmail.getText().toString().trim();
                    if(!Verifications.isValid(email)){
                        Toast.makeText(SignupForm.this, "Invalid Email id.", Toast.LENGTH_LONG).show();
                        eEmail.setText("");
                    }
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        if(mAuth.getCurrentUser() != null){

        }
    }


    private void registerUser() {
        if(connectionDetector.isConnected()) {
            final String name, email, phone, pass, status, image;
            final String group, gender, dob;
            Gender = (RadioButton) findViewById(radioGroup.getCheckedRadioButtonId());
            group = spinner.getSelectedItem().toString().trim();
            gender = Gender.getText().toString().trim();
            dob = DOB.getText().toString().trim();
            name = eName.getText().toString().trim();
            email = eEmail.getText().toString().trim();
            phone = ePhone.getText().toString().trim();
            pass = ePassword.getText().toString().trim();
            status = "Available";
            //image = "";

            if (gender.equals("Male")) {
                image = "https://firebasestorage.googleapis.com/v0/b/blooddonation-985b8.appspot.com/o/male.png?alt=media&token=ef38725b-32af-4537-b278-c22dc428002b";
            } else if (gender.equals("Female")) {
                image = "https://firebasestorage.googleapis.com/v0/b/blooddonation-985b8.appspot.com/o/female.png?alt=media&token=f2ee5101-a239-4a2e-a8f0-b39b19c02078";
            } else {
                image = "https://firebasestorage.googleapis.com/v0/b/blooddonation-985b8.appspot.com/o/user.png?alt=media&token=d637a76f-2055-463b-bd84-bf30a11b1a9f";
            }

            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(pass) || TextUtils.isEmpty(dob) || TextUtils.isEmpty(gender) || group.equals("Blood Group")) {

                Toast.makeText(SignupForm.this, "Fields are Empty.", Toast.LENGTH_LONG).show();
            } else if (phone.length()<10) {
                Toast.makeText(SignupForm.this, "Phone Number is not Valid.", Toast.LENGTH_LONG).show();
            } else if (pass.length()<6) {
                Toast.makeText(SignupForm.this, "Password Must be Minimum 6 Characters.", Toast.LENGTH_LONG).show();
            } else{
                progressBar.setVisibility(View.VISIBLE);
                mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {


                            User user = new User(name, email, phone, dob, gender, group, status, image, "");
                            EmailTrim(email);

                            FirebaseDatabase.getInstance().getReference("Users").child(Log.id).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    progressBar.setVisibility(View.GONE);
                                    if (task.isSuccessful()) {

                                        Toast.makeText(SignupForm.this, "User Registered Successfully.", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(SignupForm.this, "User Not Registered Successfully.", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });

                        } else {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(SignupForm.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        } else {
            Toast.makeText(SignupForm.this, "Not Connected to Internet.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        this.finish();
        Intent myIntent = new Intent(SignupForm.this, LoginForm.class);
        startActivity(myIntent);
        this.finish();
        super.onBackPressed();
    }

    @Override
    public void onClick(View view) {
        registerUser();
    }

    public void EmailTrim(String str){
        String text = str.replaceAll("[^a-zA-Z0-9]", "");
        Log.id = text;
    }
}
