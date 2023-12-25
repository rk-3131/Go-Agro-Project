package com.example.goagro;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.annotation.SuppressLint;
import android.location.Location;
import android.os.Looper;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

public class WeatherActivity extends AppCompatActivity {

    private static final String API_KEY = "ce01ebf2067c5fdeda9ec64142038e2d";
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;

    private TextView tvCityView, tvTemperatureView, tvDescriptionView;
    private ImageView bgImage;
    String CITY_NAME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        tvCityView = findViewById(R.id.tvCity);
        tvTemperatureView = findViewById(R.id.tvTemperature);
        tvDescriptionView = findViewById(R.id.tvDescription);
        bgImage = findViewById(R.id.backgroundImageView);

        if (checkLocationPermission()) {
            // If permission is granted, get the current location and fetch weather data
            getCurrentLocationAndFetchWeather();
        } else {
            // Request location permission
            requestLocationPermission();
        }
    }

    private boolean checkLocationPermission() {
        return ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                LOCATION_PERMISSION_REQUEST_CODE
        );
    }

    private void getCurrentLocationAndFetchWeather() {
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        LocationRequest locationRequest = new LocationRequest()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10000)
                .setFastestInterval(5000);

        // Check if the required permissions are granted
        if (checkLocationPermission()) {
            // Request location updates
            fusedLocationClient.requestLocationUpdates(locationRequest, new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    super.onLocationResult(locationResult);
                    if (locationResult != null && locationResult.getLastLocation() != null) {
                        Location location = locationResult.getLastLocation();
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();

                        // Reverse geocode to get city name from latitude and longitude
                        CITY_NAME = getCityNameFromLocation(latitude, longitude);

                        // Fetch weather data
                        new WeatherTask().execute();

                        // Stop location updates as soon as the location is obtained
                        fusedLocationClient.removeLocationUpdates(this);
                    }
                }
            }, Looper.getMainLooper());
        } else {
            startActivity(new Intent(WeatherActivity.this, IntroActivity2.class));
        }
    }

    // This method converts latitude and longitude to a human-readable location name (city)
    // Inside WeatherActivity class
    private String getCityNameFromLocation(double latitude, double longitude) {
        return LocationHelper.getCityNameFromLocation(this, latitude, longitude);
    }


    // Add the following method to handle the result of the permission request
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, get the current location and fetch weather data
                getCurrentLocationAndFetchWeather();
            } else {
                // Permission denied, handle accordingly (e.g., show a message to the user)
            }
        }
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
                tvTemperatureView.setText(String.format("%.2f", temperatureCelsius) + "°C");
                tvDescriptionView.setText(description);

                // Update background image based on weather description
                updateBackgroundImage(description);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        private void updateBackgroundImage(String weatherDescription) {
            int backgroundImageResource;

            // Map weather descriptions to corresponding background images
            String desc = weatherDescription.toLowerCase();
            Log.d("WeatherActivity", "Weather description: " + desc);

            if (desc.contains("rain")){
                backgroundImageResource = R.drawable.rain_bg;
            }else if (desc.contains("snow")){
                backgroundImageResource = R.drawable.snow_bg;
            }else if (desc.contains("cloud") || desc.contains("clouds")){
                backgroundImageResource = R.drawable.clouds_bg;
            }else if (desc.contains("thunder") || desc.contains("storm") || desc.contains("thunderstorm")){
                backgroundImageResource = R.drawable.thunder_bg;
            }else if (desc.contains("clear")){
                backgroundImageResource = R.drawable.clear_sky_bg;
            }else if (desc.contains("fog") || desc.contains("fogg")){
                backgroundImageResource = R.drawable.fogg_bg;
            }else{
                backgroundImageResource = R.drawable.all_weather_bg;
            }

            bgImage.setBackgroundResource(backgroundImageResource);

//            Log.d("WeatherActivity", "Selected Resource ID: " + backgroundImageResource);
//            getWindow().setBackgroundDrawableResource(backgroundImageResource);
        }
    }
}

/*
Rain: light rain, moderate rain, heavy rain, very heavy rain, extreme rain, freezing rain, shower rain
Snow: light snow, snow showers, moderate snow, heavy snow, very heavy snow, extreme snow
Clouds: few clouds, scattered clouds, broken clouds, overcast clouds, mist
Thunderstorm: thunderstorm, thunderstorm with hail
Clear: clear sky, few clouds
Fog: fog, light fog
 */
