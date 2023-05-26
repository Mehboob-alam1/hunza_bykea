package com.mehboob.hunzabykea.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.mehboob.hunzabykea.Constants;

public class SharedPref {
    private SharedPreferences sharedPreferences;
    private static final String LATITUDE="latitude";
    private static final String LONGITUDE="longitude";


    public SharedPref(Context context){
        sharedPreferences= context.getSharedPreferences(LATITUDE,Context.MODE_PRIVATE);
        sharedPreferences= context.getSharedPreferences(LONGITUDE,Context.MODE_PRIVATE);

    }


    public void saveLatitude(String latitude)
    {
        SharedPreferences.Editor editor= sharedPreferences.edit();
        editor.putString(LATITUDE,latitude);
        editor.apply();
    }

    public String fetchLatitude()
    {
        return sharedPreferences.getString(LATITUDE, Constants.DEFAULT_LATITUDE);

    }

    public void saveLongitude(String longitude)
    {
        SharedPreferences.Editor editor= sharedPreferences.edit();
        editor.putString(LONGITUDE,longitude);
        editor.apply();
    }

    public String fetchLongitude()
    {
        return sharedPreferences.getString(LONGITUDE,Constants.DEFAULT_LONGITUDE);
    }
}
