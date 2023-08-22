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
import static com.mehboob.hunzabykea.Constants.TOPIC;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.provider.Telephony;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.chinalwb.slidetoconfirmlib.ISlideListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
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
import com.mehboob.hunzabykea.DriverDetailsActivity;
import com.mehboob.hunzabykea.MapsActivity;
import com.mehboob.hunzabykea.R;
import com.mehboob.hunzabykea.databinding.ActivitySearchingForDriverBinding;
import com.mehboob.hunzabykea.ui.models.ActiveOrders;
import com.mehboob.hunzabykea.ui.models.ActiveRides;
import com.mehboob.hunzabykea.ui.models.Available;
import com.mehboob.hunzabykea.ui.models.CompletedRides;
import com.mehboob.hunzabykea.ui.models.FareModel;
import com.mehboob.hunzabykea.ui.models.LocationModel;
import com.mehboob.hunzabykea.ui.models.NotifFirebase;
import com.mehboob.hunzabykea.ui.models.NotificationData;
import com.mehboob.hunzabykea.ui.models.OrderPlace;
import com.mehboob.hunzabykea.ui.models.UserProfileInfo;
import com.mehboob.hunzabykea.ui.models.VehicleDetailsClass;
import com.mehboob.hunzabykea.utils.LocationTrack;
import com.mehboob.hunzabykea.utils.SharedPref;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
    UserProfileInfo userInfo;
    ProgressDialog mDialog;
    ActiveOrders orders;
    private String driverImage;
    VehicleDetailsClass data;
    BottomSheetDialog bottomSheetDialog;
    private static final String TAG = "SearchDriver";
    private ActiveRides activeRides;
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
    double distance;
    private final List<List<Point>> points = new ArrayList<>();
    private final List<Point> outerPoints = new ArrayList<>();
    private String pushId;
    private DatabaseReference mRef;
    ArrayList<Available> availables = new ArrayList<>();
    ArrayList<VehicleDetailsClass> availableDriversWithVehicleSelected = new ArrayList<>();
    ArrayList<LatLng> nearest = new ArrayList<>();

    String DriverToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getResources().getString(R.string.mapbox_access_token));
        binding = ActivitySearchingForDriverBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        sharedPref = new SharedPref(this);
        pushId = getIntent().getStringExtra("pushId");
        mRef = FirebaseDatabase.getInstance().getReference();
        mDialog = new ProgressDialog(this);
        mDialog.setMessage("Finding nearest driver.....");
        mDialog.setCancelable(false);

        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC);
       // checkForAvailableDrivers();

        findNearestAvailableDriver();

        binding.mapView.getMapAsync(mapboxMap -> {
            SearchingForDriverActivity.this.mapboxMap = mapboxMap;
            AddMarkerToMyLocation(new LatLng(Double.parseDouble(sharedPref.fetchLatitude()), Double.parseDouble(sharedPref.fetchLongitude())));
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

        binding.arrow.setOnClickListener(v -> {
            
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
                deactivateOrder(activeRides);
            }
        });


    }


    private void findNearestAvailableDriver() {
        mDialog.show();

        mRef.child(Constants.HUNZA_RIDER).child("available").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Available> availables = new ArrayList<>();

                for (DataSnapshot snap : snapshot.getChildren()) {
                    Available available = snap.getValue(Available.class);
                    if (available.isAvailable()) {
                        availables.add(available);
                    }
                }

                checkAndFilterDrivers(availables);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                mDialog.dismiss();
                Toast.makeText(SearchingForDriverActivity.this, "No available vehicles: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkAndFilterDrivers(ArrayList<Available> availables) {
        ArrayList<VehicleDetailsClass> availableDriversWithVehicleSelected = new ArrayList<>();

        for (Available available : availables) {
            mRef.child(Constants.HUNZA_RIDER).child("Vehicles").child(available.getUserId()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String vehicleType = snapshot.child("vehicleType").getValue(String.class);
                        if (vehicleType != null && vehicleType.equalsIgnoreCase(sharedPref.fetchSelectedVehicle().getVehicle())) {
                            VehicleDetailsClass detailsClass = snapshot.getValue(VehicleDetailsClass.class);
                            availableDriversWithVehicleSelected.add(detailsClass);
                        }
                    }

                    if (available == availables.get(availables.size() - 1)) {
                        getNearestDriverLocation(availableDriversWithVehicleSelected);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    mDialog.dismiss();
                    Toast.makeText(SearchingForDriverActivity.this, "Error checking vehicles: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void getNearestDriverLocation(ArrayList<VehicleDetailsClass> availableDrivers) {
        ArrayList<LatLng> driverLocations = new ArrayList<>();
        ArrayList<String> driverUserIds = new ArrayList<>();

        for (VehicleDetailsClass detailsClass : availableDrivers) {
            mRef.child(Constants.HUNZA_RIDER).child("location").child(detailsClass.getUserId()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String latitude = snapshot.child("latitude").getValue(String.class);
                        String longitude = snapshot.child("longitude").getValue(String.class);
                        if (latitude != null && longitude != null) {
                            driverLocations.add(new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude)));
                            driverUserIds.add(detailsClass.getUserId());
                        }
                    }

                    if (detailsClass == availableDrivers.get(availableDrivers.size() - 1)) {
                        findNearestLocation(driverLocations, driverUserIds);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    mDialog.dismiss();
                    Toast.makeText(SearchingForDriverActivity.this, "Error fetching driver locations: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    private void findNearestLocation(ArrayList<LatLng> driverLocations, ArrayList<String> driverUserIds) {
        if (!driverLocations.isEmpty()) {
            double userLatitude = Double.parseDouble(sharedPref.fetchLocation().getLatitude());
            double userLongitude = Double.parseDouble(sharedPref.fetchLocation().getLongitude());
            LatLng nearestDriverLocation = null;
            double nearestDistance = Double.MAX_VALUE;
            String nearestDriverUserId = null;

            for (int i = 0; i < driverLocations.size(); i++) {
                LatLng driverLocation = driverLocations.get(i);
                double distance = calculateDistance(userLatitude, userLongitude, driverLocation.getLatitude(), driverLocation.getLongitude());

                if (distance < nearestDistance) {
                    nearestDistance = distance;
                    nearestDriverLocation = driverLocation;
                    nearestDriverUserId = driverUserIds.get(i);
                }
            }

            if (nearestDriverLocation != null && nearestDriverUserId != null) {
                // Use nearestDriverUserId and nearestDriverLocation
                AddMarkerToDriver(nearestDriverLocation, nearestDriverUserId, nearestDistance);
                getRoute(mapboxMap, Point.fromLngLat(userLongitude, userLatitude), Point.fromLngLat(nearestDriverLocation.getLongitude(), nearestDriverLocation.getLatitude()));
            } else {
                mDialog.dismiss();
                Toast.makeText(SearchingForDriverActivity.this, "No near drivers found.", Toast.LENGTH_SHORT).show();
            }
        } else {
            mDialog.dismiss();
            Toast.makeText(SearchingForDriverActivity.this, "No near drivers found.", Toast.LENGTH_SHORT).show();
        }
    }




    private void AddMarkerToMyLocation(LatLng position) {

        markerOptions = new MarkerOptions().setIcon(IconFactory.getInstance(this).defaultMarker());
        markerOptions.title("My location");

        markerOptions.position(position);


        mapboxMap.addMarker(markerOptions);

    }

    private void AddMarkerToDriver(LatLng position, String userId, double distance) {


        getUserDetails(userId, position, distance);


    }

    private void getUserDetails(String userId, LatLng position, double distance) {


        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference();

        userRef.child(Constants.HUNZA_RIDER).child("Profiles").child(userId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {

                            String name = snapshot.child("userName").getValue(String.class);
                            String address = snapshot.child("userAddress").getValue(String.class);
                            String phoneNumber = snapshot.child("userPhoneNumber").getValue(String.class);
                            String driverUserId = snapshot.child("userId").getValue(String.class);

                            details = name + "\n" + address + "\n" + phoneNumber;
                            markerOptions = new MarkerOptions().setIcon(IconFactory.getInstance(SearchingForDriverActivity.this).defaultMarker());
                            markerOptions.title(details);

                            markerOptions.position(position);


                            mapboxMap.addMarker(markerOptions);
                            mapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(position.getLatitude(), position.getLongitude()), ZOOM_LEVEL));
                            getImage(driverUserId);

                            addActiveRidesToUser(data, name, address, phoneNumber, driverUserId, sharedPref.fetchSelectedVehicle(), distanceTotal, String.valueOf(System.currentTimeMillis()), sharedPref.fetchPaymentMethod(), new LocationModel(sharedPref.fetchLatitude(), sharedPref.fetchLongitude()), new LocationModel(String.valueOf(position.getLatitude()), String.valueOf(position.getLongitude())));
                            // addActiveRidesToUser(FirebaseAuth.getInstance().getCurrentUser().getUid(), driverUserId, name, address, phoneNumber);

                        } else {
                            mDialog.dismiss();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        mDialog.dismiss();
                    }
                });


    }

    private void addActiveRidesToUser(VehicleDetailsClass data, String driverName, String driverAddress, String driverPhone, String driverUserId, FareModel fareModel, String distanceTotal, String currentTime, String fetchPaymentMethod, LocationModel origin, LocationModel destination) {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child(Constants.HUNZA_BYKEA).child("UserInfo").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {

                    userInfo = snapshot.getValue(UserProfileInfo.class);

                    activeRides = new ActiveRides("", "", "", ""
                            , driverUserId, driverName, driverAddress, driverPhone, userInfo.getName(), userInfo.getEmail(), userInfo.getPhone(),
                            fareModel.getVehicle(), fareModel.getFare(), fareModel.getNearBy(),
                            distanceTotal, currentTime, fetchPaymentMethod, origin.getLatitude(), origin.getLongitude(), destination.getLatitude(), destination.getLongitude(),
                            FirebaseAuth.getInstance().getCurrentUser().getUid(), true, "");

//                    uploadToCloud(activeRides);
                    databaseReference.child(Constants.USER_ACTIVE_RIDES).child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(activeRides)
                            .addOnCompleteListener(task -> {
                                if (task.isComplete() && task.isSuccessful()) {
                                    addActiveRidesToDriver(activeRides);
                                } else {

                                    Snackbar snackbar = Snackbar.make(
                                            findViewById(android.R.id.content),
                                            "Failed to add ride",
                                            Snackbar.LENGTH_SHORT
                                    );
                                    snackbar.show();
                                    mDialog.dismiss();

                                }
                            }).addOnFailureListener(e -> {
                                Snackbar snackbar = Snackbar.make(
                                        findViewById(android.R.id.content),
                                        "" + e.getLocalizedMessage(),
                                        Snackbar.LENGTH_SHORT
                                );
                                snackbar.show();
                                mDialog.dismiss();
                            });
                } else {
                    mDialog.dismiss();
                    Snackbar snackbar = Snackbar.make(
                            findViewById(android.R.id.content),
                            "Something went wrong",
                            Snackbar.LENGTH_SHORT
                    );
                    snackbar.show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                mDialog.dismiss();
                Snackbar snackbar = Snackbar.make(
                        findViewById(android.R.id.content),
                        "" + error.getMessage(),
                        Snackbar.LENGTH_SHORT
                );
                snackbar.show();
            }
        });

    }


    private void addActiveRidesToDriver(ActiveRides orders) {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child(Constants.RIDER_ACTIVE_RIDES).child(orders.getDriverUserId()).setValue(orders)
                .addOnCompleteListener(task -> {
                    if (task.isComplete() && task.isSuccessful()) {
                        mDialog.dismiss();

binding.arrow.setVisibility(View.VISIBLE);
                        getToken(orders);


                    } else {
                        Snackbar snackbar = Snackbar.make(
                                findViewById(android.R.id.content),
                                "Failed to add ride",
                                Snackbar.LENGTH_SHORT
                        );
                        snackbar.show();
                    }
                }).addOnFailureListener(e -> {
                    Snackbar snackbar = Snackbar.make(
                            findViewById(android.R.id.content),
                            "" + e.getLocalizedMessage(),
                            Snackbar.LENGTH_SHORT
                    );
                    snackbar.show();
                });
    }

    private void getToken(ActiveRides order) {


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child(Constants.HUNZA_RIDER).child("Profiles").child(order.getDriverUserId())
                .child("token").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            DriverToken = snapshot.getValue(String.class);

                            onSendNotification(order.getRiderName(), "Booked your ride", DriverToken,order);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


    }

    private void uploadToCloud(ActiveRides orderPlace) {


        mRef.child(Constants.HUNZA_BYKEA).child(Constants.ORDERS).child(sharedPref.fetchUserId()).child(pushId).setValue(orderPlace).
                addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                    } else {
                        Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(e -> {
                    Toast.makeText(this, "" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    @SuppressLint("SuspiciousIndentation")
    private void showDriverDialog(ActiveRides order) {
        getVehicle(order.getDriverUserId());
         bottomSheetDialog = new BottomSheetDialog(SearchingForDriverActivity.this, R.style.AppBottomSheetDialogTheme);
        View bottomSheetView = LayoutInflater.from(getApplicationContext())
                .inflate(R.layout.driver_arriving_bottom, (LinearLayout) findViewById(R.id.driverArriving));


        bottomSheetDialog.setContentView(bottomSheetView);
        try {
            bottomSheetDialog.show();
        }
        catch (WindowManager.BadTokenException e) {
            //use a log message
        }



        TextView txtMinutesDriver = bottomSheetView.findViewById(R.id.txtHowMinutes);
        TextView txtDriverName = bottomSheetView.findViewById(R.id.txtRiderName);
        TextView txtVehicleType = bottomSheetView.findViewById(R.id.txtVehicleType);
        TextView txtRating = bottomSheetView.findViewById(R.id.txtRating);
        ImageView driverImageView = bottomSheetView.findViewById(R.id.imgRider);
        ImageView btnMessage = bottomSheetView.findViewById(R.id.btnChat);
        ImageView btnCall = bottomSheetView.findViewById(R.id.btnCall);
        LinearLayout driverDetails = bottomSheetView.findViewById(R.id.lineDriver);


        txtDriverName.setText(order.getDriverName());
        if (order != null) {
            txtVehicleType.setText(order.getVehicleColor() + " Color " + order.getVehicleType() + "\nModel " + order.getVehicleModel());
        }
        if (driverImage != null) {
            Glide.with(getApplicationContext()).load(Uri.parse(driverImage))
                    .placeholder(R.drawable.user)
                    .into(driverImageView);
        }
        btnCall.setOnClickListener(v -> {
            onCallBtnClick(order.getDriverPhoneNumber());
        });

        btnMessage.setOnClickListener(v -> {
            sendSMS(order.getDriverPhoneNumber());
        });

        driverDetails.setOnClickListener(v -> {
            bottomSheetDialog.dismiss();

            Gson gson = new Gson();

            Intent i = new Intent(SearchingForDriverActivity.this, DriverDetailsActivity.class);
            i.putExtra("driver", gson.toJson(order));
            i.putExtra("img", driverImage);
            i.putExtra("vehicle", gson.toJson(data));
            startActivity(i);

            finish();
        });
    }

    private void onSendNotification(String name, String send_you_and_interest, String token,ActiveRides order) {
        try {

            RequestQueue requestQueue = Volley.newRequestQueue(this);

            String url = "https://fcm.googleapis.com/fcm/send";
            JSONObject jsonObject = new JSONObject();

            jsonObject.put("title", name);
            jsonObject.put("body", send_you_and_interest);


            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put("notification", jsonObject);
            jsonObject1.put("to", token);


            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, jsonObject1,
                    new com.android.volley.Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                           showDriverDialog(order);
                            saveNotifData(order,new NotifFirebase(order.getUserId(),name,send_you_and_interest,String.valueOf(System.currentTimeMillis()),String.valueOf(UUID.randomUUID().toString())));
                            Log.d("Notification", "sent notification");
                        }

                    }, error -> {
     showDriverDialog(order);
                saveNotifData(order,new NotifFirebase(order.getUserId(),name,send_you_and_interest,String.valueOf(System.currentTimeMillis()),String.valueOf(UUID.randomUUID().toString())));
                        Log.d("Notification", "sent not notification");
                    }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> map = new HashMap<>();
                    String key = "key=AAAAGriD1uw:APA91bHV7PTVFTXFaCXBlgRrT8Lr8-G79rMZWb1aVDBCpphUykRKNNV73JH0nK8jEfsMqpzKRJ0rlxyS5-nAPkKHJKmoJ8wiMMElQRRM34TLJN4rv3WzmRvAtFk_J2aOsbP4f1_JEATu";
                    map.put("Content-type", "application/json");
                    map.put("Authorization", key);


                    return map;
                }
            };
            requestQueue.add(jsonObjectRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void saveNotifData(ActiveRides order,NotifFirebase notifFirebase) {


        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference();
        databaseReference.child(Constants.HUNZA_RIDER)
                .child("UserNotifications")
                .child(order.getDriverUserId())
                .setValue(notifFirebase).addOnCompleteListener(task -> {
                    if (task.isComplete() && task.isSuccessful()){

                    }
                }).addOnFailureListener(e -> {

                });
    }

    private void getImage(String driverUserId) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(Constants.HUNZA_RIDER);

        databaseReference.child("documents").child(driverUserId)

                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            driverImage = snapshot.child("ImgLink0").getValue(String.class);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


    }


    private void sendSMS(String phoneNumber) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) // At least KitKat
        {
            String defaultSmsPackageName = Telephony.Sms.getDefaultSmsPackage(this); // Need to change the build to API 19

            Intent sendIntent = new Intent(Intent.ACTION_SEND);
            sendIntent.setType("text/plain");
            sendIntent.putExtra(Intent.EXTRA_TEXT, "text");

            if (defaultSmsPackageName != null)// Can be null in case that there is no default, then the user would be able to choose
            // any app that support this intent.
            {
                sendIntent.setPackage(defaultSmsPackageName);
            }
            startActivity(sendIntent);

        } else // For early versions, do what worked for you before.
        {
            Intent smsIntent = new Intent(android.content.Intent.ACTION_VIEW);
            smsIntent.setType("vnd.android-dir/mms-sms");
            smsIntent.putExtra("address", phoneNumber);
            smsIntent.putExtra("sms_body", "message");
            startActivity(smsIntent);
        }
    }

    private void getVehicle(String driverUserId) {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child(Constants.HUNZA_RIDER).child("Vehicles").child(driverUserId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            data = snapshot.getValue(VehicleDetailsClass.class);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        // return  data.getVehicleColor() +" Color "+ data.getVehicleType() + "\nModel " + data.getVehicleModel();
    }


    public void getRoute(MapboxMap mapboxMap, Point origin, Point destination) {


        MapboxDirections client = MapboxDirections.builder()

                .origin(origin)
                .destination(destination)
                .overview(DirectionsCriteria.OVERVIEW_FULL)
                .profile(DirectionsCriteria.ANNOTATION_DISTANCE)
                .accessToken(getString(R.string.mapbox_access_token))
                .build();

        Log.d(TAG, "current Location ::" + origin);
        Log.d(TAG, "driver Location ::" + destination);

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

    private void deactivateOrder(ActiveRides rideModel) {







            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            String pushId=UUID.randomUUID().toString();


            CompletedRides completedRides = new CompletedRides(rideModel,pushId);

            databaseReference.child(Constants.USER_CANCELLED_RIDES).child(rideModel.getUserId()).child(pushId)
                    .setValue(completedRides).addOnCompleteListener(task -> {
                        if (task.isSuccessful()){
                            databaseReference.child(Constants.USER_ACTIVE_RIDES).child(rideModel.getUserId()).removeValue();

                            databaseReference.child(Constants.RIDER_CANCELLED_RIDES).child(rideModel.getDriverUserId()).child(pushId)
                                    .setValue(completedRides)
                                    .addOnCompleteListener(task1 -> {
                                        if (task1.isComplete() && task1.isSuccessful()){
                                            databaseReference.child(Constants.RIDER_ACTIVE_RIDES).child(rideModel.getDriverUserId()).removeValue();
                                            startActivity(new Intent(SearchingForDriverActivity.this, MapsActivity.class));
                                            finishAffinity();
//                                            showDialog(rideModel);
                                        }
                                    }).addOnFailureListener(e -> {
                                        Toast.makeText(this
                                                , ""+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                    });
                        }else{
                            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    });


//        mRef.child(Constants.HUNZA_BYKEA).child(Constants.ORDERS).child(sharedPref.fetchUserId()).child(pushId).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.child("status").exists()) {
//                    mRef.child(Constants.HUNZA_BYKEA).child(Constants.ORDERS).child(sharedPref.fetchUserId()).child(pushId).child("status").setValue(false).addOnCompleteListener(task -> {
//                        if (task.isSuccessful())
//
//                            deleteOrder(sharedPref.fetchUserId(), activeRides.getDriverUserId());
//
//                    }).addOnFailureListener(e -> {
//                        Toast.makeText(SearchingForDriverActivity.this, "Cancellation not completed", Toast.LENGTH_SHORT).show();
//                    });
//                } else {
//                    Toast.makeText(SearchingForDriverActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(SearchingForDriverActivity.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    private void deleteOrder(String userId, String driverUserId) {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child(Constants.USER_ACTIVE_RIDES).child(userId).removeValue().addOnCompleteListener(task -> {
            if (task.isComplete() && task.isSuccessful()) {
                databaseReference.child(Constants.RIDER_ACTIVE_RIDES).child(driverUserId).removeValue().addOnCompleteListener(task1 -> {
                    if (task1.isComplete() && task1.isSuccessful()) {
                        Toast.makeText(this, "Order cancelled", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SearchingForDriverActivity.this, MapsActivity.class));
                        finishAffinity();
                    } else {

                    }
                }).addOnFailureListener(e -> {
                    Toast.makeText(this, "" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                });
            } else {

            }
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
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


    private void onCallBtnClick(String phonenumber) {
        if (Build.VERSION.SDK_INT < 23) {
            phoneCall(phonenumber);
        } else {

            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {

                phoneCall(phonenumber);
            } else {
                final String[] PERMISSIONS_STORAGE = {Manifest.permission.CALL_PHONE};
                //Asking request Permissions
                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, 9);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean permissionGranted = false;
        switch (requestCode) {
            case 9:
                permissionGranted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (permissionGranted) {
            phoneCall(orders.getDriverPhoneNumber());
        } else {
            Toast.makeText(this, "You don't assign permission.", Toast.LENGTH_SHORT).show();
        }
    }

    private void phoneCall(String phoneNumber) {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + phoneNumber));
            this.startActivity(callIntent);
        } else {
            Toast.makeText(this, "You don't assign permission.", Toast.LENGTH_SHORT).show();
        }
    }
}