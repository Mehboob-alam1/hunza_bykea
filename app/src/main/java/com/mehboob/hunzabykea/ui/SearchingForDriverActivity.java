package com.mehboob.hunzabykea.ui;

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

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
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
import com.mehboob.hunzabykea.utils.LocationTrack;
import com.mehboob.hunzabykea.utils.SharedPref;

import java.util.ArrayList;
import java.util.List;

public class SearchingForDriverActivity extends AppCompatActivity {
    private ActivitySearchingForDriverBinding binding;
    private MapboxMap mapboxMap;
    LocationTrack locationTrack;
    private static float ZOOM_LEVEL = 16f;
    private SharedPref sharedPref;
    double latitude, longitude;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getResources().getString(R.string.mapbox_access_token));
        binding = ActivitySearchingForDriverBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        sharedPref = new SharedPref(this);
        pushId = getIntent().getStringExtra("pushId");
        mRef = FirebaseDatabase.getInstance().getReference();


        checkForAvailableDrivers();
        binding.mapView.getMapAsync(mapboxMap -> {
            SearchingForDriverActivity.this.mapboxMap = mapboxMap;
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
                Toast.makeText(SearchingForDriverActivity.this, "juuu", Toast.LENGTH_SHORT).show();
                deleteOrder(pushId);
            }
        });
    }

    private void checkForAvailableDrivers() {

        mRef.child(Constants.HUNZA_RIDER).child("available").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap : snapshot.getChildren()) {
                    Available available = snap.getValue(Available.class);
                    if (available.isAvailable()) {

                        availables.add(available);

                        checkAvailableDriverVehicles(availables);
                        Toast.makeText(SearchingForDriverActivity.this, "" + available.getUserId() + " is " + available.isAvailable(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(SearchingForDriverActivity.this, " no " + available.getUserId() + " is " + available.isAvailable(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
//        Query query = mRef.orderByChild("available").equalTo(true);
//
//        query.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                if (snapshot.exists()) {
//                    for (DataSnapshot snap : snapshot.getChildren()) {
//
//                        Available available = snap.getValue(Available.class);
//
//                        Toast.makeText(SearchingForDriverActivity.this, "" + available.getUserId(), Toast.LENGTH_SHORT).show();
//                    }
//                }else {
//                    Toast.makeText(SearchingForDriverActivity.this, "No data exists", Toast.LENGTH_SHORT).show();
//
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(SearchingForDriverActivity.this, ""+ error.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    private void checkAvailableDriverVehicles(ArrayList<Available> list) {

        for (Available available : list){

        }
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