package com.example.finsec_finalfinalnajud;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class ScheduleListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ScheduleListAdapter adapter;
    private List<Schedule> scheduleList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_list);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // TODO: Fetch schedules from database, or create temporary data here
        scheduleList.add(new Schedule("name1", "date1", "desc1", "amount1"));
        scheduleList.add(new Schedule("name2", "date2", "desc2", "amount2"));

        adapter = new ScheduleListAdapter(scheduleList, this);
        recyclerView.setAdapter(adapter);
    }
}

