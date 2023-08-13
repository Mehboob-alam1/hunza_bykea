package com.mehboob.hunzabykea.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mehboob.hunzabykea.R;
import com.mehboob.hunzabykea.ui.models.ActiveRideModel;
import com.mehboob.hunzabykea.ui.models.ActiveRides;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ActiveRideAdapter extends RecyclerView.Adapter<ActiveRideAdapter.viewholder> {

  private   ArrayList<ActiveRides> list;
  private   Context context;
    private String status;


    public ActiveRideAdapter(ArrayList<ActiveRides> list, Context context,String status) {
        this.list = list;
        this.context = context;
        this.status=status;
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


        if (status.equals("Active")){
            holder.txtStatus.setText("Active");
            holder.btnOrderRider.setText("Mark Order as Complete");
        }else if (status.equals("Complete")){
            holder.txtStatus.setText("Complete");
            holder.btnOrderRider.setVisibility(View.GONE);
        }else if (status.equals("Cancelled")){
            holder.txtStatus.setText("Cancelled");
            holder.btnOrderRider.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class viewholder extends RecyclerView.ViewHolder {
       private ImageView down, up, riderProfileImage;
       private TextView textView, txtDistance, txtTimeTake, txtFare, txtTime, txtCurrentLocation, txtDestinationLocation,txtStatus,txtRiderVechileDetail;
     private    ConstraintLayout constraintLayout;
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
