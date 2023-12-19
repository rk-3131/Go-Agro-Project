package com.example.goagro;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherActivity extends AppCompatActivity {

    // Replace with your OpenWeatherMap API key
    private static final String API_KEY = "ce01ebf2067c5fdeda9ec64142038e2d";
    private static final String CITY_NAME = "Mumbai"; // Replace with the desired city

    private TextView tvCityView, tvTemperatureView, tvDescriptionView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        tvCityView = findViewById(R.id.tvCity);
        tvTemperatureView = findViewById(R.id.tvTemperature);
        tvDescriptionView = findViewById(R.id.tvDescription);

        new WeatherTask().execute();
    }

    private class WeatherTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL("http://api.openweathermap.org/data/2.5/weather?q=" + CITY_NAME + "&appid=" + API_KEY);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder result = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

                return result.toString();

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                JSONObject jsonObject = new JSONObject(s);
                String cityName = jsonObject.getString("name");
                double temperatureKelvin = jsonObject.getJSONObject("main").getDouble("temp");
                String description = jsonObject.getJSONArray("weather").getJSONObject(0).getString("description");

                // Convert Kelvin to Celsius
                double temperatureCelsius = temperatureKelvin - 273.15;

                tvCityView.setText(cityName);
                tvTemperatureView.setText("Temperature: " + String.format("%.2f", temperatureCelsius) + "°C");
                tvDescriptionView.setText("Description: " + description);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
