package com.example.finsec_finalfinalnajud;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignInDark extends AppCompatActivity {
    DatabaseReference dbFinsec = FirebaseDatabase.getInstance().getReferenceFromUrl("https://finsec-14c51-default-rtdb.firebaseio.com/");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in_dark);

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

        EditText email = (EditText) findViewById(R.id.etEmailLogin);
        EditText pass = (EditText) findViewById(R.id.etPasswordLogin);
        Button btnLogin = (Button) findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String em = email.getText().toString();
                String password = pass.getText().toString();

                if(em.isEmpty() || password.isEmpty()) {
                    Toast.makeText(SignInDark.this, "All fields required", Toast.LENGTH_SHORT).show();
                    return;
                }

                String encodedEmail = em.replace(".", "_");
                dbFinsec.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.hasChild(encodedEmail)) {
                            String getPassword = snapshot.child(encodedEmail).child("password").getValue(String.class);

                            if(getPassword.equals(password)) {
                                Toast.makeText(SignInDark.this, "Logged in Succesfully", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(SignInDark.this, HomePageDark.class);
                                System.out.println(encodedEmail);
                                i.putExtra("email", encodedEmail);
                                startActivity(i);
                            } else {
                                Toast.makeText(SignInDark.this, "Wrong password", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(SignInDark.this, "Wrong email", Toast.LENGTH_SHORT).show();
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