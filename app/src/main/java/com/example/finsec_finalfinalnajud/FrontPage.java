package com.example.finsec_finalfinalnajud;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

public class FrontPage extends AppCompatActivity implements View.OnClickListener {

    Button fSignIn;
    Button fSignUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.front_page);

        int backgroundColor = Color.parseColor("#F1F1F1");
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

        fSignIn = (Button) findViewById(R.id.btnSignIn);
        fSignUp = (Button) findViewById(R.id.btnSignUp);

        fSignIn.setOnClickListener(this);
        fSignUp.setOnClickListener(this);

        Switch switchDarkMode = findViewById(R.id.switchDarkMode);
        switchDarkMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    // Switch is toggled on, start activity to display front_page_dark.xml
                    Intent intent = new Intent(FrontPage.this, FrontPageDark.class);
                    startActivity(intent);
                } else {
                    // Switch is toggled off, no action needed
                }
            }
        });
    }

    @Override
    public void onClick(View view) {

        Intent i;
        switch(view.getId()) {
            case R.id.btnSignIn:
                i = new Intent(FrontPage.this, SignIn.class);
                startActivity(i);
                break;
            case R.id.btnSignUp:
                i = new Intent(FrontPage.this, SignUp.class);
                startActivity(i);
                break;
        }
    }

    public static boolean isColorDark(int color) {
        double darkness = 1 - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255;
        return darkness >= 0.5;
    }
}