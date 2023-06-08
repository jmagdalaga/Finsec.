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

public class CustomDialog extends AppCompatDialogFragment {
    private EditText etGoalSavings;
    DatabaseReference dbFinsec = FirebaseDatabase.getInstance().getReferenceFromUrl("https://finsec-14c51-default-rtdb.firebaseio.com/");
    CustomDialogListener listener;

    public static CustomDialog newInstance(String message) {
        CustomDialog fragment = new CustomDialog();

        Bundle args = new Bundle();
        args.putString("email4", message);
        fragment.setArguments(args);

        return fragment;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View v = inflater.inflate(R.layout.custom_dialog, null);



        if(getArguments() != null) {
            String email5 = getArguments().getString("email4");
            builder.setView(v)
                    .setTitle("New Goal")
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    })
                    .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            double savings = Double.parseDouble(etGoalSavings.getText().toString());
                            dbFinsec.child("users").child(email5).child("goalsavings").child("goal").setValue(savings);
                            listener.applyChanges(savings);
                        }
                    });
        }
                etGoalSavings = v.findViewById(R.id.etGoalSavings);
                return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (CustomDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement CustomDialogListener");
        }

    }

    public interface CustomDialogListener {
        void applyChanges(double savings);
    }

}
