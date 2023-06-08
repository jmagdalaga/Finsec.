package com.example.finsec_finalfinalnajud;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.content.Context;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SchedulepageFragmentDark#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SchedulepageFragmentDark extends Fragment implements View.OnClickListener, CalendarView.OnDateChangeListener {
    public static SchedulepageFragmentDark newInstance(String em) {
        Log.d(TAG, "newInstance called with: " + em);
        SchedulepageFragmentDark fragment = new SchedulepageFragmentDark();
        Bundle args = new Bundle();
        args.putString("encodedEmail", em);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            String encodedEmail = arguments.getString("encodedEmail");
            Log.d(TAG, "onCreate: encodedEmail: " + encodedEmail);
            if (encodedEmail != null) {
                email = encodedEmail;
            }
        } else {
            Log.d(TAG, "onCreate: getArguments is null");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.schedulepage_dark, container, false);

        Bundle arguments = getArguments();
        if (arguments != null) {
            String encodedEmail = arguments.getString("encodedEmail");
            if (encodedEmail != null) {
                email = encodedEmail;
            }
        }
        return view;
    }

    AlertDialog addNewBudget;
    DatabaseReference dbFinsec = FirebaseDatabase.getInstance().getReferenceFromUrl("https://finsec-14c51-default-rtdb.firebaseio.com/");
    String email, date, timeStamp;
    FloatingActionButton addBudgetFab, addExpenseFab, addBillsFab;
    ExtendedFloatingActionButton addActionsFab;
    TextView txtBudgetFab, txtExpenseFab, txtBillsFab;
    Button bottomsheet1, bottomsheet2, bottomsheet3;
    View overlay;
    boolean isAllFABVisible;
    LinearLayout botnav;
    private Animation rotateOpenAnim;
    private Animation rotateCloseAnim;
    private ImageView fabIcon;
    CalendarView calendarView;
    private long lastClickTime = 0;
    private static final long DOUBLE_CLICK_TIME_DELTA = 300;
    TextView txtExpensesNum, txtExpensesArrow, txtBudgetNum, txtBudgetArrow;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            String encodedEmail = arguments.getString("encodedEmail");
            if (encodedEmail != null) {
                email = encodedEmail;
            }
        }


        System.out.println(email);
        addBudgetFab = view.findViewById(R.id.schedBudget_fab);
        addExpenseFab = view.findViewById(R.id.schedExpense_fab);
        addBillsFab = view.findViewById(R.id.schedBills_fab);
        addActionsFab = view.findViewById(R.id.add_fab);

        txtBudgetFab = view.findViewById(R.id.txtFABbudget);
        txtExpenseFab = view.findViewById(R.id.txtFABexpense);
        txtBillsFab = view.findViewById(R.id.txtFABbills);

        calendarView = view.findViewById(R.id.calendarView);

        txtExpensesNum = view.findViewById(R.id.txtexpensesnum1);
        txtExpensesArrow = view.findViewById(R.id.txtexpensesarrow1);

        addBudgetFab.setVisibility(View.GONE);
        addExpenseFab.setVisibility(View.GONE);
        addBillsFab.setVisibility(View.GONE);
        txtBudgetFab.setVisibility(View.GONE);
        txtExpenseFab.setVisibility(View.GONE);
        txtBillsFab.setVisibility(View.GONE);

        overlay = view.findViewById(R.id.overlay);
        isAllFABVisible = false;

        rotateOpenAnim = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_open_anim);
        rotateCloseAnim = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_close_anim);
        addActionsFab.shrink();
        FloatingActionButton bottomsheet1 = view.findViewById(R.id.schedBudget_fab);
        FloatingActionButton bottomsheet2 = view.findViewById(R.id.schedExpense_fab);
        FloatingActionButton bottomsheet3 = view.findViewById(R.id.schedBills_fab);

        bottomsheet1.setOnClickListener(this);
        bottomsheet2.setOnClickListener(this);
        bottomsheet3.setOnClickListener(this);

        addActionsFab.setOnClickListener(this);
        overlay.setOnClickListener(this);

        calendarView.setOnDateChangeListener(this);
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault());
        String currentDate = sdf.format(new Date());
        setDailyExpenses(txtExpensesNum, txtExpensesArrow, currentDate);

        txtBudgetNum = view.findViewById(R.id.txtbudgetnum);
        txtBudgetArrow = view.findViewById(R.id.txtbudgetarrow);

        setDailySavings(txtBudgetNum, txtBudgetArrow, currentDate);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.schedBudget_fab:
                showBudgetScheduleDialog();
                break;
            case R.id.schedExpense_fab:
                showExpenseScheduleDialog();
                break;
            case R.id.schedBills_fab:
                showBillsScheduleDialog();
                break;
            case R.id.add_fab:
                if (!isAllFABVisible) {
                    overlay.setVisibility(View.VISIBLE);

                    addBudgetFab.show();
                    addExpenseFab.show();
                    addBillsFab.show();
                    txtBudgetFab.setVisibility(View.VISIBLE);
                    txtExpenseFab.setVisibility(View.VISIBLE);
                    txtBillsFab.setVisibility(View.VISIBLE);

                    addActionsFab.extend();

                    isAllFABVisible = true;

                } else {
                    overlay.setVisibility(View.GONE);

                    addBudgetFab.hide();
                    addExpenseFab.hide();
                    addBillsFab.hide();
                    txtBudgetFab.setVisibility(View.GONE);
                    txtExpenseFab.setVisibility(View.GONE);
                    txtBillsFab.setVisibility(View.GONE);

                    addActionsFab.shrink();

                    isAllFABVisible = false;

                }
                break;
            case R.id.overlay:
                overlay.setVisibility(View.GONE);

                addBudgetFab.hide();
                addExpenseFab.hide();
                addBillsFab.hide();
                txtBudgetFab.setVisibility(View.GONE);
                txtExpenseFab.setVisibility(View.GONE);
                txtBillsFab.setVisibility(View.GONE);

                addActionsFab.shrink();
                isAllFABVisible = false;
                break;
        }
    }

    @Override
    public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
        long clickTime = System.currentTimeMillis();
        if (clickTime - lastClickTime < DOUBLE_CLICK_TIME_DELTA) {
            // Perform action on double click
            // Your code here
            showScheduleForDate();
        }
        lastClickTime = clickTime;
        date =  String.format("%02d", i1+1) + "-" + String.format("%02d", i2) + "-" + Integer.toString(i);

        setDailyExpenses(txtExpensesNum, txtExpensesArrow, date);
        setDailySavings(txtBudgetNum, txtBudgetArrow, date);
    }

    private void showScheduleForDate() {
        if (email == null) {
            Log.e(TAG, "email3 is null");
            return;
        }
        Context context = getContext(); // or `this` for an Activity

        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.scheduledisplay_dialog_dark, null);
        dialog.setContentView(view);

        ImageView imgBack = view.findViewById(R.id.imgBack);
        Button btnSchedBudget = view.findViewById(R.id.btnSchedBudget);
        Button btnSchedBills = view.findViewById(R.id.btnSchedBills);
        Button btnSchedExpenses = view.findViewById(R.id.btnSchedExpenses);

        btnSchedBudget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), DisplaySchedule.class);
                i.putExtra("schedType", "Budget");
                i.putExtra("email", email);
                i.putExtra("date", date);
                startActivity(i);
            }
        });

        btnSchedBills.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), DisplaySchedule.class);
                i.putExtra("schedType", "Bills");
                i.putExtra("email", email);
                i.putExtra("date", date);
                startActivity(i);
            }
        });

        btnSchedExpenses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), DisplaySchedule.class);
                i.putExtra("schedType", "Expenses");
                i.putExtra("email", email);
                i.putExtra("date", date);
                startActivity(i);
            }
        });
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        commonDialogConfig(dialog);
    }

    private void showBudgetScheduleDialog() {
        if (email == null) {
            Log.e(TAG, "email3 is null");
            return;
        }
        System.out.println(email);
        Context context = getContext(); // or `this` for an Activity

        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.budgetschedule_bottomsheet_dark, null);
        dialog.setContentView(view);

        EditText etBudgetName = view.findViewById(R.id.layoutBudgetName);
        EditText etAmount = view.findViewById(R.id.layoutAmount);
        EditText etDate = view.findViewById(R.id.layoutDate);
        EditText etDescription = view.findViewById(R.id.layoutDescription);
        Button btnSchedule = view.findViewById(R.id.btnschedule);

        etDate.setText(date);
        commonDialogConfig(dialog);

        btnSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeStamp = String.valueOf(System.currentTimeMillis());

                // get data from inputs
                dbFinsec.child("users").child(email).child("Schedule").child("Budget").child(date).child(timeStamp).child("sbudget").setValue(etBudgetName.getText().toString());
                dbFinsec.child("users").child(email).child("Schedule").child("Budget").child(date).child(timeStamp).child("samount").setValue(etAmount.getText().toString());
                dbFinsec.child("users").child(email).child("Schedule").child("Budget").child(date).child(timeStamp).child("sdate").setValue(etDate.getText().toString());
                dbFinsec.child("users").child(email).child("Schedule").child("Budget").child(date).child(timeStamp).child("sdescription").setValue(etDescription.getText().toString());
                dialog.dismiss();
            }
        });

    }

    private void showExpenseScheduleDialog() {
        if (email == null) {
            Log.e(TAG, "email3 is null");
            return;
        }
        System.out.println(email);
        Context context = getContext(); // or `this` for an Activity

        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.expenseschedule_bottomsheet_dark, null);
        dialog.setContentView(view);

        EditText etExpenseName = view.findViewById(R.id.layoutExpenseName);
        EditText etAmount = view.findViewById(R.id.layoutAmount);
        EditText etDate = view.findViewById(R.id.layoutDate);
        EditText etDescription = view.findViewById(R.id.layoutDescription);
        Button btnSchedule = view.findViewById(R.id.btnschedule);

        etDate.setText(date);
        commonDialogConfig(dialog);
        btnSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeStamp = String.valueOf(System.currentTimeMillis());

                // get data from inputs
                dbFinsec.child("users").child(email).child("Schedule").child("Expenses").child(date).child(timeStamp).child("sbudget").setValue(etExpenseName.getText().toString());
                dbFinsec.child("users").child(email).child("Schedule").child("Expenses").child(date).child(timeStamp).child("samount").setValue(etAmount.getText().toString());
                dbFinsec.child("users").child(email).child("Schedule").child("Expenses").child(date).child(timeStamp).child("sdate").setValue(etDate.getText().toString());
                dbFinsec.child("users").child(email).child("Schedule").child("Expenses").child(date).child(timeStamp).child("sdescription").setValue(etDescription.getText().toString());
                dialog.dismiss();
            }
        });

    }

    private void showBillsScheduleDialog() {
        if (email == null) {
            Log.e(TAG, "email3 is null");
            return;
        }
        System.out.println(email);
        Context context = getContext(); // or `this` for an Activity

        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.billsschedule_bottomsheet_dark, null);
        dialog.setContentView(view);

        EditText etBillsName = view.findViewById(R.id.layoutBillsName);
        EditText etAmount = view.findViewById(R.id.layoutAmount);
        EditText etDate = view.findViewById(R.id.layoutDate);
        EditText etDescription = view.findViewById(R.id.layoutDescription);
        Button btnSchedule = view.findViewById(R.id.btnschedule);

        etDate.setText(date);
        commonDialogConfig(dialog);

        btnSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeStamp = String.valueOf(System.currentTimeMillis());

                dbFinsec.child("users").child(email).child("Schedule").child("Bills").child(date).child(timeStamp).child("sbudget").setValue(etBillsName.getText().toString());
                dbFinsec.child("users").child(email).child("Schedule").child("Bills").child(date).child(timeStamp).child("samount").setValue(etAmount.getText().toString());
                dbFinsec.child("users").child(email).child("Schedule").child("Bills").child(date).child(timeStamp).child("sdate").setValue(etDate.getText().toString());
                dbFinsec.child("users").child(email).child("Schedule").child("Bills").child(date).child(timeStamp).child("sdescription").setValue(etDescription.getText().toString());
                dialog.dismiss();
            }
        });
    }

    private void setDailySavings(TextView txtSavingsNum, TextView txtSavingsArrow, String date) {
        if (email == null) {
            Log.e(TAG, "email3 is null");
            return;
        }

        DatabaseReference userRef = dbFinsec.child("users").child(email).child("Schedule").child("Budget").child(date);
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                double totalSavings = 0;
                for (DataSnapshot goalSnapshot : snapshot.getChildren()) {
                    if (goalSnapshot.child("samount").getValue() != null) {
                        String amount = goalSnapshot.child("samount").getValue(String.class);

                        double savings = 0;
                        try {
                            savings = Double.parseDouble(amount);
                        } catch (NumberFormatException e) {
                            Log.e(TAG, "Failed to parse savings for date " + date);
                        }

                        totalSavings += savings;
                    } else {
                        Log.e(TAG, "Null value for savings for date " + date);
                    }
                }

                NumberFormat n = NumberFormat.getInstance();
                n.setMaximumFractionDigits(2);
                n.setMinimumFractionDigits(2);
                txtSavingsNum.setText(String.format("₱ " + n.format(totalSavings)));
                if (totalSavings > 0) {
                    txtSavingsArrow.setText("⬆");
                } else {
                    txtSavingsArrow.setText("-");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error...
            }
        });
    }
    private void setDailyExpenses(TextView txtExpensesNum, TextView txtExpensesArrow, String date) {
        if (email == null) {
            Log.e(TAG, "email3 is null");
            return;
        }

        DatabaseReference userRef = dbFinsec.child("users").child(email).child("Schedule").child("Expenses").child(date);
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                double totalExpenses = 0;
                for (DataSnapshot expenseSnapshot : snapshot.getChildren()) {
                    if (expenseSnapshot.child("samount").getValue() != null) {
                        String expenseStr = expenseSnapshot.child("samount").getValue(String.class);

                        double expense = 0;
                        try {
                            expense = Double.parseDouble(expenseStr);
                        } catch (NumberFormatException e) {
                            Log.e(TAG, "Failed to parse expense for date " + date);
                        }

                        totalExpenses += expense;
                    } else {
                        Log.e(TAG, "Null value for expense for date " + date);
                    }
                }

                NumberFormat n = NumberFormat.getInstance();
                n.setMaximumFractionDigits(2);
                n.setMinimumFractionDigits(2);
                txtExpensesNum.setText(String.format("₱ " + n.format(totalExpenses)));
                if (totalExpenses > 0) {
                    txtExpensesArrow.setText("⬆");
                } else {
                    txtExpensesArrow.setText("-");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error...
            }
        });
    }
    private void commonDialogConfig(Dialog dialog){
        if (email == null) {
            Log.e(TAG, "email3 is null");
            return;
        }
        System.out.println(email);
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }
}
