package com.mehboob.hunzabykea.ui.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

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
    private ArrayList<ExistingLocations> mDataListFiltered;

    private OnItemClickListener mListener;

    public PredefinedLocationsAdapter(Context context, ArrayList<ExistingLocations> locations) {
        this.context = context;
        this.locations = locations;
        sharedPref=new SharedPref(context);
        mDataListFiltered=new ArrayList<>();

    }

    @NonNull
    @Override
    public LocationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_pre_locations,parent,false);
        return new LocationHolder(view);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    @Override
    public void onBindViewHolder(@NonNull LocationHolder holder, int position) {

      holder.txtLocationTitle.setText(locations.get(position).getLocationTitle());
      holder.txtLocationCordinates.setText(locations.get(position).getLocation().getLatitude()+"  , "+locations.get(position).getLocation().getLongitude());


        holder.itemView.setOnClickListener(v -> {

            sharedPref.saveSearchedLocation(locations.get(position).getLocation());
            if (mListener != null) {
                mListener.onItemClick(holder.getAdapterPosition());
            }
        });

//      holder.itemView.setOnClickListener(v -> {
//          Gson gson= new Gson();
//        String locationStr=  gson.toJson(locations.get(position).getLocation());
//          sharedPref.saveSearchedLocation(locations.get(position).getLocation());
////          Intent i = new Intent(context, MapsActivity.class);
////          i.putExtra("loc",locationStr);
////          context.startActivity(i);
//         SearchLocationActivity.finish();
//
//
//
//
//      });

    }

    @Override
    public int getItemCount() {
        return locations.size();
    }


    public void filterList(ArrayList<ExistingLocations> filterlist) {
        // below line is to add our filtered
        // list in our course array list.
        mDataListFiltered = filterlist;
        // below line is to notify our adapter
        // as change in recycler view data.
        notifyDataSetChanged();
    }
//    @Override
//    public Filter getFilter() {
//        return new Filter() {
//            @Override
//            protected FilterResults performFiltering(CharSequence constraint) {
//                mDataListFiltered.clear();
//                String query = constraint.toString();
//
//                if (query.isEmpty()) {
//                    mDataListFiltered.addAll(locations);
//                } else {
//                    for (ExistingLocations data : locations) {
//       if (data.getLocationTitle().toLowerCase().contains(query)){
//           mDataListFiltered.add(data)
//       }
//
//
//                    }
//                }
//                FilterResults filterResults = new FilterResults();
//                filterResults.values = filteredList;
//                return filterResults;
//            }
//
//            @Override
//            protected void publishResults(CharSequence constraint, FilterResults results) {
//                mDataListFiltered.clear();
//                mDataListFiltered.addAll((ArrayList<String>) results.values);
//                notifyDataSetChanged();
//            }
//        };
//    }

    public class LocationHolder extends RecyclerView.ViewHolder{

private TextView txtLocationTitle,txtLocationCordinates;
        public LocationHolder(@NonNull View itemView) {
            super(itemView);

            txtLocationTitle=itemView.findViewById(R.id.txtLocationTitle);
            txtLocationCordinates=itemView.findViewById(R.id.txtLocationCordinates);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
