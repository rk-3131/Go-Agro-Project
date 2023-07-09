package com.example.goagro;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class WeatherActivity extends AppCompatActivity {

    EditText cName;
    EditText cCode;
    TextView weather;
    private final String apiid = "ce01ebf2067c5fdeda9ec64142038e2d";
    private final String url = "http://api.openweathermap.org/data/2.5/weather";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        cName = findViewById(R.id.cityName);
        cCode = findViewById(R.id.countryCode);
        weather = findViewById(R.id.result);
    }

    public void getWeather(View view){
        String tempUrl = "";
        String cityName = cName.getText().toString().trim();
        String countryCode = cCode.getText().toString().trim();

        if (cityName.equals("")){
            weather.setText("Please enter the city name!");
        }else{
            if (countryCode.equals("")){
                tempUrl = url + "?q=" + cityName + "&appid=" + apiid;
            }else{
                tempUrl = url + "?q=" + cityName + "," + countryCode + "&appid" + apiid;
            }
        }
    }
}