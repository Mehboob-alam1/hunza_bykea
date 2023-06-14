package com.mehboob.hunzabykea.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.widget.Toast;

import com.google.android.gms.location.LocationResult;
import com.mehboob.hunzabykea.MapsActivity;
import com.mehboob.hunzabykea.ui.models.LocationModel;

public class LocationService  extends BroadcastReceiver {

    public static final String ACTION_UPDATE_PROCESS ="com.example.qaash.UPDATE_LOCATION";
    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent!= null)
        {
            final String Action= intent.getAction();
            if (ACTION_UPDATE_PROCESS.equals(Action))
            {
                LocationResult locationResult = LocationResult.extractResult(intent);
                if (locationResult!=null)
                {
                    Location location =     locationResult.getLastLocation();



                    try {
                        MapsActivity.getInstance().updateLocationObject(location);
                    }
                    catch (Exception e )
                    {


                    }
                }
            }

        }
    }
}