package com.example.finsec_finalfinalnajud;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class ScheduleDetailActivity extends AppCompatActivity {
    private TextView textViewName;
    private TextView textViewAmount;
    private TextView textViewDate;
    private TextView textViewDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_detail);

        textViewName = findViewById(R.id.textViewName);
        textViewAmount = findViewById(R.id.textViewAmount);
        textViewDate = findViewById(R.id.textViewDate);
        textViewDescription = findViewById(R.id.textViewDescription);

        Schedule schedule = (Schedule) getIntent().getSerializableExtra("schedule");
        textViewName.setText(schedule.getName());
        textViewAmount.setText(schedule.getAmount());
        textViewDate.setText(schedule.getDate());
        textViewDescription.setText(schedule.getDescription());
    }
}
