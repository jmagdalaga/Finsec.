package com.example.finsec_finalfinalnajud;


import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CalculatorpageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CalculatorpageFragment extends Fragment implements View.OnClickListener{
    TextView resultTv,solutionTv;
    MaterialButton buttonC,buttonBrackOpen,buttonBrackClose;
    MaterialButton buttonDivide,buttonMultiply,buttonPlus,buttonMinus,buttonEquals;
    MaterialButton button0,button1,button2,button3,button4,button5,button6,button7,button8,button9;
    MaterialButton buttonAC,buttonDot;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        resultTv = view.findViewById(R.id.result_tv);
        solutionTv = view.findViewById(R.id.solution_tv);

        assignId(view, buttonC, R.id.button_c);
        assignId(view,buttonBrackOpen,R.id.button_open_bracket);
        assignId(view,buttonBrackClose,R.id.button_close_bracket);
        assignId(view,buttonDivide,R.id.button_divide);
        assignId(view,buttonMultiply,R.id.button_multiply);
        assignId(view,buttonPlus,R.id.button_plus);
        assignId(view,buttonMinus,R.id.button_minus);
        assignId(view,buttonEquals,R.id.button_equals);
        assignId(view,button0,R.id.button_0);
        assignId(view,button1,R.id.button_1);
        assignId(view,button2,R.id.button_2);
        assignId(view,button3,R.id.button_3);
        assignId(view,button4,R.id.button_4);
        assignId(view,button5,R.id.button_5);
        assignId(view,button6,R.id.button_6);
        assignId(view,button7,R.id.button_7);
        assignId(view,button8,R.id.button_8);
        assignId(view,button9,R.id.button_9);
        assignId(view,buttonAC,R.id.button_ac);
        assignId(view,buttonDot,R.id.button_dot);

    }

    void assignId(View view, MaterialButton btn, int id) {
        btn = view.findViewById(id);
        btn.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        MaterialButton button =(MaterialButton) view;
        String buttonText = button.getText().toString();
        String dataToCalculate = solutionTv.getText().toString();

        if(buttonText.equals("AC")){
            solutionTv.setText("");
            resultTv.setText("0");
            return;
        }
        if(buttonText.equals("=")){
            solutionTv.setText(resultTv.getText());
            return;
        }
        if(buttonText.equals("C")){
            if (!dataToCalculate.isEmpty()) {
                if(dataToCalculate.length()-1 == 0) {
                    solutionTv.setText("");
                    resultTv.setText("0");
                    return;
                }
                dataToCalculate = dataToCalculate.substring(0,dataToCalculate.length()-1);
            } else {
                return;
            }
        } else {
            dataToCalculate = dataToCalculate+buttonText;
        }
        solutionTv.setText(dataToCalculate);

        String finalResult = getResult(dataToCalculate);

        if(!finalResult.equals("Err")){
            resultTv.setText(finalResult);
        }
    }

    String getResult(String data){
        try{
            Context context  = Context.enter();
            context.setOptimizationLevel(-1);
            Scriptable scriptable = context.initStandardObjects();
            String finalResult =  context.evaluateString(scriptable,data,"Javascript",1,null).toString();
            if(finalResult.endsWith(".0")){
                finalResult = finalResult.replace(".0","");
            }
            return finalResult;
        }catch (Exception e){
            return "Err";
        }
    }


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CalculatorpageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CalculatorpageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CalculatorpageFragment newInstance(String param1, String param2) {
        CalculatorpageFragment fragment = new CalculatorpageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.calculatorpage, container, false);
    }


}
