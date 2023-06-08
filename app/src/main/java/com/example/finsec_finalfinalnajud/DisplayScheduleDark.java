package com.example.finsec_finalfinalnajud;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DisplayScheduleDark extends AppCompatActivity {
    DatabaseReference dbFinsec = FirebaseDatabase.getInstance().getReferenceFromUrl("https://finsec-14c51-default-rtdb.firebaseio.com/");
    LinearLayout schedulelist;
    String type, email, date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_schedule_dark);

        schedulelist = findViewById(R.id.schedule_list);

        type = getIntent().getStringExtra("schedType");
        email = getIntent().getStringExtra("email");
        date = getIntent().getStringExtra("date");

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


        ImageView backToButton = findViewById(R.id.imgBackToSchedule);
        backToButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        dbFinsec.child("users").child(email).child("Schedule").child(type).child(date).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot timestampSnapshot : snapshot.getChildren()) {
                        String amountString = timestampSnapshot.child("samount").getValue(String.class);
                        if (amountString != null) {
                            double amount = Double.parseDouble(amountString);
                            String budget = timestampSnapshot.child("sbudget").getValue(String.class);
                            String description = timestampSnapshot.child("sdescription").getValue(String.class);

                            addView(budget, date, type, amount, description);
                        }
                    }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void addView(String name, String date, String schedType, double amount, String description) {
        View view = getLayoutInflater().inflate(R.layout.schedulelist_view_dark, null);

        TextView txtName = view.findViewById(R.id.txtSchedName);
        TextView txtDate = view.findViewById(R.id.txtSchedDate);
        TextView txtType = view.findViewById(R.id.txtSchedType);
        TextView txtAmount = view.findViewById(R.id.txtSchedAmount);
        TextView txtDesc = view.findViewById(R.id.txtSchedDescription);

        txtName.setText(name);

        SimpleDateFormat inputFormat = new SimpleDateFormat("MM-dd-yyyy", Locale.US);
        SimpleDateFormat outputFormat = new SimpleDateFormat("EEEE, MMMM d", Locale.US);
        try {
            Date dateFormatter = inputFormat.parse(date);
            String formattedDate = outputFormat.format(dateFormatter);
            txtDate.setText(formattedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        txtType.setText(schedType);

        NumberFormat n = NumberFormat.getInstance();
        n.setMaximumFractionDigits(2);
        n.setMinimumFractionDigits(2);
        txtAmount.setText("₱ " + n.format(amount));

        txtDesc.setText(description);
        schedulelist.addView(view);
    }

    public static boolean isColorDark(int color) {
        double darkness = 1 - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255;
        return darkness >= 0.5;
    }
}
