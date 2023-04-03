package com.mehboob.hunzabykea.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiActivity;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.mehboob.hunzabykea.R;
import com.mehboob.hunzabykea.databinding.ActivityMapsBinding;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {
    private ActivityMapsBinding binding;
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST = 1234;
    private GoogleMap mMap;


    private Boolean mLocationPermissionGranted = false;
private  Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        dialog = new Dialog(this);

        if (!getLocationPermission())
            setDialog();
        else
            getLocationPermission();

    }

    private void setDialog() {

        dialog.setContentView(R.layout.permission_dialog);
        Button enableLocation = (Button) dialog.findViewById(R.id.btnEnableLocation);
        Button notKnow = (Button) dialog.findViewById(R.id.btnNotNow);
        dialog.show();
        enableLocation.setOnClickListener(v -> {
            getLocationPermission();
        });
        notKnow.setOnClickListener(v -> {
            dialog.dismiss();
            Toast.makeText(this, "Kindly allow the permission to use the maps", Toast.LENGTH_SHORT).show();
            finish();
        });


    }

    private void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(MapsActivity.this);
    }

    private boolean getLocationPermission() {
        String[] permission = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(this, FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this, COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionGranted = true;
            } else {
                ActivityCompat.requestPermissions(this, permission,
                        LOCATION_PERMISSION_REQUEST);
            }
        } else {
            ActivityCompat.requestPermissions(this, permission,
                    LOCATION_PERMISSION_REQUEST);
        }
        return mLocationPermissionGranted;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                            mLocationPermissionGranted = true;
                            return;
                        }
                    }
                    mLocationPermissionGranted = true;
                    //init map
                    initMap();
                }
            }
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
    }
}