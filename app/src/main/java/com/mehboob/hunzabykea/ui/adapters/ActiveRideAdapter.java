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

import com.mehboob.hunzabykea.R;
import com.mehboob.hunzabykea.ui.models.ActiveRideModel;

import java.util.ArrayList;

public class ActiveRideAdapter extends RecyclerView.Adapter<ActiveRideAdapter.viewholder> {

    ArrayList<ActiveRideModel> list;
    Context context;


    public ActiveRideAdapter(ArrayList<ActiveRideModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activenow_sample_layout,parent,false);

        return new ActiveRideAdapter.viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {

        ActiveRideModel rideModel = list.get(position);

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
        holder.textView.setText(rideModel.getName());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class viewholder extends RecyclerView.ViewHolder {
        ImageView down,up;
        TextView textView;
        ConstraintLayout constraintLayout;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            down = itemView.findViewById(R.id.detailsShowbtn);
            up = itemView.findViewById(R.id.detailsShowbtn2);
            textView = itemView.findViewById(R.id.riderName);
            constraintLayout = itemView.findViewById(R.id.constraintLayout3);

        }
    }

}
