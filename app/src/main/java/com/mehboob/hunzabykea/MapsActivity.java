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
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.app.appsearch.SearchResult;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.icu.number.Precision;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.akexorcist.googledirection.model.Line;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;

import com.google.android.gms.maps.OnMapReadyCallback;


import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;

import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.api.directions.v5.MapboxDirections;


import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.api.geocoding.v5.GeocodingCriteria;
import com.mapbox.api.geocoding.v5.MapboxGeocoding;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.api.geocoding.v5.models.GeocodingResponse;
import com.mapbox.core.exceptions.ServicesException;
import com.mapbox.geocoder.MapboxGeocoder;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
import com.mapbox.geojson.Polygon;
import com.mapbox.mapboxsdk.Mapbox;


import com.mapbox.mapboxsdk.annotations.IconFactory;
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


import com.mapbox.mapboxsdk.offline.OfflineTilePyramidRegionDefinition;
import com.mapbox.mapboxsdk.style.layers.FillLayer;
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.utils.BitmapUtils;

import com.mehboob.hunzabykea.databinding.ActivityMapsBinding;
import com.mehboob.hunzabykea.ui.MainActivity;
import com.mehboob.hunzabykea.ui.SearchActivity;
import com.mehboob.hunzabykea.ui.SelectVehicleActivity;
import com.mehboob.hunzabykea.ui.models.LocationModel;
import com.mehboob.hunzabykea.utils.LocationService;
import com.mehboob.hunzabykea.utils.LocationTrack;
import com.mehboob.hunzabykea.utils.SharedPref;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MapsActivity extends AppCompatActivity implements PermissionsListener, OnMapReadyCallback {


    private ActivityMapsBinding binding;

    private MapboxMap mapboxMap;
    private static final String SOURCE_ID = "source-id";
    private static final String LAYER_ID = "layer-id";
    private PermissionsManager permissionsManager;
    protected LocationManager locationManager;

    private String searchedLocation;
    boolean gps_enabled = false;
    boolean network_enabled = false;
    //
    private BottomSheetDialog dialog;
    List<Polygon> serviceAreaPolygons = new ArrayList<>();

    private LocationRequest locationRequest;
    FusedLocationProviderClient fusedLocationProviderClient;
    boolean isWithinServiceArea;
    private static float ZOOM_LEVEL = 16f;

    private static final String TAG = "MapsActivity";

    private SharedPref sharedPref;
    //  Location
    private String distanceTotal;

    private ArrayList permissionsToRequest;
    private ArrayList permissionsRejected = new ArrayList();
    private ArrayList permissions = new ArrayList();

    private final static int ALL_PERMISSIONS_RESULT = 101;
    LocationTrack locationTrack;

    private Point origin, destination;
    MarkerOptions markerOptions;

    static MapsActivity instance;
// User location

    LocationModel mLocation;

    double latitude, longitude;
    String firstResultPoint;
    DirectionsRoute drivingRoute;

    public static MapsActivity getInstance() {
        return instance;
    }

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

    CarmenFeature feature;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getResources().getString(R.string.mapbox_access_token));
        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        sharedPref = new SharedPref(this);
        permissions.add(ACCESS_FINE_LOCATION);
        permissions.add(ACCESS_COARSE_LOCATION);


//        mapView.onCreate(savedInstanceState);
        permissionsToRequest = findUnAskedPermissions(permissions);

        binding.btnMyLocation.setOnClickListener(v -> {

            if (isLocationEnabled()) {


                enableLocations();
            } else {
                new AlertDialog.Builder(this)
                        .setTitle("Please activate location")
                        .setMessage("Click ok to goto settings else exit.")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                System.exit(0);
                            }
                        })
                        .show();
            }

        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


            if (permissionsToRequest.size() > 0)
                requestPermissions((String[]) permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
        }
        if (isLocationEnabled()) {
            binding.mapView.getMapAsync(new com.mapbox.mapboxsdk.maps.OnMapReadyCallback() {
                @Override
                public void onMapReady(@NonNull com.mapbox.mapboxsdk.maps.MapboxMap mapboxMap) {
                    MapsActivity.this.mapboxMap = mapboxMap;
                    mapboxMap.setStyle(Style.OUTDOORS, new Style.OnStyleLoaded() {
                        @Override
                        public void onStyleLoaded(@NonNull Style style) {
//                        mapboxMap.getUiSettings().setAttributionEnabled(false);
                            //     enableLocationComponent(style);


                            enableLocations();
//updateLocation();

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

                            mapboxMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(origin.latitude(), origin.longitude()), ZOOM_LEVEL));
                            Point userDest = Point.fromLngLat(107.6848254, -6.9218571);
                            Point userPoint = Point.fromLngLat(105.6848254, -9.9218571);
                            getRoute(mapboxMap, userPoint, userDest);


                        }
                    });
                }
            });
        } else {
            enableLocations();
        }


    }


    private void enableLocations() {

        locationTrack = new LocationTrack(MapsActivity.this);


        if (locationTrack.canGetLocation()) {


            longitude = locationTrack.getLongitude();
            latitude = locationTrack.getLatitude();


            markerOptions = new MarkerOptions().setIcon(IconFactory.getInstance(this).defaultMarker());
            markerOptions.title("My location");

            markerOptions.position(new LatLng(latitude, longitude));


            mapboxMap.addMarker(markerOptions);


            binding.txtOrigin.setText("latlng://" + latitude + " , " + longitude + "\n" + reverseGeocode(Point.fromLngLat(longitude, latitude)));
// The result of this reverse geocode will give you "Pennsylvania Ave NW"

            origin = Point.fromLngLat(longitude, latitude);
            sharedPref.saveLatitude(String.valueOf(latitude));

            sharedPref.saveLongitude(String.valueOf(longitude));


            mapboxMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), ZOOM_LEVEL));


        } else {
            MapsActivity.this.DialogShow();
            //  locationTrack.showSettingsAlert();
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

            binding.txtOrigin.setText("latlng://" + latitude + " , " + longitude + "\n" + reverseGeocode(Point.fromLngLat(longitude, latitude)));

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
                } else {
                    assert response.body() != null;
                    if (response.body().routes().size() < 1) {
                        Log.d(TAG, "No routes found");
                        return;
                    }
                }


                if (response.body() != null) {
                    try {
                        drivingRoute = response.body().routes().get(0);
                    } catch (IndexOutOfBoundsException e) {
                        e.printStackTrace();
                    }

                    double distance = drivingRoute.distance() / 1000;
                    distanceTotal = String.format("%2f KM", distance);
                    Toast.makeText(MapsActivity.this, "" + distanceTotal, Toast.LENGTH_SHORT).show();
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

                                        mapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(destination.latitude(), destination.longitude()), ZOOM_LEVEL));

                                    } else {
                                        iconGeoJsonSource.setGeoJson(Feature.fromGeometry(Point.fromLngLat(destination.longitude(), destination.latitude())));

                                        mapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(destination.latitude(), destination.longitude()), ZOOM_LEVEL));
                                    }

                                    binding.txtDestination.setText("latlng://" + destination.latitude() + " , " + destination.longitude() + "\n" + reverseGeocode(Point.fromLngLat(destination.longitude(), destination.latitude())));

                                    continueOrder("Latlng:// " + origin.latitude() + " , " + origin.longitude() + "\n" + reverseGeocode(Point.fromLngLat(longitude, latitude)), "Latlng:// " + destination.latitude() + " , " + destination.longitude() + "\n" + reverseGeocode(Point.fromLngLat(destination.longitude(), destination.latitude())), distanceTotal);
                                }
                            }
                        });
                    }
                }
            }

            @Override
            public void onFailure(Call<DirectionsResponse> call, Throwable t) {

            }
        });
    }

    private void bottomDialog(String pick, String drop) {
        BottomSheetDialog dialog = new BottomSheetDialog(MapsActivity.this, R.style.AppBottomSheetDialogTheme);
        View bottomsheetView = LayoutInflater.from(getApplicationContext()).
                inflate(R.layout.bottom_select_address, (CardView) findViewById(R.id.bottom_sheet_container));
        dialog.setContentView(bottomsheetView);

        TextView textViewPick = bottomsheetView.findViewById(R.id.txtPick);
        TextView textViewDrop = bottomsheetView.findViewById(R.id.txtDrop);

        textViewPick.setText(pick);
        textViewDrop.setText(drop);

        dialog.show();
    }

    @SuppressLint("MissingInflatedId")

    private void continueOrder(String pick, String drop, String distance) {
        BottomSheetDialog dialog = new BottomSheetDialog(MapsActivity.this, R.style.AppBottomSheetDialogTheme);
        View bottomsheetView = LayoutInflater.from(getApplicationContext()).
                inflate(R.layout.bottom_continue_order, (CardView) findViewById(R.id.bottom_continue_order));
        dialog.setContentView(bottomsheetView);


        TextView txtDistance = bottomsheetView.findViewById(R.id.txtDistance);
        TextView txtCurrentLocation = bottomsheetView.findViewById(R.id.txtCurrentLocation);
        TextView txtDropLocation = bottomsheetView.findViewById(R.id.txtDropLocation);

        CardView btnContinueOrder = bottomsheetView.findViewById(R.id.btnContinueOrder);

        txtDistance.setText(distance);
        txtCurrentLocation.setText(pick);
        txtDropLocation.setText(drop);

        dialog.show();


        btnContinueOrder.setOnClickListener(v -> {
            if (txtCurrentLocation.getText().toString().isEmpty() || txtDropLocation.getText().toString().isEmpty())
                Toast.makeText(this, "Select the desired location then try again !!", Toast.LENGTH_SHORT).show();
            else {
                dialog.dismiss();

                sharedPref.saveLocation(new LocationModel(String.valueOf(latitude), String.valueOf(longitude)));
                Intent i = new Intent(MapsActivity.this, SelectVehicleActivity.class);

                startActivity(i);

            }
        });


    }

    protected boolean isLocationEnabled() {

        LocationManager lm = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!gps_enabled && !network_enabled) {


            DialogShow();
            return false;
        } else
            return true;
    }

    public void DialogShow() {

        dialog = new BottomSheetDialog(MapsActivity.this, R.style.AppBottomSheetDialogTheme);

        View bottomsheetView = LayoutInflater.from(getApplicationContext()).
                inflate(R.layout.permission_dialog, (LinearLayout) findViewById(R.id.permissionDialog));
        dialog.setContentView(bottomsheetView);
        dialog.show();
        dialog.setCancelable(false);


        AppCompatButton btnYes = bottomsheetView.findViewById(R.id.btnEnableLocation);
        AppCompatButton btnNot = bottomsheetView.findViewById(R.id.btnNotNow);

        btnYes.setOnClickListener(v -> {
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        });
        btnNot.setOnClickListener(v -> {
            dialog.dismiss();
            finish();
        });

        dialog.show();
    }

    private PendingIntent pendingIntent() {
        Intent intent = new Intent(this, LocationService.class);
        intent.setAction(LocationService.ACTION_UPDATE_PROCESS);


        return PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_MUTABLE);

    }

    private void updateLocation() {

        buildLocationRequest();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }

        fusedLocationProviderClient.requestLocationUpdates(locationRequest, pendingIntent());


        markerOptions = new MarkerOptions().setIcon(IconFactory.getInstance(this).defaultMarker());
        markerOptions.title("My location");

        markerOptions.position(new LatLng(Double.parseDouble(mLocation.getLatitude()), Double.parseDouble(mLocation.getLongitude())));


        mapboxMap.addMarker(markerOptions);


        origin = Point.fromLngLat(Double.parseDouble(mLocation.getLatitude()), Double.parseDouble(mLocation.getLongitude()));


        mapboxMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(mLocation.getLatitude()), Double.parseDouble(mLocation.getLongitude())), 23f));

    }

    public void buildLocationRequest() {

        locationRequest = new LocationRequest();

        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setSmallestDisplacement(10f);

    }

    public void updateLocationObject(Location locationObj) {
        MapsActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mLocation = new LocationModel(String.valueOf(locationObj.getLatitude()), String.valueOf(locationObj.getLongitude()));
                //  mLocation = locationObj;

                Toast.makeText(MapsActivity.this, "" + mLocation.getLongitude() + "" + mLocation.getLatitude(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private String reverseGeocode(final Point point) {

        //run code on background thread
        try {


            MapboxGeocoding client = MapboxGeocoding.builder()
                    .accessToken(getResources().getString(R.string.mapbox_access_token))
                    .query(Point.fromLngLat(point.longitude(), point.latitude()))

                    .build();


            client.enqueueCall(new Callback<GeocodingResponse>() {
                @Override
                public void onResponse(Call<GeocodingResponse> call, Response<GeocodingResponse> response) {

                    if (response.body() != null) {
                        List<CarmenFeature> results = response.body().features();


                        if (results.size() > 0) {


                            firstResultPoint = results.get(0).text();
                            feature = results.get(0);
                            Log.d("place", firstResultPoint);


                        } else {

                        }
                    }
                }

                @Override
                public void onFailure(Call<GeocodingResponse> call, Throwable throwable) {
                    //  Timber.e("Geocoding Failure: %s", throwable.getMessage());
                    Toast.makeText(MapsActivity.this, "" + throwable.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (ServicesException servicesException) {

            servicesException.printStackTrace();


        }
        return firstResultPoint;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}



