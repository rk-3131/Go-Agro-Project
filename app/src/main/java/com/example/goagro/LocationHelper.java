package com.example.goagro;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LocationHelper {

    public static String getCityNameFromLocation(Context context, double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());

        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);

            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                String city = address.getLocality();
                String state = address.getAdminArea();
                String country = address.getCountryName();

                // Construct the full address or just return the city name
                return city != null ? city : state;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Return a placeholder or default value if the geocoding fails
        return "Unknown City";
    }
}

