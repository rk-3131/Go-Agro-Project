package com.example.goagro;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class Converter extends AppCompatActivity {
    EditText acres;
    Button getConversion;
    TextView result;
    Spinner options;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_converter);
        acres = findViewById(R.id.areaInAcres);
        getConversion = findViewById(R.id.convertButton);
        result = findViewById(R.id.resultText);
        options = findViewById(R.id.spinner);

        // Create an ArrayAdapter using the string array resource
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.conversion_options, R.layout.custom_spinner_item);
//        android.R.layout.simple_spinner_item

// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

// Apply the adapter to the spinner
        options.setAdapter(adapter);

        options.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Handle the selected item here
                String selectedItem = parentView.getItemAtPosition(position).toString();

                // You can perform actions based on the selected item here
                if (selectedItem.equals("Acres to Hectare")) {
                    getConversion.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            double val = Double.parseDouble(acres.getText().toString());
                            double valueInHectare = val * 0.404686;
                            result.setText("Area in Hectare is " + valueInHectare);
                        }
                    });
                } else if (selectedItem.equals("Hectare to Acres")) {
                    getConversion.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            double val = Double.parseDouble(acres.getText().toString());
                            double valueInAcres = val * 2.47105;
                            result.setText("Area in Acres is " + valueInAcres);
                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing here (optional)
            }
        });
    }
}