package com.mehboob.hunzabykea.ui.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.mehboob.hunzabykea.MapsActivity;
import com.mehboob.hunzabykea.R;
import com.mehboob.hunzabykea.ui.models.ExistingLocations;
import com.mehboob.hunzabykea.ui.models.LocationModel;
import com.mehboob.hunzabykea.utils.SharedPref;

import java.util.ArrayList;

public class PredefinedLocationsAdapter extends RecyclerView.Adapter<PredefinedLocationsAdapter.LocationHolder> {
    private Context context;
    private ArrayList<ExistingLocations> locations;
    private SharedPref sharedPref;
    private Activity SearchLocationActivity;

    public PredefinedLocationsAdapter(Context context, ArrayList<ExistingLocations> locations,Activity activity) {
        this.context = context;
        this.locations = locations;
        sharedPref=new SharedPref(context);
        SearchLocationActivity=activity;
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
      holder.txtLocationCordinates.setText(locations.get(position).getLocation().getLatitude()+"  , "+locations.get(position).getLocation().getLongitude());


      holder.itemView.setOnClickListener(v -> {
          Gson gson= new Gson();
        String locationStr=  gson.toJson(locations.get(position).getLocation());
          sharedPref.saveSearchedLocation(locations.get(position).getLocation());
          Intent i = new Intent(context, MapsActivity.class);
          i.putExtra("loc",locationStr);
          context.startActivity(i);
         SearchLocationActivity.finish();




      });

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
