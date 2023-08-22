package com.mehboob.hunzabykea.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mehboob.hunzabykea.R;
import com.mehboob.hunzabykea.ui.models.CompletedRides;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class CancelledAdapter extends RecyclerView.Adapter<CancelledAdapter.viewholder> {

    private ArrayList<CompletedRides> list;
    private Context context;


    public CancelledAdapter(ArrayList<CompletedRides> list, Context context) {
        this.list = list;
        this.context = context;

    }

    @NonNull
    @Override
    public CancelledAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cancelled_ride_sample, parent, false);

        return new CancelledAdapter.viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CancelledAdapter.viewholder holder, int position) {

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
        holder.textView.setText(rideModel.getRides().getDriverName());

        Glide.with(context)
                .load(rideModel.getRides().getDriverImage())
                .placeholder(R.drawable.user)
                .into(holder.riderProfileImage);

        holder.txtDistance.setText(rideModel.getRides().getTotalDistance());
        holder.txtTimeTake.setText("---");
        holder.txtFare.setText(rideModel.getRides().getFare());
        try {
            holder.txtTime.setText(convertMillisToDate(Long.parseLong(rideModel.getRides().getCurrentTime())));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        holder.txtRiderVechileDetail.setText(rideModel.getRides().getVehicleType() + " " + rideModel.getRides().getVehicleModel());
        holder.txtCurrentLocation.setText(rideModel.getRides().getUserOriginLatitude() + " " + rideModel.getRides().getUserOriginLongitude());
        holder.txtDestinationLocation.setText(rideModel.getRides().getUserDestLatitude() + " " + rideModel.getRides().getUserDestLongitude());









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
            down = itemView.findViewById(R.id.detailsShowbtnCanc);
            up = itemView.findViewById(R.id.detailsShowbtn2Canc);
            constraintLayout = itemView.findViewById(R.id.constraintLayoutCanc);
            textView = itemView.findViewById(R.id.riderNameCanc);
            riderProfileImage = itemView.findViewById(R.id.riderProfileImageCanc);
            txtDistance = itemView.findViewById(R.id.txtDistanceCanc);
            txtTimeTake = itemView.findViewById(R.id.txtTimeTakeCanc);
            txtFare = itemView.findViewById(R.id.txtFareCanc);
            txtTime = itemView.findViewById(R.id.txtTimeCanc);
            txtCurrentLocation = itemView.findViewById(R.id.txtCurrentLocationCanc);
            txtDestinationLocation = itemView.findViewById(R.id.txtDestinationLocationCanc);


            txtRiderVechileDetail = itemView.findViewById(R.id.txtRiderVechileDetailCanc);



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
