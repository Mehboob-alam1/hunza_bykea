package com.mehboob.hunzabykea.ui.adapters;

import android.content.Context;
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

import com.mehboob.hunzabykea.R;
import com.mehboob.hunzabykea.ui.models.ActiveRides;
import com.mehboob.hunzabykea.ui.models.CompletedRides;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class CompletedAdapter extends RecyclerView.Adapter<CompletedAdapter.viewholder> {

    private ArrayList<CompletedRides> list;
    private Context context;


    public CompletedAdapter(ArrayList<CompletedRides> list, Context context) {
        this.list = list;
        this.context = context;

    }

    @NonNull
    @Override
    public CompletedAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.completed_ride_sample, parent, false);

        return new CompletedAdapter.viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CompletedAdapter.viewholder holder, int position) {

        CompletedRides rideModel = list.get(position);

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









    }


    @Override
    public int getItemCount() {
        return list.size();
    }


    public class viewholder extends RecyclerView.ViewHolder {
        private ImageView down, up, riderProfileImage;
        private TextView textView, txtDistance, txtTimeTake, txtFare, txtTime, txtCurrentLocation, txtDestinationLocation, txtRiderVechileDetail;
        private ConstraintLayout constraintLayout;


        public viewholder(@NonNull View itemView) {
            super(itemView);
            down = itemView.findViewById(R.id.detailsShowbtnComp);
            up = itemView.findViewById(R.id.detailsShowbtn2Comp);
            constraintLayout = itemView.findViewById(R.id.constraintLayoutComp);
            textView = itemView.findViewById(R.id.riderNameComp);
            riderProfileImage = itemView.findViewById(R.id.riderProfileImageComp);
            txtDistance = itemView.findViewById(R.id.txtDistanceComp);
            txtTimeTake = itemView.findViewById(R.id.txtTimeTakeComp);
            txtFare = itemView.findViewById(R.id.txtFareComp);
            txtTime = itemView.findViewById(R.id.txtTimeComp);
            txtCurrentLocation = itemView.findViewById(R.id.txtCurrentLocationComp);
            txtDestinationLocation = itemView.findViewById(R.id.txtDestinationLocationComp);


            txtRiderVechileDetail = itemView.findViewById(R.id.txtRiderVechileDetailComp);



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
