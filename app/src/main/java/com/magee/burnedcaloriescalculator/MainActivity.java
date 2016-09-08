package com.magee.burnedcaloriescalculator;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

import java.text.NumberFormat;

public class MainActivity extends AppCompatActivity implements TextView.OnEditorActionListener,OnSeekBarChangeListener, View.OnKeyListener {

    private EditText weightEditText;

    private TextView caloriesTextView;
    private TextView bmiTextView;
    private TextView milesTextView;

    private SeekBar milesSeekBar;

    private Spinner feetSpinner;
    private Spinner inchesSpinner;

    private SharedPreferences savedValues;

    private float weight;
    private float milesRan;
    private float caloriesBurned;
    private float bmi;
    private float feet;
    private float inches;
    private float tipPercent;
    private String weightString;
    private String inchesString;
    private String feetString;
    private String milesRanString;

    ArrayAdapter<CharSequence> adapterFeet;
    ArrayAdapter<CharSequence> adapterInches;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        weightEditText = (EditText) findViewById(R.id.weightEditText);
        caloriesTextView = (TextView) findViewById(R.id.caloriesTextView);
        bmiTextView = (TextView) findViewById(R.id.bmiTextView);
        milesTextView = (TextView) findViewById(R.id.milesTextView);
        milesSeekBar = (SeekBar) findViewById(R.id.milesSeekBar);
        feetSpinner = (Spinner) findViewById(R.id.feetSpinner);
        inchesSpinner = (Spinner) findViewById(R.id.inchesSpinner);

        weightEditText.setOnEditorActionListener(this);
        milesSeekBar.setOnSeekBarChangeListener(this);
        milesSeekBar.setOnKeyListener(this);


        adapterFeet = ArrayAdapter.createFromResource(
                this, R.array.feet_array, android.R.layout.simple_spinner_item);
        adapterFeet.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        feetSpinner.setAdapter(adapterFeet);

        adapterInches = ArrayAdapter.createFromResource(
                this, R.array.inches_array, android.R.layout.simple_spinner_item);
        adapterInches.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        inchesSpinner.setAdapter(adapterInches);
        savedValues = getSharedPreferences("SavedValues", MODE_PRIVATE);
    }


    private void calculateAndDisplay() {


        feetString = feetSpinner.getSelectedItem().toString();

        inchesString = inchesSpinner.getSelectedItem().toString();
        feet = Float.parseFloat(feetString);
        inches = Float.parseFloat(inchesString);


        weightString = weightEditText.getText().toString();
        weight = Float.parseFloat(weightString);

         milesRanString = milesTextView.getText().toString();
        milesRan = Float.parseFloat(milesRanString);



        caloriesBurned = 0.75f * weight * milesRan;
        bmi = (weight * 703) / ((12 * feet + inches) * (12 * feet + inches));

        caloriesTextView.setText(String.valueOf(caloriesBurned));
        bmiTextView.setText(String.valueOf(bmi));


    }

    @Override
    public void onPause() {
        super.onPause();
        // save the instance variables
        int selectedPosition = feetSpinner.getSelectedItemPosition();
        int selectedPosition2 = inchesSpinner.getSelectedItemPosition();

        SharedPreferences.Editor editor = savedValues.edit();
        editor.putString("weightString", weightString);
        editor.putString("milesRanString", milesRanString);
        editor.putInt("spinnerSelection", selectedPosition);
        editor.putInt("spinnerSelection2", selectedPosition2);
        editor.commit();


    }

    @Override
    public void onResume() {
        super.onResume();

        feetSpinner.setSelection(savedValues.getInt("spinnerSelection",0));
        inchesSpinner.setSelection(savedValues.getInt("spinnerSelection2",0));
        weightString = savedValues.getString("weightString", "");
        milesRanString = savedValues.getString("milesRanString", "");

    }
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress,
                                  boolean fromUser) {
        milesTextView.setText(String.valueOf(progress));
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        calculateAndDisplay();
    }


        @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE ||
                actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
            calculateAndDisplay();
        }
        return false;
    }


    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        return false;
    }
}
