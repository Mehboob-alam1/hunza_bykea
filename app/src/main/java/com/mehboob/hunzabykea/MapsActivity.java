package com.mehboob.hunzabykea;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;

import android.content.pm.PackageManager;

import android.location.Location;

import android.os.Bundle;

import com.google.android.gms.maps.GoogleMap;

import com.google.android.gms.maps.OnMapReadyCallback;


import com.google.android.gms.maps.model.LatLng;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.mapboxsdk.Mapbox;

import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;

import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.Style;

import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mehboob.hunzabykea.databinding.ActivityMapsBinding;

import java.util.List;



public class MapsActivity extends AppCompatActivity implements PermissionsListener, OnMapReadyCallback {


    private ActivityMapsBinding binding;
    private MapView mapView;
    private MapboxMap mapboxMap;
    private static final String SOURCE_ID = "source-id";
    private static final String LAYER_ID = "layer-id";
    private PermissionsManager permissionsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getResources().getString(R.string.mapbox_access_token));
        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.mapView.getMapAsync(new com.mapbox.mapboxsdk.maps.OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull com.mapbox.mapboxsdk.maps.MapboxMap mapboxMap) {
                MapsActivity.this.mapboxMap = mapboxMap;
                mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        mapboxMap.getUiSettings().setAttributionEnabled(false);
                        enableLocationComponent(style);
                    }
                });
            }
        });


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

    }


    @SuppressLint("Lifecycle")
    @Override
    protected void onStart() {
        super.onStart();
        binding.mapView.onStart();
    }

    @SuppressLint("Lifecycle")
    @Override
    protected void onStop() {
        super.onStop();
        binding.mapView.onStop();
    }

    @SuppressLint("Lifecycle")
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        binding.mapView.onLowMemory();
    }

    @SuppressLint("Lifecycle")
    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding.mapView.onDestroy();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {

    }

    private void enableLocationComponent(Style style) {
        // Check if location permissions are granted
        if (PermissionsManager.areLocationPermissionsGranted(this)) {
            mapboxMap.getLocationComponent().activateLocationComponent(this, mapboxMap.getStyle());
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            mapboxMap.getLocationComponent().setLocationComponentEnabled(true);

            // Set the camera position to the user's current location
            Location lastLocation = mapboxMap.getLocationComponent().getLastKnownLocation();
            if (lastLocation != null) {
                LatLng latLng = new LatLng(lastLocation.getLatitude(),lastLocation.getLongitude());
                com.mapbox.mapboxsdk.geometry.LatLng latLng1= new com.mapbox.mapboxsdk.geometry.LatLng(lastLocation.getLatitude(),lastLocation.getLongitude());
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(latLng1)
                        .zoom(14)
                        .build();
                mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
//                mapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom( (LatLng) new LatLng(lastLocation.getLatitude(),lastLocation.getLongitude()),12));
            }

            // Add a marker at the user's current location
            style.addSource(new GeoJsonSource(SOURCE_ID));
            style.addLayer(new SymbolLayer(LAYER_ID, SOURCE_ID)
                    .withProperties(
                            PropertyFactory.iconImage("your-icon-image"),
                            PropertyFactory.iconSize(1.0f),
                            PropertyFactory.iconOffset(new Float[] {0f, -16f})
                    ));
        } else {
            // Request location permissions
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }
    @Override
    public void onPermissionResult(boolean granted) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}
