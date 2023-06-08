package com.example.finsec_finalfinalnajud;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;

public class HomePage extends AppCompatActivity {
    private int selectedTab = 1;
    private HashMap<Integer, Fragment> fragmentMap = new HashMap<>();

    ImageButton btnFrame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);

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

        final LinearLayout home = findViewById(R.id.homeLayout);
        final LinearLayout schedule = findViewById(R.id.scheduleLayout);
        final LinearLayout calcu = findViewById(R.id.calcuLayout);
        final LinearLayout advisor = findViewById(R.id.advisorLayout);

        final ImageView imgHome = findViewById(R.id.icHome);
        final ImageView imgSched = findViewById(R.id.icSchedule);
        final ImageView imgCalcu = findViewById(R.id.icCalculator);
        final ImageView imgAdvisor = findViewById(R.id.icAdvisor);
//
        final TextView txtHome = findViewById(R.id.txtHome);
        final TextView txtSched = findViewById(R.id.txtSchedule);
        final TextView txtCalcu = findViewById(R.id.txtCalculator);
        final TextView txtAdvisor = findViewById(R.id.txtAdvisor);

        String em = getIntent().getStringExtra("email");
        System.out.println(em + em + em);
        HomepageFragment hp = HomepageFragment.newInstance(em);

        switchFragment(1, hp.getClass(), em);

        fragmentMap.put(1, hp);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedTab != 1) {
                    switchFragment(1, hp.getClass(), em);

                    txtSched.setVisibility(View.GONE);
                    txtCalcu.setVisibility(View.GONE);
                    txtAdvisor.setVisibility(View.GONE);

                    imgSched.setImageResource(R.drawable.ic_schedule);
                    imgCalcu.setImageResource(R.drawable.ic_calculator);
                    imgAdvisor.setImageResource(R.drawable.ic_financialadvisor);

                    schedule.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),android.R.color.transparent));
                    calcu.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),android.R.color.transparent));
                    advisor.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),android.R.color.transparent));

                    txtHome.setVisibility(View.VISIBLE);
                    imgHome.setImageResource(R.drawable.ic_home_selected);
                    home.setBackgroundResource(R.drawable.tab_bg);

                    ScaleAnimation scaleAnimation = new ScaleAnimation(0.8f, 1.0f, 1f, 1f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
                    scaleAnimation.setDuration(200);
                    scaleAnimation.setFillAfter(true);
                    home.startAnimation(scaleAnimation);

                    selectedTab = 1;
                }
            }
        });

        schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedTab != 2) {
                    switchFragment(2, SchedulepageFragment.class, em);
                    txtHome.setVisibility(View.GONE);
                    txtCalcu.setVisibility(View.GONE);
                    txtAdvisor.setVisibility(View.GONE);

                    imgHome.setImageResource(R.drawable.ic_home);
                    imgCalcu.setImageResource(R.drawable.ic_calculator);
                    imgAdvisor.setImageResource(R.drawable.ic_financialadvisor);

                    home.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),android.R.color.transparent));
                    calcu.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),android.R.color.transparent));
                    advisor.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),android.R.color.transparent));

                    txtSched.setVisibility(View.VISIBLE);
                    imgSched.setImageResource(R.drawable.ic_schedule_selected);
                    schedule.setBackgroundResource(R.drawable.tab_bg);

                    ScaleAnimation scaleAnimation = new ScaleAnimation(0.8f, 1.0f, 1f, 1f, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
                    scaleAnimation.setDuration(200);
                    scaleAnimation.setFillAfter(true);
                    schedule.startAnimation(scaleAnimation);

                    selectedTab = 2;
                }
            }
        });

        calcu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedTab != 3) {

                    switchFragment(3, CalculatorpageFragment.class, em);

                    txtHome.setVisibility(View.GONE);
                    txtSched.setVisibility(View.GONE);
                    txtAdvisor.setVisibility(View.GONE);

                    imgHome.setImageResource(R.drawable.ic_home);
                    imgSched.setImageResource(R.drawable.ic_schedule);
                    imgAdvisor.setImageResource(R.drawable.ic_financialadvisor);

                    home.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),android.R.color.transparent));
                    schedule.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),android.R.color.transparent));
                    advisor.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),android.R.color.transparent));

                    txtCalcu.setVisibility(View.VISIBLE);
                    imgCalcu.setImageResource(R.drawable.ic_calculator_selected);
                    calcu.setBackgroundResource(R.drawable.tab_bg);

                    ScaleAnimation scaleAnimation = new ScaleAnimation(0.8f, 1.0f, 1f, 1f, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
                    scaleAnimation.setDuration(200);
                    scaleAnimation.setFillAfter(true);
                    calcu.startAnimation(scaleAnimation);

                    selectedTab = 3;
                }
            }
        });

        advisor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedTab != 4) {
                    switchFragment(4, AdvisorpageFragment.class, em);

                    txtHome.setVisibility(View.GONE);
                    txtSched.setVisibility(View.GONE);
                    txtCalcu.setVisibility(View.GONE);

                    imgHome.setImageResource(R.drawable.ic_home);
                    imgSched.setImageResource(R.drawable.ic_schedule);
                    imgCalcu.setImageResource(R.drawable.ic_calculator);

                    home.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),android.R.color.transparent));
                    schedule.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),android.R.color.transparent));
                    calcu.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),android.R.color.transparent));

                    txtAdvisor.setVisibility(View.VISIBLE);
                    imgAdvisor.setImageResource(R.drawable.ic_financialadvisor_selected);
                    advisor.setBackgroundResource(R.drawable.tab_bg);

                    ScaleAnimation scaleAnimation = new ScaleAnimation(0.8f, 1.0f, 1f, 1f, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
                    scaleAnimation.setDuration(200);
                    scaleAnimation.setFillAfter(true);
                    advisor.startAnimation(scaleAnimation);

                    selectedTab = 4;
                }
            }
        });
    }

    private void switchFragment(int tab, Class<? extends Fragment> fragmentClass, String email) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        Fragment currentFragment = fragmentMap.get(selectedTab);
        if(currentFragment != null) {
            ft.hide(currentFragment);
        }

        Fragment newFragment = fragmentMap.get(tab);
        if(newFragment == null) {
            try {
                if (fragmentClass == HomepageFragment.class) {
                    newFragment = HomepageFragment.newInstance(email);
                } else if (fragmentClass == SchedulepageFragment.class) {
                    newFragment = SchedulepageFragment.newInstance(email);
                } else {
                    newFragment = fragmentClass.newInstance();
                }
                fragmentMap.put(tab, newFragment);
                ft.add(R.id.fragmentContainer, newFragment);
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        } else {
            ft.show(newFragment);
        }

        ft.commit();
    }

    public boolean isColorDark(int color){
        double darkness = 1 - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255;
        return darkness >= 0.5;
    }
}


