package com.mehboob.hunzabykea.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mehboob.hunzabykea.Constants;
import com.mehboob.hunzabykea.ui.models.FareModel;
import com.mehboob.hunzabykea.ui.models.LocationModel;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class SharedPref {
    private SharedPreferences sharedPreferences;
    private static final String LATITUDE = "latitude";
    private static final String LONGITUDE = "longitude";
    private static final String SEARCH_LOCATION = "search_location";
    private static final String LOCATION = "location";
    private static final String VEHICLE = "vehicle";
    private static final String PAYMENT_METHOD="payment";
    private static final String USER_ID="uid";


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
    public void saveUserId(String userId) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_ID, userId);
        editor.apply();
    }

    public String fetchUserId() {
        return sharedPreferences.getString(USER_ID, "");

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


    public void saveLocation(LocationModel locationModel){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson= new Gson();
        editor.putString(LOCATION,gson.toJson(locationModel));
        editor.apply();

    }

    public LocationModel fetchLocation(){

        String str = sharedPreferences.getString(LOCATION, null);

        Gson gson = new Gson();
        return gson.fromJson(str, LocationModel.class);

    }

    public void saveSelectedVehicle(FareModel fareModel)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson= new Gson();
        editor.putString(VEHICLE,gson.toJson(fareModel));
        editor.apply();
    }

    public FareModel fetchSelectedVehicle(){
        String str = sharedPreferences.getString(VEHICLE, null);

        Gson gson = new Gson();
        return gson.fromJson(str, FareModel.class);
    }

    public void savePaymentMethod(String paymentMethod)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PAYMENT_METHOD, paymentMethod);
        editor.apply();
    }

    public String fetchPaymentMethod()
    {
        return sharedPreferences.getString(PAYMENT_METHOD,"Cash");
    }
}
