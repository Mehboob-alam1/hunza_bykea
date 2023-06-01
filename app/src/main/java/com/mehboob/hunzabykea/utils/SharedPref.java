package com.mehboob.hunzabykea.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.mehboob.hunzabykea.Constants;
import com.mehboob.hunzabykea.ui.models.LocationModel;

public class SharedPref {
    private SharedPreferences sharedPreferences;
    private static final String LATITUDE = "latitude";
    private static final String LONGITUDE = "longitude";
    private static final String SEARCH_LOCATION = "search_location";


    public SharedPref(Context context) {
        sharedPreferences = context.getSharedPreferences(LATITUDE, Context.MODE_PRIVATE);
        sharedPreferences = context.getSharedPreferences(LONGITUDE, Context.MODE_PRIVATE);

        sharedPreferences = context.getSharedPreferences(SEARCH_LOCATION, Context.MODE_PRIVATE);

    }

    public void saveSearchedLocation(LocationModel locationModel) {

        Gson gson = new Gson();
        String str = gson.toJson(locationModel);

        SharedPreferences.Editor editor= sharedPreferences.edit();
        editor.putString(SEARCH_LOCATION,str);
        editor.apply();

    }


     public String fetchSearchedLocation()
     {
         return sharedPreferences.getString(SEARCH_LOCATION,null);
     }
    public void saveLatitude(String latitude) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(LATITUDE, latitude);
        editor.apply();
    }

    public String fetchLatitude() {
        return sharedPreferences.getString(LATITUDE, Constants.DEFAULT_LATITUDE);

    }

    public void saveLongitude(String longitude) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(LONGITUDE, longitude);
        editor.apply();
    }

    public String fetchLongitude() {
        return sharedPreferences.getString(LONGITUDE, Constants.DEFAULT_LONGITUDE);
    }
}
