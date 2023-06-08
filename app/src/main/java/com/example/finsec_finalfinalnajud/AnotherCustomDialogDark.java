package com.example.finsec_finalfinalnajud;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AnotherCustomDialogDark extends AppCompatDialogFragment {
    private EditText etSavingsAdded;
    private EditText etGoalName;
    DatabaseReference dbFinsec = FirebaseDatabase.getInstance().getReferenceFromUrl("https://finsec-14c51-default-rtdb.firebaseio.com/");
    AnotherCustomDialogListener listener;

    private GoalSavings goalSavings;

    public AnotherCustomDialogDark(GoalSavings goalSavings) {
        this.goalSavings = goalSavings;
    }

    public AnotherCustomDialogDark() {
    }

    public static AnotherCustomDialogDark newInstance(String message, double goal) {
        AnotherCustomDialogDark fragment = new AnotherCustomDialogDark();

        Bundle args = new Bundle();
        args.putString("email4", message);
        args.putDouble("goal", goal);
        fragment.setArguments(args);

        return fragment;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View v = inflater.inflate(R.layout.another_customdialog_dark, null);

        etSavingsAdded = v.findViewById(R.id.layoutAmount);
        etGoalName = v.findViewById(R.id.etGoalName);

        if(getArguments() != null) {
            String email6 = getArguments().getString("email4");
            double goal = getArguments().getDouble("goal");
            builder.setView(v)
                    .setTitle("Add Savings")
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    })
                    .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String name = etGoalName.getText().toString();
                            double savings = Double.parseDouble(etSavingsAdded.getText().toString());
                            dbFinsec.child("users").child(email6).child("goalsavings").child(String.valueOf(goal).replace(".", "_")).child("goalname").setValue(name);
                            dbFinsec.child("users").child(email6).child("goalsavings").child(String.valueOf(goal).replace(".", "_")).child("savings").setValue(savings);
                            listener.applyChanges(name, savings);
                        }
                    });
        }
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (AnotherCustomDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement AnotherCustomDialogListener");
        }

    }

    public interface AnotherCustomDialogListener {
        void applyChanges(String name, double savings);
    }
}
