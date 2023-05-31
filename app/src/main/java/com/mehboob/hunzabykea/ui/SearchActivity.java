package com.mehboob.hunzabykea.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;

import com.mehboob.hunzabykea.databinding.ActivitySearchBinding;
import com.mehboob.hunzabykea.ui.adapters.PredefinedLocationsAdapter;
import com.mehboob.hunzabykea.ui.models.ExistingLocations;
import com.mehboob.hunzabykea.ui.models.LocationModel;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
private ActivitySearchBinding binding;
private PredefinedLocationsAdapter adapter;
private ArrayList<ExistingLocations> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding=ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initViews();

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
      list.add(new ExistingLocations("BE ENERFY PETROL PUMP",new LocationModel("36.2834242132134","3241234231432141234")));
      list.add(new ExistingLocations("BE ENERFY PETROL PUMP",new LocationModel("36.2834242132134","3241234231432141234")));

      adapter.notifyDataSetChanged();

    }

    private void initViews() {

        list= new ArrayList<>();
        adapter= new PredefinedLocationsAdapter(this,list);
        binding.recyclerLocations.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerLocations.setAdapter(adapter);
    }
}