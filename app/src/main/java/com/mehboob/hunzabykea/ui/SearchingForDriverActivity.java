package com.mehboob.hunzabykea.ui;

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
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.chinalwb.slidetoconfirmlib.ISlideListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.api.directions.v5.MapboxDirections;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
import com.mapbox.geojson.Polygon;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.FillLayer;
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.utils.BitmapUtils;
import com.mehboob.hunzabykea.Constants;
import com.mehboob.hunzabykea.MapsActivity;
import com.mehboob.hunzabykea.R;
import com.mehboob.hunzabykea.databinding.ActivitySearchingForDriverBinding;
import com.mehboob.hunzabykea.ui.models.Available;
import com.mehboob.hunzabykea.ui.models.VehicleDetailsClass;
import com.mehboob.hunzabykea.utils.LocationTrack;
import com.mehboob.hunzabykea.utils.SharedPref;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchingForDriverActivity extends AppCompatActivity {
    private ActivitySearchingForDriverBinding binding;
    private MapboxMap mapboxMap;
    LocationTrack locationTrack;
    private static float ZOOM_LEVEL = 16f;
    private SharedPref sharedPref;
    double latitude, longitude;
    DirectionsRoute drivingRoute;
    LatLng nearestDriverLocation;
    MarkerOptions markerOptions;
    private String distanceTotal;
    String details;
    ProgressDialog mDialog;
    private static final String TAG = "SearchDriver";
    private BottomSheetDialog dialog;
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
    private String pushId;
    private DatabaseReference mRef;
    ArrayList<Available> availables = new ArrayList<>();
    ArrayList<VehicleDetailsClass> availableDriversWithVehicleSelected = new ArrayList<>();
    ArrayList<LatLng> nearest = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getResources().getString(R.string.mapbox_access_token));
        binding = ActivitySearchingForDriverBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        sharedPref = new SharedPref(this);
        pushId = getIntent().getStringExtra("pushId");
        mRef = FirebaseDatabase.getInstance().getReference();
        mDialog= new ProgressDialog(this);
        mDialog.setMessage("Finding nearest driver.....");
        mDialog.setCancelable(false);


        checkForAvailableDrivers();
        binding.mapView.getMapAsync(mapboxMap -> {
            SearchingForDriverActivity.this.mapboxMap = mapboxMap;
            AddMarkerToMyLocation(new LatLng(Double.parseDouble(sharedPref.fetchLatitude()),Double.parseDouble(sharedPref.fetchLongitude())));
            mapboxMap.setStyle(Style.OUTDOORS, style -> {
//                        mapboxMap.getUiSettings().setAttributionEnabled(false);
                //     enableLocationComponent(style);

                enableLocations();

//updateLocation();

                showBoundsArea(style);


                style.addImage("red-pin-icon-id", BitmapUtils.getBitmapFromDrawable(ContextCompat.getDrawable(SearchingForDriverActivity.this, R.drawable.ic_baseline_place_24)));
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


            });
        });


        binding.slideToConfirm.setSlideListener(new ISlideListener() {
            @Override
            public void onSlideStart() {

            }

            @Override

            public void onSlideMove(float percent) {

            }

            @Override
            public void onSlideCancel() {

            }

            @Override
            public void onSlideDone() {
                //   Toast.makeText(SearchingForDriverActivity.this, "juuu", Toast.LENGTH_SHORT).show();
                deleteOrder(pushId);
            }
        });



    }

    private void checkForAvailableDrivers() {
mDialog.show();
        mRef.child(Constants.HUNZA_RIDER).child("available").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap : snapshot.getChildren()) {
                    Available available = snap.getValue(Available.class);
                    if (available.isAvailable()) {

                        availables.add(available);

                        checkAvailableDriverVehicles(availables);
                        //  Toast.makeText(SearchingForDriverActivity.this, "" + available.getUserId() + " is " + available.isAvailable(), Toast.LENGTH_SHORT).show();
                    } else {
                        mDialog.dismiss();
                     //   Toast.makeText(SearchingForDriverActivity.this, " no " + available.getUserId() + " is " + available.isAvailable(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                mDialog.dismiss();
            }
        });

    }

    private void checkAvailableDriverVehicles(ArrayList<Available> list) {

        for (Available available : list) {

            mRef.child(Constants.HUNZA_RIDER).child("Vehicles").child(available.getUserId()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if (snapshot.child("vehicleType").getValue(String.class).toLowerCase().equals(sharedPref.fetchSelectedVehicle().getVehicle().toLowerCase())) {

                        VehicleDetailsClass detailsClass = snapshot.getValue(VehicleDetailsClass.class);

                        availableDriversWithVehicleSelected.add(detailsClass);

                        getNearestOne(availableDriversWithVehicleSelected);

                    } else {
                        mDialog.dismiss();
                       // Toast.makeText(SearchingForDriverActivity.this, "Not juuuuuuuuuuuuuuuuuuu", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    mDialog.dismiss();
                }
            });
        }
    }

    private void getNearestOne(ArrayList<VehicleDetailsClass> availableDriversWithVehicleSelected) {


        for (VehicleDetailsClass detailsClass : availableDriversWithVehicleSelected) {
            mRef.child(Constants.HUNZA_RIDER).child("location").child(detailsClass.getUserId()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {

                        String latitude = snapshot.child("latitude").getValue(String.class);
                        String longitude = snapshot.child("longitude").getValue(String.class);


                        nearest.add(new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude)));

                        nearestDriverLocation = findNearestLocation(nearest, Double.parseDouble(sharedPref.fetchLocation().getLatitude()), Double.parseDouble(sharedPref.fetchLocation().getLongitude()),detailsClass.getUserId());
                        Log.d("near : Locations", latitude + " " + longitude);


                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    mDialog.dismiss();

                }
            });
        }


        //  Toast.makeText(this, ""+nearestDriverLocation.getLatitude() +" " + nearestDriverLocation.getLongitude(), Toast.LENGTH_SHORT).show();
    }

    private void AddMarkerToMyLocation(LatLng position) {

        markerOptions = new MarkerOptions().setIcon(IconFactory.getInstance(this).defaultMarker());
        markerOptions.title("My location");

        markerOptions.position(position);


        mapboxMap.addMarker(markerOptions);

    }

    private void AddMarkerToDriver(LatLng position,String userId) {


getUserDetails(userId,position);


    }

    private void getUserDetails(String userId,LatLng position) {


        DatabaseReference userRef= FirebaseDatabase.getInstance().getReference();

        userRef.child(Constants.HUNZA_RIDER).child("Profiles").child(userId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                         String name=   snapshot.child("userName").getValue(String.class);
                          String address=  snapshot.child("userAddress").getValue(String.class);
                          String phoneNumber=  snapshot.child("userPhoneNumber").getValue(String.class);

                          details=name +"\n"+address+"\n"+phoneNumber;
                            markerOptions = new MarkerOptions().setIcon(IconFactory.getInstance(SearchingForDriverActivity.this).defaultMarker());
                            markerOptions.title(details);

                            markerOptions.position(position);


                            mapboxMap.addMarker(markerOptions);
                            mapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(position.getLatitude(), position.getLongitude()), ZOOM_LEVEL));

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


    }


    public void getRoute(MapboxMap mapboxMap, Point origin, Point destination) {


        MapboxDirections client = MapboxDirections.builder()

                .origin(origin)
                .destination(destination)
                .overview(DirectionsCriteria.OVERVIEW_FULL)
                .profile(DirectionsCriteria.ANNOTATION_DISTANCE)
                .accessToken(getString(R.string.mapbox_access_token))
                .build();

        Log.d(TAG,"current Location ::" +origin);
        Log.d(TAG,"driver Location ::" +destination);

        client.enqueueCall(new Callback<DirectionsResponse>() {
            @Override
            public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {


                if (response == null) {
                    Log.d(TAG, "No routes found make sure you have correct access token");
                    return;
                }
//                else {
//
//                    //TODO
////            assert response.body() != null;
//
//                    if (response.body().routes().size() < 1) {
//                        Log.d(TAG, "No routes found");
//                        return;
//                    }
//                }
                if (response.body() != null) {
                    try {
                        drivingRoute = response.body().routes().get(0);
                    } catch (IndexOutOfBoundsException e) {
                        e.printStackTrace();
                    }

                    double distance = drivingRoute.distance() / 1000;
                    distanceTotal = String.format("%2f KM", distance);
                    Toast.makeText(SearchingForDriverActivity.this, "" + distanceTotal, Toast.LENGTH_SHORT).show();
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

    public LatLng findNearestLocation(ArrayList<LatLng> locations, double myLatitude, double myLongitude,String userId) {

        LatLng nearestLocation = null;
        double shortestDistance = Double.MAX_VALUE;

        for (LatLng location : locations) {
            double distance = calculateDistance(myLatitude, myLongitude, location.getLatitude(), location.getLatitude());

            if (distance < shortestDistance) {
                shortestDistance = distance;
                nearestLocation = location;

//                        AddMarkerToMyLocation(nearestLocation);
                mDialog.dismiss();
                AddMarkerToDriver(nearestLocation,userId);
                getRoute(mapboxMap, Point.fromLngLat(Double.parseDouble(sharedPref.fetchLocation().getLatitude()), Double.parseDouble(sharedPref.fetchLocation().getLongitude())), Point.fromLngLat(nearestLocation.getLatitude(), nearestLocation.getLongitude()));

            }
            Log.d("near : Location ", "The nearest location is " + nearestLocation);
        }
        return nearestLocation;
    }

    public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int EARTH_RADIUS = 6371;// in kilometers

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS * c;

    }

    private void deleteOrder(String pushId) {


        mRef.child(Constants.HUNZA_BYKEA).child(Constants.ORDERS).child(sharedPref.fetchUserId()).child(pushId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("active").exists()) {
                    mRef.child(Constants.HUNZA_BYKEA).child(Constants.ORDERS).child(sharedPref.fetchUserId()).child(pushId).child("active").setValue(false).addOnCompleteListener(task -> {
                        if (task.isSuccessful())
                            Toast.makeText(SearchingForDriverActivity.this, "Order cancelled", Toast.LENGTH_SHORT).show();
                        finish();
                    }).addOnFailureListener(e -> {
                        Toast.makeText(SearchingForDriverActivity.this, "Cancellation not completed", Toast.LENGTH_SHORT).show();
                    });
                } else {
                    Toast.makeText(SearchingForDriverActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SearchingForDriverActivity.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
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


        loadedMapStyle.addSource(new GeoJsonSource("source-id",
                Polygon.fromLngLats(points)));

        loadedMapStyle.addLayer(new FillLayer("layer-id", "source-id").withProperties(fillOpacity(.24f),
                fillColor(Color.TRANSPARENT)));
    }

    private void enableLocations() {

        locationTrack = new LocationTrack(SearchingForDriverActivity.this);


        if (locationTrack.canGetLocation()) {


            longitude = locationTrack.getLongitude();
            latitude = locationTrack.getLatitude();


            mapboxMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), ZOOM_LEVEL));


        } else {
            SearchingForDriverActivity.this.DialogShow();
            //  locationTrack.showSettingsAlert();
        }
    }

    public void DialogShow() {

        dialog = new BottomSheetDialog(SearchingForDriverActivity.this, R.style.AppBottomSheetDialogTheme);

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
}