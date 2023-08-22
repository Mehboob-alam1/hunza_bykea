package com.mehboob.hunzabykea.ui.adapters;

import static com.mapbox.mapboxsdk.Mapbox.getApplicationContext;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mehboob.hunzabykea.Constants;
import com.mehboob.hunzabykea.R;
import com.mehboob.hunzabykea.ui.DashboardActivity;
import com.mehboob.hunzabykea.ui.SearchingForDriverActivity;
import com.mehboob.hunzabykea.ui.models.ActiveRideModel;
import com.mehboob.hunzabykea.ui.models.ActiveRides;
import com.mehboob.hunzabykea.ui.models.Rate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

public class ActiveRideAdapter extends RecyclerView.Adapter<ActiveRideAdapter.viewholder> {

    private ArrayList<ActiveRides> list;
    private Context context;
    private String status;
    BottomSheetDialog bottomSheetDialog;

    public ActiveRideAdapter(ArrayList<ActiveRides> list, Context context, String status) {
        this.list = list;
        this.context = context;
        this.status = status;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activenow_sample_layout, parent, false);

        return new ActiveRideAdapter.viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {

        ActiveRides rideModel = list.get(position);

        holder.down.setOnClickListener(v -> {
            holder.constraintLayout.setVisibility(View.VISIBLE);
            holder.down.setVisibility(View.GONE);
            holder.up.setVisibility(View.VISIBLE);

        });
        holder.up.setOnClickListener(v -> {
            holder.constraintLayout.setVisibility(View.GONE);
            holder.up.setVisibility(View.GONE);
            holder.down.setVisibility(View.VISIBLE);

        });
        holder.textView.setText(rideModel.getDriverName());

        Glide.with(context)
                .load(rideModel.getDriverImage())
                .placeholder(R.drawable.user)
                .into(holder.riderProfileImage);

        holder.txtDistance.setText(rideModel.getTotalDistance());
        holder.txtTimeTake.setText("---");
        holder.txtFare.setText(rideModel.getFare());
        try {
            holder.txtTime.setText(convertMillisToDate(Long.parseLong(rideModel.getCurrentTime())));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        holder.txtRiderVechileDetail.setText(rideModel.getVehicleType() + " " + rideModel.getVehicleModel());
        holder.txtCurrentLocation.setText(rideModel.getUserOriginLatitude() + " " + rideModel.getUserOriginLongitude());
        holder.txtDestinationLocation.setText(rideModel.getUserDestLatitude() + " " + rideModel.getUserDestLongitude());


        holder.btnOrderRider.setOnClickListener(v -> {

            setOrderComplete(rideModel);
        });


        if (status.equals("Active")) {
            holder.txtStatus.setText("Active");
            holder.btnOrderRider.setText("Mark Order as Complete");
        } else if (status.equals("Complete")) {
            holder.txtStatus.setText("Complete");
            holder.btnOrderRider.setVisibility(View.GONE);
        } else if (status.equals("Cancelled")) {
            holder.txtStatus.setText("Cancelled");
            holder.btnOrderRider.setVisibility(View.GONE);
        }

    }

    private void setOrderComplete(ActiveRides rideModel) {


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();


        databaseReference.child(Constants.USER_ACTIVE_RIDES).child(rideModel.getUserId())
                .child("completed").setValue(true).addOnCompleteListener(task -> {
                    if (task.isComplete() && task.isSuccessful()) {

                        databaseReference.child(Constants.USER_ACTIVE_RIDES).child(rideModel.getUserId())
                                .child("status").removeValue();
                        databaseReference.child(Constants.RIDER_ACTIVE_RIDES).child(rideModel.getDriverUserId()).child("completed")
                                .setValue(true).addOnCompleteListener(task1 -> {
                                    if (task1.isComplete() && task1.isSuccessful()) {
                                        databaseReference.child(Constants.RIDER_ACTIVE_RIDES).child(rideModel.getDriverUserId()).child("status").removeValue();
                                        Toast.makeText(context, "Order completed", Toast.LENGTH_SHORT).show();

                                        showDialog(rideModel);
                                    } else {
                                        Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(e -> {
                                    Toast.makeText(context, "" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                });

                    } else {
                        Toast.makeText(context, "Cannot add the order to complete", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(e -> {
                    Toast.makeText(context, "" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void showDialog(ActiveRides rideModel) {


        bottomSheetDialog = new BottomSheetDialog(context, R.style.AppBottomSheetDialogTheme);
        View bottomSheetView = LayoutInflater.from(context)
                .inflate(R.layout.rate_driver, (LinearLayout) bottomSheetDialog.findViewById(R.id.rateDriverLayout));

        bottomSheetDialog.setCancelable(false);

        bottomSheetDialog.setContentView(bottomSheetView);
        try {
            bottomSheetDialog.show();
        } catch (WindowManager.BadTokenException e) {
            //use a log message
        }

        ImageView driverImageView = bottomSheetView.findViewById(R.id.imgRiderRate);
        TextView txtDriverName = bottomSheetView.findViewById(R.id.txtRiderNameRate);
        TextView txtVehicleType = bottomSheetView.findViewById(R.id.txtVehicleTypeRate);
        TextView txtRating = bottomSheetView.findViewById(R.id.txtRatingRate);


        txtDriverName.setText(rideModel.getDriverName());
        txtVehicleType.setText(rideModel.getVehicleType());
        Glide.with(context)
                .load(rideModel.getDriverImage())
                .into(driverImageView);


        AppCompatButton btnRate = bottomSheetView.findViewById(R.id.btnRateDriver);

        RatingBar ratingBar = bottomSheetView.findViewById(R.id.rateBar);


        btnRate.setOnClickListener(v -> {


            if (ratingBar.getRating() <= 0.0) {
                Toast.makeText(context, "Please select a rating", Toast.LENGTH_SHORT).show();
            } else {

                rateDriver(ratingBar.getRating(), rideModel);
            }
        });
    }

    private void rateDriver(float rating, ActiveRides rides) {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
       ;

        String pushId = UUID.randomUUID().toString();
        databaseReference.child("Riders").child("Ratings").child(rides.getDriverUserId()).child(rides.getUserId()).child(pushId)
                .setValue(new Rate(String.valueOf(rating), rides.getUserId(), rides.getRiderName(), pushId))
                .addOnCompleteListener(task -> {
                    if (task.isComplete() && task.isSuccessful()) {
                        Toast.makeText(context, "Thanks for rating", Toast.LENGTH_SHORT).show();
                     bottomSheetDialog.dismiss();

                    }
                }).addOnFailureListener(e -> {
                    Toast.makeText(context, ""+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class viewholder extends RecyclerView.ViewHolder {
        private ImageView down, up, riderProfileImage;
        private TextView textView, txtDistance, txtTimeTake, txtFare, txtTime, txtCurrentLocation, txtDestinationLocation, txtStatus, txtRiderVechileDetail;
        private ConstraintLayout constraintLayout;
        private AppCompatButton btnOrderRider;

        public viewholder(@NonNull View itemView) {
            super(itemView);
            down = itemView.findViewById(R.id.detailsShowbtn);
            up = itemView.findViewById(R.id.detailsShowbtn2);
            constraintLayout = itemView.findViewById(R.id.constraintLayout3);
            textView = itemView.findViewById(R.id.riderName);
            riderProfileImage = itemView.findViewById(R.id.riderProfileImage);
            txtDistance = itemView.findViewById(R.id.txtDistance);
            txtTimeTake = itemView.findViewById(R.id.txtTimeTake);
            txtFare = itemView.findViewById(R.id.txtFare);
            txtTime = itemView.findViewById(R.id.txtTime);
            txtCurrentLocation = itemView.findViewById(R.id.txtCurrentLocation);
            txtDestinationLocation = itemView.findViewById(R.id.txtDestinationLocation);
            txtStatus = itemView.findViewById(R.id.txtStatus);
            btnOrderRider = itemView.findViewById(R.id.btnOrderRider);
            txtRiderVechileDetail = itemView.findViewById(R.id.txtRiderVechileDetail);


        }
    }

    public static String convertMillisToDate(long millis) {
        // Create a SimpleDateFormat instance with the desired date format
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

        // Convert milliseconds to a Date object
        Date date = new Date(millis);

        // Format the Date object using the SimpleDateFormat
        return sdf.format(date);
    }

}
