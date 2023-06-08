package com.example.finsec_finalfinalnajud;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignUpDark extends AppCompatActivity {
    DatabaseReference dbFinsec = FirebaseDatabase.getInstance().getReferenceFromUrl("https://finsec-14c51-default-rtdb.firebaseio.com/");
    RadioGroup bgGender;
    EditText etFname;
    EditText etLname;
    EditText dobDay;
    EditText dobMonth;
    EditText dobYear;
    EditText etContactNumber;
    EditText etEmail;
    EditText etPassword;
    EditText etRepass;

    Button signUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_dark);

//        rbMale = (RadioButton) findViewById(R.id.rbMale);
//        rbFemale = (RadioButton) findViewById(R.id.rbFemale);
        bgGender = findViewById(R.id.bgGender);
        etFname = (EditText) findViewById(R.id.etFName);
        etLname = (EditText) findViewById(R.id.etLName);
        dobDay = (EditText) findViewById(R.id.etDay);
        dobMonth = (EditText) findViewById(R.id.etMonth);
        dobYear = (EditText) findViewById(R.id.etYear);
        etContactNumber = (EditText) findViewById(R.id.etContactNumber);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etRepass = (EditText) findViewById(R.id.etRepass);
        signUp = (Button) findViewById(R.id.btnCreateAccount);

        int backgroundColor = Color.parseColor("#222222");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (isColorDark(backgroundColor)) {
                    getWindow().getDecorView().setSystemUiVisibility(getWindow().getDecorView().getSystemUiVisibility() & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                } else {
                    getWindow().getDecorView().setSystemUiVisibility(getWindow().getDecorView().getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                }
            }
        }

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String gender;
                if(bgGender.getCheckedRadioButtonId() != -1) {
                    RadioButton rbSelected = (RadioButton) findViewById(bgGender.getCheckedRadioButtonId());
                    gender = rbSelected.getText().toString();
                } else {
                    gender = "";
                }

                String fname = etFname.getText().toString();
                String lname = etLname.getText().toString();
                String dateofbirth = dobDay.getText().toString() + "/" + dobMonth.getText().toString() + "/" + dobYear.getText().toString();
                String contactNumber = etContactNumber.getText().toString();
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();
                String repass = etRepass.getText().toString();
                if (gender.isEmpty() || fname.isEmpty() || lname.isEmpty() || dateofbirth.isEmpty() || contactNumber.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(SignUpDark.this, "All fields required", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!password.equals(repass)) {
                    Toast.makeText(SignUpDark.this, "Password does not match!", Toast.LENGTH_SHORT).show();
                    return;
                }

                String encodeEmail = email.replace(".", "_");
                dbFinsec.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.hasChild(encodeEmail)) {
                            Toast.makeText(SignUpDark.this, "Email already exists!", Toast.LENGTH_SHORT).show();
                        } else {
                            User u = new User(gender, fname, lname, dateofbirth, contactNumber, password);
                            dbFinsec.child("users").child(encodeEmail).setValue(u);
                            Toast.makeText(SignUpDark.this, "Registration Successful!", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(SignUpDark.this, HomePageDark.class);
                            i.putExtra("email", encodeEmail);

                            startActivity(i);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }

    public static boolean isColorDark(int color) {
        double darkness = 1 - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255;
        return darkness >= 0.5;
    }
}