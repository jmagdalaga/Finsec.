package com.example.finsec_finalfinalnajud;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ExpensePage extends AppCompatActivity implements View.OnClickListener {
    TextView txtGoal;
    TextView txtCurrentSavings;
    TextView txtTotalCurrentSavings;
    DatabaseReference dbFinsec = FirebaseDatabase.getInstance().getReferenceFromUrl("https://finsec-14c51-default-rtdb.firebaseio.com/");
    String email3;
    LinearLayout layoutlist;
    Button btnAddSavings;
    AlertDialog addSavingsDialog;
    AlertDialog newGoalDialog;
    String goal, date;
    ProgressBar progressBar;
    HashMap<String, Integer> indexMap = new HashMap<>();
    ArrayList barArrayList;
    BarChart barChart;
    BarDataSet barDataSet;
    BarData barData;
    TextView txtExpensePercent;
    private int currentWeekOfYear;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expense_page);

        Button back = (Button) findViewById(R.id.btnGoalSavingsBack);
        layoutlist = (LinearLayout) findViewById(R.id.layout_list);
        btnAddSavings = (Button) findViewById(R.id.btnAddSavings);
        Intent i = getIntent();
        email3 = i.getStringExtra("email2");
        date = i.getStringExtra("currentDate");

        txtCurrentSavings = findViewById(R.id.txtCurrentSavings);

        txtExpensePercent = findViewById(R.id.txtExpensePercent);

        currentWeekOfYear = getWeekOfYear(new SimpleDateFormat("MM-dd-yyyy").format(new Date()));

        btnAddSavings.setOnClickListener(this);
        back.setOnClickListener(this);

        buildNewGoalDialog();
        buildAddSavingsDialog();
        loadData();
        displayGraph();

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
    }

    private void buildAddSavingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.expense_customdialog, null);

        EditText etGoal = view.findViewById(R.id.etGoalName);
        EditText etSavingsAdded = view.findViewById(R.id.layoutAmount);
        builder.setView(view);
        builder.setTitle("Add Expenses")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        double expense = Double.parseDouble(etSavingsAdded.getText().toString());
                        try {
                            NumberFormat n = NumberFormat.getInstance(Locale.US);

                            double temp = n.parse(txtCurrentSavings.getText().toString().substring(1).replace(",", "")).doubleValue();
                            String num;
                            double goals;
                            int tempPer = Integer.parseInt(txtExpensePercent.getText().toString().substring(1, txtExpensePercent.getText().toString().indexOf("%")));

                            if(goal==null) {
                                goal = txtGoal.getText().toString().substring(2);
                            }

                            Number x = n.parse(goal);
                            num = x.toString();
                            goals = Double.parseDouble(num);
                            int percent;
                            if(goals == 0) {
                                throw new IllegalArgumentException("Set the your goal-savings first!");
                            }
                            if(goals >= expense) {
                                percent = (int) ((expense/goals) * 100);
                            } else {
                                throw new IllegalArgumentException("Goal max cap exceeded!");
                            }

                            if((temp + expense) > goals) {
                                throw new IllegalArgumentException("You have reached your goal. Set another one!");
                            }

                            DatabaseReference dateRef = dbFinsec.child("users").child(email3).child(date).child("Expenses");

                            dateRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    String expense = etSavingsAdded.getText().toString();
                                    String dbPercent = String.valueOf(percent);
                                    if(!snapshot.hasChild(etGoal.getText().toString())) {
                                        dateRef.child(etGoal.getText().toString()).child("expense").setValue("0");
                                        dateRef.child(etGoal.getText().toString()).child("percent").setValue("0");
                                    } else {
                                        String dbSavingsVal = snapshot.child(etGoal.getText().toString()).child("expense").getValue(String.class);
                                        String percentVal = snapshot.child(etGoal.getText().toString()).child("percent").getValue(String.class);
                                        int fs = Integer.parseInt(dbSavingsVal) + Integer.parseInt(expense);
                                        if(fs > goals) {
                                            throw new IllegalArgumentException("You have reached your goal. Set another one!");
                                        }
                                        int finalSavings = fs;
                                        int finalPercent = Integer.parseInt(percentVal) + Integer.parseInt(dbPercent);
                                        expense = String.valueOf(finalSavings);
                                        dbPercent = String.valueOf(finalPercent);
                                    }
                                    dateRef.child(etGoal.getText().toString()).child("expense").setValue(expense);
                                    dateRef.child(etGoal.getText().toString()).child("percent").setValue(dbPercent);

                                    String finalDbPercent = dbPercent;
                                    dateRef.child(etGoal.getText().toString()).child("expense").setValue(expense)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        dateRef.child(etGoal.getText().toString()).child("percent").setValue(finalDbPercent)
                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        if (task.isSuccessful()) {
                                                                            setTxtExpensePercent();
                                                                        }
                                                                    }
                                                                });
                                                    }
                                                }
                                            });
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                            setTxtCurrentSavings(etGoal.getText().toString(),temp + expense);
                            addView(etGoal.getText().toString(), Double.parseDouble(etSavingsAdded.getText().toString()), percent);

                        }  catch (ParseException e) {
                            throw new RuntimeException(e);
                        } catch(IllegalArgumentException il) {
                            Toast.makeText(ExpensePage.this, il.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        addSavingsDialog = builder.create();
    }

    private void buildNewGoalDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.custom_dialog, null);

        EditText etGoalSavings = view.findViewById(R.id.etGoalSavings);

        builder.setView(view);
        builder.setTitle("New Goal")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        NumberFormat n = NumberFormat.getInstance();
                        n.setMaximumFractionDigits(2);
                        n.setMinimumFractionDigits(2);

                        double goalValue = Double.parseDouble(etGoalSavings.getText().toString());


                        goal = String.valueOf((int)goalValue);
                        dbFinsec.child("users").child(email3).child("goal").setValue(goal);

                        setTxtGoal();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        newGoalDialog = builder.create();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.btnAddSavings:
                addSavingsDialog.show();
                break;
            case R.id.btnNewGoal:
                newGoalDialog.show();
                break;
            case R.id.btnGoalSavingsBack:
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK, returnIntent);

                finish();
                break;
        }
    }

    // Create a List to store the goal name order
    List<String> goalNameOrder = new ArrayList<>();

    public void addView(String goalname, double expense, int percent) {
        DatabaseReference userRef = dbFinsec.child("users").child(email3);

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ExpensePage.ExpenseData expenseData = new ExpensePage.ExpenseData(expense, percent);

                for (DataSnapshot dateSnapshot : snapshot.getChildren()) {
                    String date = dateSnapshot.getKey();

                    // Skip if it is otherfields
                    if (date.equals("contactNumber") || date.equals("dateofbirth") || date.equals("firstname")
                            || date.equals("gender") || date.equals("goal") || date.equals("lastname")
                            || date.equals("password")) continue;

                    if (dateSnapshot.child("Expenses").hasChild(goalname)) {
                        String dbExpense = dateSnapshot.child("Expenses").child(goalname).child("expense").getValue(String.class);
                        String dbPercent = dateSnapshot.child("Expenses").child(goalname).child("percent").getValue(String.class);

                        double dbExpenseValue = Double.parseDouble(dbExpense);
                        int dbPercentValue = Integer.parseInt(dbPercent);

                        expenseData.expense += dbExpenseValue;
                    }
                }

                // Update the view with consolidated data
                updateView(goalname, expenseData.expense, expenseData.percent);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public void updateView(String goalname, double expense, int percent) {
        View view = getLayoutInflater().inflate(R.layout.add_expense, null);

        TextView txtSavingsAdded = view.findViewById(R.id.txtSavingsAdded);
        TextView txtGoalName = view.findViewById(R.id.txtGoalName);

        NumberFormat n = NumberFormat.getInstance();
        n.setMaximumFractionDigits(2);
        n.setMinimumFractionDigits(2);

        txtGoalName.setText(goalname);
        txtSavingsAdded.setText("₱ " + n.format(expense));

        // if goal already exists, remove the old view
        if (goalNameOrder.contains(goalname)) {
            int index = goalNameOrder.indexOf(goalname);
            layoutlist.removeViewAt(index);
            goalNameOrder.remove(index);
        }

        // Add the new view at index 0 and update goalNameOrder
        layoutlist.addView(view, 0);
        goalNameOrder.add(0, goalname);
    }

    private void removeAllViewsExceptLast() {
        int childCount = layoutlist.getChildCount();

        // leave the last child (Button)
        if (childCount > 1) {
            layoutlist.removeViews(0, childCount - 1);
        }
    }

    public void loadData() {
        setTxtGoal();
        updateMonthBasedOnCurrentDate();
        DatabaseReference userRef = dbFinsec.child("users").child(email3);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                removeAllViewsExceptLast();

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
                String currentDateString = LocalDate.now().format(formatter);
                String currentMonth = currentDateString.substring(0, 2);
                String currentYear = currentDateString.substring(4, 8);
                String currentDate = currentYear + "-" + currentMonth;

                Map<String, ExpensePage.ExpenseData> consolidatedData = new HashMap<>();
                double totalSavings = 0;
                int totalPercent = 0;

                for (DataSnapshot dateSnapshot : snapshot.getChildren()) {
                    String date = dateSnapshot.getKey();
                    Log.d(TAG, "Processing date: " + date);
                    if (date.equals("contactNumber") || date.equals("dateofbirth") || date.equals("firstname")
                            || date.equals("gender") || date.equals("goal") || date.equals("lastname")
                            || date.equals("password")) continue;

                    String snapshotYear = date.substring(4, 8);
                    String snapshotMonth = date.substring(0, 2);
                    String snapshotDate = snapshotYear + "-" + snapshotMonth;
                    boolean isSameMonth = snapshotDate.equals(currentDate);

                    if (isSameMonth) {
                        for (DataSnapshot goalSnapshot : dateSnapshot.child("Expenses").getChildren()) {
                            String goalName = goalSnapshot.getKey();

                            if (goalSnapshot.child("expense").getValue() != null && goalSnapshot.child("percent").getValue() != null) {
                                String savingsStr = goalSnapshot.child("expense").getValue(String.class);
                                String percentStr = goalSnapshot.child("percent").getValue(String.class);

                                double expense = 0;
                                int percent = 0;

                                try {
                                    expense = Double.parseDouble(savingsStr);
                                    percent = Integer.parseInt(percentStr);
                                } catch (NumberFormatException e) {
                                    Log.e(TAG, "Failed to parse savings or percent for goal " + goalName);
                                }

                                if (consolidatedData.containsKey(goalName)) {
                                    ExpensePage.ExpenseData existingExpenseData = consolidatedData.get(goalName);
                                    existingExpenseData.expense += expense;
                                    existingExpenseData.percent += percent;
                                } else {
                                    consolidatedData.put(goalName, new ExpensePage.ExpenseData(expense, percent));
                                }

                                totalSavings += expense;
                                totalPercent += percent;
                            } else {
                                Log.e(TAG, "Null value for savings or percent for goal " + goalName);
                            }
                        }
                    }
                }

                for (Map.Entry<String, ExpensePage.ExpenseData> entry : consolidatedData.entrySet()) {
                    updateView(entry.getKey(), entry.getValue().expense, entry.getValue().percent);
                }

                NumberFormat n = NumberFormat.getInstance();
                n.setMaximumFractionDigits(2);
                n.setMinimumFractionDigits(2);

                txtCurrentSavings.setText("₱ " + n.format(totalSavings));
                txtExpensePercent.setText("+" + totalPercent + "%");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public static class ExpenseData {
        public double expense;
        public int percent;

        public ExpenseData(double expense, int percent) {
            this.expense = expense;
            this.percent = percent;
        }
    }



    public void setTxtGoal() {
        NumberFormat n = NumberFormat.getInstance();
        n.setMaximumFractionDigits(2);
        n.setMinimumFractionDigits(2);
        dbFinsec.child("users").child(email3).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild("goal")) {
                    goal = snapshot.child("goal").getValue(String.class);
                    double numValue = Double.parseDouble(goal);

                } else {
                    System.out.println("LOL its zero");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void setTxtCurrentSavings(String name, double expense) {
        NumberFormat n = NumberFormat.getInstance();
        n.setMaximumFractionDigits(2);
        n.setMinimumFractionDigits(2);

        DatabaseReference dbSavingsRef = dbFinsec.child("users").child(email3).child(date).child("Expenses").child(name);
        dbSavingsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

//                if(snapshot.hasChild())
                txtCurrentSavings.setText("₱ " + n.format(expense));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void setTxtExpensePercent() {
        DatabaseReference userRef = dbFinsec.child("users").child(email3);

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
                String currentDateString = LocalDate.now().format(formatter);
                String currentMonth = currentDateString.substring(0, 2);
                String currentYear = currentDateString.substring(6, 10);
                String currentDate = currentYear + "-" + currentMonth;

                int totalPercent = 0;

                for (DataSnapshot dateSnapshot : snapshot.getChildren()) {
                    String date = dateSnapshot.getKey();

                    if (date.equals("contactNumber") || date.equals("dateofbirth") || date.equals("firstname")
                            || date.equals("gender") || date.equals("goal") || date.equals("lastname")
                            || date.equals("password")) continue;

                    String snapshotYear = date.substring(4, 8);
                    String snapshotMonth = date.substring(0, 2);
                    String snapshotDate = snapshotYear + "-" + snapshotMonth;
                    boolean isSameMonth = snapshotDate.equals(currentDate);

                    if (isSameMonth) {
                        for (DataSnapshot goalSnapshot : dateSnapshot.child("Expenses").getChildren()) {
                            if (goalSnapshot.child("percent").getValue() != null) {
                                String percentStr = goalSnapshot.child("percent").getValue(String.class);

                                int percent = 0;

                                try {
                                    percent = Integer.parseInt(percentStr);
                                } catch (NumberFormatException e) {
                                    Log.e(TAG, "Failed to parse percent for goal " + goalSnapshot.getKey());
                                }

                                totalPercent += percent;
                            } else {
                                Log.e(TAG, "Null value for percent for goal " + goalSnapshot.getKey());
                            }
                        }
                    }
                }

                txtExpensePercent.setText("+" + totalPercent + "%");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static boolean isColorDark(int color) {
        double darkness = 1 - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255;
        return darkness >= 0.5;
    }

//    private void displayGraph() {
//        barChart = findViewById(R.id.barchart);
//        getData();
//        barDataSet = new BarDataSet(barArrayList, "Finsec.");
//        barData = new BarData(barDataSet);
//        barChart.setData(barData);
//        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
//        barDataSet.setValueTextColor(Color.BLACK);
//        barDataSet.setValueTextSize(16f);
//        barChart.getDescription().setEnabled(true);
//    }
//    private void getData() {
//        barArrayList = new ArrayList();
//        barArrayList.add(new BarEntry(2f, 10));
//        barArrayList.add(new BarEntry(3f, 20));
//        barArrayList.add(new BarEntry(4f, 30));
//        barArrayList.add(new BarEntry(5f, 40));
//        barArrayList.add(new BarEntry(6f, 50));
//    }
ArrayList<BarEntry> barEntries = new ArrayList<>();
    private void displayGraph() {
        barChart = findViewById(R.id.barchart);
        getDataAndUpdateChart();

        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(Arrays.asList("SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT")));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setCenterAxisLabels(false);
        xAxis.setGranularityEnabled(true);

        barChart.getDescription().setEnabled(true);
    }

    private void getDataAndUpdateChart() {
        dbFinsec.child("users").child(email3).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                HashMap<String, Float> expenseData = new HashMap<>();
                String lastDate = "";
                for (DataSnapshot dateSnapshot : snapshot.getChildren()) {
                    String dateString = dateSnapshot.getKey();

                    if (isValidDate(dateString)){
                        if (!lastDate.equals("")) {
                            int weekOfYear = getWeekOfYear(dateString);
                            int lastWeekOfYear = getWeekOfYear(lastDate);
                            if (weekOfYear != lastWeekOfYear) {
                                // A new week has started
                                expenseData.clear();
                                if (barEntries != null) {
                                    barEntries.clear();
                                }
                                if (barDataSet != null) {
                                    barDataSet.clear();
                                }
                            }
                        }
                        lastDate = dateString;

                        String dayOfWeek = getDayOfWeek(dateString);
                        float totalExpense = 0f;

                        for (DataSnapshot expensesSnapshot : dateSnapshot.getChildren()) {
                            if(expensesSnapshot.getKey().equals("Expenses")) {
                                for (DataSnapshot expenseNameSnapshot : expensesSnapshot.getChildren()) {
                                    String expenseValue = expenseNameSnapshot.child("expense").getValue(String.class);
                                    totalExpense += Float.parseFloat(expenseValue);
                                }
                            }
                        }
                        expenseData.put(dayOfWeek, totalExpense);
                    }
                }


                int index = 0;
                for (String dayOfWeek : Arrays.asList("SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT")) {
                    Float totalExpense = expenseData.getOrDefault(dayOfWeek, 0f);
                    barEntries.add(new BarEntry(index++, totalExpense));
                }

                barDataSet = new BarDataSet(barEntries, "Finsec.");
                barData = new BarData(barDataSet);
                barChart.setData(barData);

                barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                barDataSet.setValueTextColor(Color.BLACK);
                barDataSet.setValueTextSize(16f);

                barChart.notifyDataSetChanged();
                barChart.invalidate();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Error handling
            }
        });
    }

    private boolean isValidDate(String dateString){
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
        sdf.setLenient(false);
        try{
            sdf.parse(dateString);
            return true;
        } catch(ParseException e){
            return false;
        }
    }

    private String getDayOfWeek(String dateString){
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
        try{
            Date date = sdf.parse(dateString);
            SimpleDateFormat simpleDateformat = new SimpleDateFormat("EEE"); // the day of the week abbreviated
            return simpleDateformat.format(date).toUpperCase();
        } catch(ParseException e){
            return "";
        }
    }

    private int getWeekOfYear(String dateString){
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
        try{
            Date date = sdf.parse(dateString);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            return calendar.get(Calendar.WEEK_OF_YEAR);
        } catch(ParseException e){
            return -1;
        }
    }
//    ArrayList<Entry> lineEntries = new ArrayList<>();
//    LineChart lineChart;
//    LineDataSet lineDataSet;
//    LineData lineData;
//
//    private void displayGraph() {
//        lineChart = findViewById(R.id.linechart);
//        getDataAndUpdateChart();
//
//        XAxis xAxis = lineChart.getXAxis();
//        xAxis.setValueFormatter(new IndexAxisValueFormatter(Arrays.asList("SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT")));
//        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//        xAxis.setGranularity(1f);
//        xAxis.setCenterAxisLabels(false);
//        xAxis.setGranularityEnabled(true);
//
//        lineChart.getDescription().setEnabled(true);
//    }
//
//    private void getDataAndUpdateChart() {
//        dbFinsec.child("users").child(email3).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                HashMap<String, Float> expenseData = new HashMap<>();
//                String lastDate = "";
//                for (DataSnapshot dateSnapshot : snapshot.getChildren()) {
//                    String dateString = dateSnapshot.getKey();
//
//                    if (isValidDate(dateString)) {
//                        if (!lastDate.equals("")) {
//                            int weekOfYear = getWeekOfYear(dateString);
//                            int lastWeekOfYear = getWeekOfYear(lastDate);
//                            if (weekOfYear != lastWeekOfYear) {
//                                // A new week has started
//                                expenseData.clear();
//                                lineEntries.clear();
//                                if (lineDataSet != null) {
//                                    lineDataSet.clear();
//                                }
//                            }
//                        }
//                        lastDate = dateString;
//
//                        String dayOfWeek = getDayOfWeek(dateString);
//                        float totalExpense = 0f;
//
//                        for (DataSnapshot expensesSnapshot : dateSnapshot.getChildren()) {
//                            if(expensesSnapshot.getKey().equals("Expenses")) {
//                                for (DataSnapshot expenseNameSnapshot : expensesSnapshot.getChildren()) {
//                                    String expenseValue = expenseNameSnapshot.child("expense").getValue(String.class);
//                                    totalExpense += Float.parseFloat(expenseValue);
//                                }
//                            }
//                        }
//                        expenseData.put(dayOfWeek, totalExpense);
//                    }
//                }
//
//                lineEntries = new ArrayList<>();
//                int index = 0;
//                for (String dayOfWeek : Arrays.asList("SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT")) {
//                    Float totalExpense = expenseData.getOrDefault(dayOfWeek, 0f);
//                    lineEntries.add(new Entry(index++, totalExpense));
//                }
//
//                lineDataSet = new LineDataSet(lineEntries, "Finsec.");
//                lineData = new LineData(lineDataSet);
//                lineChart.setData(lineData);
//
//                lineDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
//                lineDataSet.setValueTextColor(Color.BLACK);
//                lineDataSet.setValueTextSize(16f);
//
//                lineChart.notifyDataSetChanged();
//                lineChart.invalidate();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                // Error handling
//            }
//        });
//    }
//
//    private boolean isValidDate(String dateString){
//        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
//        sdf.setLenient(false);
//        try{
//            sdf.parse(dateString);
//            return true;
//        } catch(ParseException e){
//            return false;
//        }
//    }
//
//    private String getDayOfWeek(String dateString){
//        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
//        try{
//            Date date = sdf.parse(dateString);
//            SimpleDateFormat simpleDateformat = new SimpleDateFormat("EEE"); // the day of the week abbreviated
//            return simpleDateformat.format(date).toUpperCase();
//        } catch(ParseException e){
//            return "";
//        }
//    }
//
//    private int getWeekOfYear(String dateString){
//        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
//        try{
//            Date date = sdf.parse(dateString);
//            Calendar calendar = Calendar.getInstance();
//            calendar.setTime(date);
//            return calendar.get(Calendar.WEEK_OF_YEAR);
//        } catch(ParseException e){
//            return -1;
//        }
//    }

    private void updateMonthBasedOnCurrentDate() {

        dbFinsec.child("users").child(email3).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String latestDate = null;

                for (DataSnapshot dateSnapshot : snapshot.getChildren()) {
                    String key = dateSnapshot.getKey();
                    if (isValidDate(key)) {
                        if (latestDate == null || key.compareTo(latestDate) > 0) {
                            latestDate = key;
                        }
                    }
                }

                if (latestDate != null) {
                    setMonthInTextView(latestDate);
                } else {
                    setMonthInTextView(getCurrentDate());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Error handling
            }
        });
    }

    private void setMonthInTextView(String dateStringFromDb) {
        SimpleDateFormat dbSdf = new SimpleDateFormat("MM-dd-yyyy"); // Assuming this is the format of your date in the database
        try {
            Date date = dbSdf.parse(dateStringFromDb);
            SimpleDateFormat monthSdf = new SimpleDateFormat("MMMM"); // "MMMM" for full name of the month
            String month = monthSdf.format(date);

            TextView txtMonthof = findViewById(R.id.txtMonthof);
            txtMonthof.setText("Month of " + month);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
        return sdf.format(new Date());
    }



}