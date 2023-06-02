package com.mehboob.hunzabykea.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.widget.Toast;

import com.mehboob.hunzabykea.databinding.ActivitySearchBinding;
import com.mehboob.hunzabykea.ui.adapters.PredefinedLocationsAdapter;
import com.mehboob.hunzabykea.ui.models.ExistingLocations;
import com.mehboob.hunzabykea.ui.models.LocationModel;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity implements  SearchView.OnQueryTextListener{
private ActivitySearchBinding binding;
private PredefinedLocationsAdapter adapter;
private ArrayList<ExistingLocations> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding=ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initViews();
//35.92252430578264, 74.32375521127818
      list.add(new ExistingLocations("BE ENERFY PETROL PUMP",new LocationModel("36.2834242132134","3241234231432141234")));
      list.add(new ExistingLocations("Canopy nexus",new LocationModel("35.92252430578264","74.32375521127818")));
      list.add(new ExistingLocations("BE ENERFY PETROL PUMP",new LocationModel("36.2834242132134","3241234231432141234")));
      list.add(new ExistingLocations("BE ENERFY PETROL PUMP",new LocationModel("36.2834242132134","3241234231432141234")));
      list.add(new ExistingLocations("BE ENERFY PETROL PUMP",new LocationModel("36.2834242132134","3241234231432141234")));
      list.add(new ExistingLocations("BE ENERFY PETROL PUMP",new LocationModel("36.2834242132134","3241234231432141234")));
      list.add(new ExistingLocations("BE ENERFY PETROL PUMP",new LocationModel("36.2834242132134","3241234231432141234")));
      list.add(new ExistingLocations("BE ENERFY PETROL PUMP",new LocationModel("36.2834242132134","3241234231432141234")));
      list.add(new ExistingLocations("BE ENERFY PETROL PUMP",new LocationModel("36.2834242132134","3241234231432141234")));
      list.add(new ExistingLocations("BE ENERFY PETROL PUMP",new LocationModel("36.2834242132134","3241234231432141234")));
      list.add(new ExistingLocations("BE ENERFY PETROL PUMP",new LocationModel("36.2834242132134","3241234231432141234")));
      list.add(new ExistingLocations("BE ENERFY PETROL PUMP",new LocationModel("36.2834242132134","3241234231432141234")));
      list.add(new ExistingLocations("BE ENERFY PETROL PUMP",new LocationModel("36.2834242132134","3241234231432141234")));
      list.add(new ExistingLocations("BE ENERFY PETROL PUMP",new LocationModel("36.2834242132134","3241234231432141234")));
      list.add(new ExistingLocations("BE ENERFY PETROL PUMP",new LocationModel("36.2834242132134","3241234231432141234")));
      list.add(new ExistingLocations("BE ENERFY PETROL PUMP",new LocationModel("36.2834242132134","3241234231432141234")));
      list.add(new ExistingLocations("BE ENERFY PETROL PUMP",new LocationModel("36.2834242132134","3241234231432141234")));
      list.add(new ExistingLocations("BE ENERFY PETROL PUMP",new LocationModel("36.2834242132134","3241234231432141234")));
      list.add(new ExistingLocations("BE ENERFY PETROL PUMP",new LocationModel("36.2834242132134","3241234231432141234")));
      list.add(new ExistingLocations("BE ENERFY PETROL PUMP",new LocationModel("36.2834242132134","3241234231432141234")));
      list.add(new ExistingLocations("BE ENERFY PETROL PUMP",new LocationModel("36.2834242132134","3241234231432141234")));
      list.add(new ExistingLocations("BE ENERFY PETROL PUMP",new LocationModel("36.2834242132134","3241234231432141234")));
      list.add(new ExistingLocations("BE ENERFY PETROL PUMP",new LocationModel("36.2834242132134","3241234231432141234")));
      list.add(new ExistingLocations("BE ENERFY PETROL PUMP",new LocationModel("36.2834242132134","3241234231432141234")));
      list.add(new ExistingLocations("BE ENERFY PETROL PUMP",new LocationModel("36.2834242132134","3241234231432141234")));
      list.add(new ExistingLocations("BE ENERFY PETROL PUMP",new LocationModel("36.2834242132134","3241234231432141234")));
      list.add(new ExistingLocations("BE ENERFY PETROL PUMP",new LocationModel("36.2834242132134","3241234231432141234")));
      list.add(new ExistingLocations("BE ENERFY PETROL PUMP",new LocationModel("36.2834242132134","3241234231432141234")));
        adapter= new PredefinedLocationsAdapter(this,list);
        binding.recyclerLocations.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerLocations.setAdapter(adapter);


        binding.searchBar.setOnQueryTextListener(this);

        adapter.setOnItemClickListener(new PredefinedLocationsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // Code to finish the Activity
                finish();
            }
        });

    }

    private void initViews() {

        list= new ArrayList<>();



    }


    @Override
    public boolean onQueryTextSubmit(String query) {
       return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        filter(newText);
        return false;
    }
    private void filter(String text) {
        // creating a new array list to filter our data.
        ArrayList<ExistingLocations> filteredlist = new ArrayList<ExistingLocations>();

        // running a for loop to compare elements.
        for (ExistingLocations item : list) {
            // checking if the entered string matched with any item of our recycler view.
            if (item.getLocationTitle().toLowerCase().contains(text.toLowerCase())) {
                // if the item is matched we are
                // adding it to our filtered list.
                filteredlist.add(item);
            }
        }
        if (filteredlist.isEmpty()) {
            // if no item is added in filtered list we are
            // displaying a toast message as no data found.
            Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show();
        } else {
            // at last we are passing that filtered
            // list to our adapter class.
            adapter.filterList(filteredlist);
        }
    }
}