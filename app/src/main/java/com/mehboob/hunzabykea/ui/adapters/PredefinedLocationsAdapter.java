package com.mehboob.hunzabykea.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mehboob.hunzabykea.R;
import com.mehboob.hunzabykea.ui.models.ExistingLocations;

import java.util.ArrayList;

public class PredefinedLocationsAdapter extends RecyclerView.Adapter<PredefinedLocationsAdapter.LocationHolder> {
    private Context context;
    private ArrayList<ExistingLocations> locations;

    public PredefinedLocationsAdapter(Context context, ArrayList<ExistingLocations> locations) {
        this.context = context;
        this.locations = locations;
    }

    @NonNull
    @Override
    public LocationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_pre_locations,parent,false);
        return new LocationHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationHolder holder, int position) {

      holder.txtLocationTitle.setText(locations.get(position).getLocationTitle());
      holder.txtLocationCordinates.setText(locations.get(position).getLocation().getLatitude()+" "+locations.get(position).getLocation().getLongitude());

    }

    @Override
    public int getItemCount() {
        return locations.size();
    }

    public class LocationHolder extends RecyclerView.ViewHolder{

private TextView txtLocationTitle,txtLocationCordinates;
        public LocationHolder(@NonNull View itemView) {
            super(itemView);

            txtLocationTitle=itemView.findViewById(R.id.txtLocationTitle);
            txtLocationCordinates=itemView.findViewById(R.id.txtLocationCordinates);
        }
    }
}
