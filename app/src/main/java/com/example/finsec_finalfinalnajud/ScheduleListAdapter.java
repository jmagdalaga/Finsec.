package com.example.finsec_finalfinalnajud;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ScheduleListAdapter extends RecyclerView.Adapter<ScheduleListAdapter.ViewHolder> {
    private List<Schedule> scheduleList;
    private Context context;

    public ScheduleListAdapter(List<Schedule> scheduleList, Context context) {
        this.scheduleList = scheduleList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_schedule_card, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Schedule schedule = scheduleList.get(position);
        holder.textViewName.setText(schedule.getName());
        holder.textViewAmount.setText(schedule.getAmount());
        holder.textViewDate.setText(schedule.getDate());
        // set onClickListener
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ScheduleDetailActivity.class);
                intent.putExtra("schedule", schedule); // pass schedule to detail activity
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return scheduleList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewName;
        public TextView textViewAmount;
        public TextView textViewDate;
        public CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewAmount = itemView.findViewById(R.id.textViewAmount);
            textViewDate = itemView.findViewById(R.id.textViewDate);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}


