package com.example.goagro;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Converter extends AppCompatActivity {
    EditText acres;
    Button getConversion;
    TextView result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_converter);
        acres = findViewById(R.id.areaInAcres);
        getConversion = findViewById(R.id.convertButton);
        result = findViewById(R.id.resultText);

        getConversion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double val = Double.parseDouble(acres.getText().toString());
                double valueInHectare = val * 0.404686;
                result.setText("Area in Hectare is " + valueInHectare);
            }
        });
    }
}