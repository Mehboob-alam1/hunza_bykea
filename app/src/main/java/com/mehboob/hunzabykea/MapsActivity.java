package com.mehboob.hunzabykea;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import static com.mapbox.core.constants.Constants.PRECISION_6;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.fillColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.fillOpacity;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconOffset;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineCap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineJoin;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineWidth;


import androidx.annotation.NonNull;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.graphics.Color;
import android.icu.number.Precision;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;


import com.google.android.gms.maps.GoogleMap;

import com.google.android.gms.maps.OnMapReadyCallback;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;

import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.api.directions.v5.MapboxDirections;


import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
import com.mapbox.geojson.Polygon;
import com.mapbox.mapboxsdk.Mapbox;


import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.Style;

import com.mapbox.mapboxsdk.style.layers.FillLayer;
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.utils.BitmapUtils;

import com.mehboob.hunzabykea.databinding.ActivityMapsBinding;
import com.mehboob.hunzabykea.ui.SearchActivity;
import com.mehboob.hunzabykea.ui.models.LocationModel;
import com.mehboob.hunzabykea.utils.LocationTrack;
import com.mehboob.hunzabykea.utils.SharedPref;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
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

    private String searchedLocation;

    //

    List<Polygon> serviceAreaPolygons = new ArrayList<>();


    boolean isWithinServiceArea;

    private static final String TAG = "MapsActivity";

    private SharedPref sharedPref;
    //  Location

    private ArrayList permissionsToRequest;
    private ArrayList permissionsRejected = new ArrayList();
    private ArrayList permissions = new ArrayList();

    private final static int ALL_PERMISSIONS_RESULT = 101;
    LocationTrack locationTrack;
    String uerSearchedLoc;
    private Point origin, destination;
    MarkerOptions markerOptions;
    private static final LatLngBounds RESTRICTED_BOUNDS_AREA = new LatLngBounds.Builder()
            .include(Constants.BOUND_CORNER_NW)
            .include(Constants.BOUND_CORNER_NW1)
            .include(Constants.BOUND_CORNER_NW2)
            .include(Constants.BOUND_CORNER_NW3)
            .include(Constants.BOUND_CORNER_NW4)
            .include(Constants.BOUND_CORNER_NW5)
            .include(Constants.BOUND_CORNER_NW6)
            .include(Constants.BOUND_CORNER_NW7)

            .include(Constants.BOUND_CORNER_SE)
            .build();

    private final List<List<Point>> points = new ArrayList<>();
    private final List<Point> outerPoints = new ArrayList<>();
    Gson gson;
    LocationModel locationModel;

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

        binding.btnMyLocation.setOnClickListener(v -> {
            enableLocations();

        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


            if (permissionsToRequest.size() > 0)
                requestPermissions((String[]) permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
        }
        binding.mapView.getMapAsync(mapboxMap -> {
            MapsActivity.this.mapboxMap = mapboxMap;
            mapboxMap.setStyle(Style.OUTDOORS, new Style.OnStyleLoaded() {
                @Override
                public void onStyleLoaded(@NonNull Style style) {
                    mapboxMap.getUiSettings().setAttributionEnabled(false);
                    //     enableLocationComponent(style);


                    enableLocations();


                    showBoundsArea(style);
                    MapsActivity.this.mapboxMap.addOnMapClickListener(new MapboxMap.OnMapClickListener() {
                        @Override
                        public boolean onMapClick(@NonNull LatLng latLng) {
                            destination = Point.fromLngLat(latLng.getLongitude(), latLng.getLatitude());


                            if (!RESTRICTED_BOUNDS_AREA.contains(latLng)) {
                                Toast.makeText(MapsActivity.this, "No service area", Toast.LENGTH_SHORT).show();
                            } else {
                                getRoute(mapboxMap, origin, destination);
                            }


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
                            lineWidth(3f),
                            lineColor(Color.parseColor("#14CA15"))
                    );
                    style.addLayer(routeLayer);

                    mapboxMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(origin.latitude(), origin.longitude()), 11.7f));
                    Point userDest = Point.fromLngLat(107.6848254, -6.9218571);
                    Point userPoint = Point.fromLngLat(105.6848254, -9.9218571);
                    getRoute(mapboxMap, userPoint, userDest);



                }
            });
        });

        binding.etSearchLocation.setOnClickListener(v -> {
            startActivity(new Intent(MapsActivity.this,SearchActivity.class));
        });


        binding.btnSearchLocation.setOnClickListener(v -> {
            getRoute(mapboxMap, origin, destination);
        });

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

    }

    private void enableLocations() {

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


//            CameraPosition position = new CameraPosition.Builder()
//                    .target(new LatLng(latitude, longitude))
//                    .zoom(23)
//
//                    .build();

            mapboxMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 23f));

            //    mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(position), 1000);


            //
            //
            // Toast.makeText(getApplicationContext(), "Longitude:" + Double.toString(longitude) + "\nLatitude:" + Double.toString(latitude), Toast.LENGTH_SHORT).show();
        } else {

            locationTrack.showSettingsAlert();
        }
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
        binding.etSearchLocation.setHint("Search where you go");
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
    protected void onResume() {
        super.onResume();
      //  binding.etSearchLocation.setHint("Search where you go");
        if (sharedPref.fetchSearchedLocation() != null) {
            searchedLocation = sharedPref.fetchSearchedLocation();

            Gson gson = new Gson();
            Type type = new TypeToken<LocationModel>() {
            }.getType();
            LocationModel model = gson.fromJson(searchedLocation, type);
            destination = Point.fromLngLat(Double.parseDouble(model.getLongitude()), Double.parseDouble(model.getLatitude()));

            binding.etSearchLocation.setText(model.getLatitude() + "," + model.getLongitude());

            // getRoute(map

        }
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
//        mapView.onSaveInstanceState(outState);
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

    private void showBoundsArea(@NonNull Style loadedMapStyle) {
        outerPoints.add(Point.fromLngLat(RESTRICTED_BOUNDS_AREA.getNorthWest().getLongitude(),
                RESTRICTED_BOUNDS_AREA.getNorthWest().getLatitude()));
        outerPoints.add(Point.fromLngLat(RESTRICTED_BOUNDS_AREA.getNorthEast().getLongitude(),
                RESTRICTED_BOUNDS_AREA.getNorthEast().getLatitude()));
        outerPoints.add(Point.fromLngLat(RESTRICTED_BOUNDS_AREA.getSouthEast().getLongitude(),
                RESTRICTED_BOUNDS_AREA.getSouthEast().getLatitude()));
        outerPoints.add(Point.fromLngLat(RESTRICTED_BOUNDS_AREA.getSouthWest().getLongitude(),
                RESTRICTED_BOUNDS_AREA.getSouthWest().getLatitude()));
        outerPoints.add(Point.fromLngLat(RESTRICTED_BOUNDS_AREA.getNorthWest().getLongitude(),
                RESTRICTED_BOUNDS_AREA.getNorthWest().getLatitude()));
        points.add(outerPoints);


        // serviceAreaPolygons.add(Polygon.fromLngLats(Collections.singletonList(points)));


        loadedMapStyle.addSource(new GeoJsonSource("source-id",
                Polygon.fromLngLats(points)));

        loadedMapStyle.addLayer(new FillLayer("layer-id", "source-id").withProperties(fillOpacity(.24f),
                fillColor(Color.RED)));
    }


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

                                    mapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(destination.latitude(), destination.longitude()), 14.7f));

                                } else {
                                    iconGeoJsonSource.setGeoJson(Feature.fromGeometry(Point.fromLngLat(destination.longitude(), destination.latitude())));

                                    mapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(destination.latitude(), destination.longitude()), 14.7f));
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
