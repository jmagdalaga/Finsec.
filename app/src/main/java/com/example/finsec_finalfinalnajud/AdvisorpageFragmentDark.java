package com.example.finsec_finalfinalnajud;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AdvisorpageFragmentDark#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdvisorpageFragmentDark extends Fragment {
    ImageButton popupBTN;
    Dialog mDialog;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        popupBTN = view.findViewById(R.id.imgbtnchinkee);
        mDialog=new Dialog(requireContext());

        popupBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.setContentView(R.layout.advisorypopupchinkee_dark);
                mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                mDialog.show();
            }
        });
        popupBTN = view.findViewById(R.id.imgbtnfitz);
        mDialog=new Dialog(requireContext());

        popupBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.setContentView(R.layout.advisorypopupfitz_dark);
                mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                mDialog.show();
            }
        });
        popupBTN = view.findViewById(R.id.imgbtnrandell);
        mDialog=new Dialog(requireContext());

        popupBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.setContentView(R.layout.advisorypopuprandell_dark);
                mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                mDialog.show();
            }
        });
        popupBTN = view.findViewById(R.id.imgbtnwarren);
        mDialog=new Dialog(requireContext());

        popupBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.setContentView(R.layout.advisorypopupwarren_dark);
                mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                mDialog.show();
            }
        });
        popupBTN = view.findViewById(R.id.imgbtnpeter);
        mDialog=new Dialog(requireContext());

        popupBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.setContentView(R.layout.advisorypopuppeter_dark);
                mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                mDialog.show();
            }
        });
        popupBTN = view.findViewById(R.id.imgbtntaylor);
        mDialog=new Dialog(requireContext());

        popupBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.setContentView(R.layout.advisorypopuptaylor_dark);
                mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                mDialog.show();
            }
        });
    }
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AdvisorpageFragmentDark() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Advisorpage.
     */
    // TODO: Rename and change types and number of parameters
    public static AdvisorpageFragmentDark newInstance(String param1, String param2) {
        AdvisorpageFragmentDark fragment = new AdvisorpageFragmentDark();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.advisorpage_dark, container, false);
    }
}