package com.mehboob.hunzabykea;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconOffset;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineCap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineJoin;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineWidth;
import static com.mapbox.services.Constants.PRECISION_6;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.pm.PackageManager;

import android.graphics.Color;
import android.icu.number.Precision;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;


import com.google.android.gms.maps.GoogleMap;

import com.google.android.gms.maps.OnMapReadyCallback;


import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;

import com.mapbox.api.directions.v5.MapboxDirections;


import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;


import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.Style;

import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.utils.BitmapUtils;
import com.mapbox.services.api.directions.v5.DirectionsCriteria;

import com.mehboob.hunzabykea.databinding.ActivityMapsBinding;
import com.mehboob.hunzabykea.utils.LocationTrack;
import com.mehboob.hunzabykea.utils.SharedPref;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MapsActivity extends AppCompatActivity implements PermissionsListener, OnMapReadyCallback {


    private ActivityMapsBinding binding;
    private MapView mapView;
    private MapboxMap mapboxMap;
    private static final String SOURCE_ID = "source-id";
    private static final String LAYER_ID = "layer-id";
    private PermissionsManager permissionsManager;
    protected LocationManager locationManager;

    //

    private static final String TAG = "MapsActivity";

    private SharedPref sharedPref;
    //  Location

    private ArrayList permissionsToRequest;
    private ArrayList permissionsRejected = new ArrayList();
    private ArrayList permissions = new ArrayList();

    private final static int ALL_PERMISSIONS_RESULT = 101;
    LocationTrack locationTrack;

    private Point origin, destination;
    MarkerOptions markerOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getResources().getString(R.string.mapbox_access_token));
        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        sharedPref = new SharedPref(this);
        permissions.add(ACCESS_FINE_LOCATION);
        permissions.add(ACCESS_COARSE_LOCATION);

        permissionsToRequest = findUnAskedPermissions(permissions);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


            if (permissionsToRequest.size() > 0)
                requestPermissions((String[]) permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
        }
        binding.mapView.getMapAsync(new com.mapbox.mapboxsdk.maps.OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull com.mapbox.mapboxsdk.maps.MapboxMap mapboxMap) {
                MapsActivity.this.mapboxMap = mapboxMap;
                mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        mapboxMap.getUiSettings().setAttributionEnabled(false);
                        //     enableLocationComponent(style);


                        locationTrack = new LocationTrack(MapsActivity.this);


                        if (locationTrack.canGetLocation()) {


                            double longitude = locationTrack.getLongitude();
                            double latitude = locationTrack.getLatitude();
                            markerOptions = new MarkerOptions();
                            markerOptions.title("My location");
                            markerOptions.position(new LatLng(latitude, longitude));
                            mapboxMap.addMarker(markerOptions);

                            origin = Point.fromLngLat(longitude, latitude);
                            sharedPref.saveLatitude(String.valueOf(latitude));

                            sharedPref.saveLongitude(String.valueOf(longitude));


                            CameraPosition position = new CameraPosition.Builder()
                                    .target(new LatLng(latitude, longitude))
                                    .zoom(10)
                                    .tilt(20)
                                    .build();


                            mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position), millisecondSpeed);


                            // Toast.makeText(getApplicationContext(), "Longitude:" + Double.toString(longitude) + "\nLatitude:" + Double.toString(latitude), Toast.LENGTH_SHORT).show();
                        } else {

                            locationTrack.showSettingsAlert();
                        }


                        MapsActivity.this.mapboxMap.addOnMapClickListener(new MapboxMap.OnMapClickListener() {
                            @Override
                            public boolean onMapClick(@NonNull LatLng latLng) {
                                destination = Point.fromLngLat(latLng.getLongitude(), latLng.getLatitude());
                                getRoute(mapboxMap, origin, destination);

                                return true;
                            }


                        });

                        style.addImage("red-pin-icon-id", BitmapUtils.getBitmapFromDrawable(ContextCompat.getDrawable(MapsActivity.this, R.drawable.ic_baseline_place_24)));
                        style.addLayer(new SymbolLayer("icon-layer-id", "icon-source-id").withProperties(
                                iconImage("red-pin-icon-id"),
                                iconIgnorePlacement(true),
                                iconAllowOverlap(true),
                                iconOffset(new Float[]{0f, -0f})
                        ));
                        style.addSource(new GeoJsonSource("route-source-id"));
                        LineLayer routeLayer = new LineLayer("route-layer-id", "route-source-id");

                        routeLayer.setProperties(
                                lineCap(Property.LINE_CAP_ROUND),
                                lineJoin(Property.LINE_JOIN_ROUND),
                                lineWidth(5f),
                                lineColor(Color.parseColor("#14CA15"))
                        );
                        style.addLayer(routeLayer);

                        mapboxMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(origin.latitude(), origin.longitude()), 11.7f));
                        Point userDest = Point.fromLngLat(107.6848254, -6.9218571);
                        Point userPoint = Point.fromLngLat(105.6848254, -9.9218571);
                        getRoute(mapboxMap, userPoint, userDest);
                    }
                });
            }
        });


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

    }

    private ArrayList findUnAskedPermissions(ArrayList wanted) {
        ArrayList result = new ArrayList();

        for (Object perm : wanted) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }

        return result;
    }

    private boolean hasPermission(Object permission) {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (checkSelfPermission((String) permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    private boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
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
        locationTrack.stopListener();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {

    }

    private void enableLocationComponent(Style style) {

        // Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(this)) {

// Get an instance of the component
            LocationComponent locationComponent = mapboxMap.getLocationComponent();

// Activate with options
            locationComponent.activateLocationComponent(
                    LocationComponentActivationOptions.builder(this, style).build());

// Enable to make component visible
            if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                locationComponent.setLocationComponentEnabled(true);

// Set the component's camera mode
                locationComponent.setCameraMode(CameraMode.TRACKING);

// Set the component's render mode
                locationComponent.setRenderMode(RenderMode.COMPASS);

            } else {
                ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION}, 100);
            }

        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            mapboxMap.getStyle(new Style.OnStyleLoaded() {
                @Override
                public void onStyleLoaded(@NonNull Style style) {
                    enableLocationComponent(style);
                }
            });
        } else {
            Toast.makeText(this, R.string.user_location_permission_not_granted, Toast.LENGTH_LONG).show();
            finish();
        }
    }
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        switch (requestCode) {
//            case 100: {
//                // If request is cancelled, the result arrays are empty.
//                if (grantResults.length > 0
//                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//
//                } else {
//                    // permission denied, boo! Disable the
//                    ActivityCompat.requestPermissions(this,new String[]{ ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION},100);
//
//                    // functionality that depends on this permission.
//                }
//                return;
//            }
//            // other 'case' lines to check for other
//            // permissions this app might request
//        }
//        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
//    }


    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {

            case ALL_PERMISSIONS_RESULT:
                for (Object perms : permissionsToRequest) {
                    if (!hasPermission(perms)) {
                        permissionsRejected.add(perms);
                    }
                }

                if (permissionsRejected.size() > 0) {


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale((String) permissionsRejected.get(0))) {
                            showMessageOKCancel("These permissions are mandatory for the application. Please allow access.",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions((String[]) permissionsRejected.toArray(new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                            }
                                        }
                                    });
                            return;
                        }
                    }

                }

                break;
        }

    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(MapsActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private void getRoute(MapboxMap mapboxMap, Point origin, Point destination) {


        MapboxDirections client = MapboxDirections.builder()

                .origin(origin)
                .destination(destination)
                .overview(DirectionsCriteria.OVERVIEW_FULL)
                .profile(DirectionsCriteria.PROFILE_DRIVING)
                .accessToken(getString(R.string.mapbox_access_token))
                .build();

        client.enqueueCall(new Callback<DirectionsResponse>() {
            @Override
            public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                if (response == null) {
                    Log.d(TAG, "No routes found make sure you have correct access token");
                    return;
                } else if (response.body().routes().size() < 1) {
                    Log.d(TAG, "No routes found");
                    return;
                }

                assert response.body() != null;
                DirectionsRoute drivingRoute = response.body().routes().get(0);
                if (mapboxMap != null) {
                    mapboxMap.getStyle(new Style.OnStyleLoaded() {
                        @Override
                        public void onStyleLoaded(@NonNull Style style) {
                            GeoJsonSource routeLineSource = style.getSourceAs("route-source-id");
                            GeoJsonSource iconGeoJsonSource = style.getSourceAs("icon-source-id");

                            if (routeLineSource != null) {
                                routeLineSource.setGeoJson(LineString.fromPolyline(drivingRoute.geometry(), PRECISION_6));

                                if (iconGeoJsonSource == null) {
                                    iconGeoJsonSource = new GeoJsonSource("icon-source-id", Feature.fromGeometry(Point.fromLngLat(destination.longitude(), destination.latitude())));

                                    style.addSource(iconGeoJsonSource);

                                } else {
                                    iconGeoJsonSource.setGeoJson(Feature.fromGeometry(Point.fromLngLat(destination.longitude(), destination.latitude())));
                                }
                            }
                        }
                    });
                }

            }

            @Override
            public void onFailure(Call<DirectionsResponse> call, Throwable t) {

            }
        });
    }

}
