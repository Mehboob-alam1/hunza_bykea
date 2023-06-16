package com.mehboob.hunzabykea.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.ClipData;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.mehboob.hunzabykea.databinding.ActivitySearchBinding;
import com.mehboob.hunzabykea.ui.adapters.PredefinedLocationsAdapter;
import com.mehboob.hunzabykea.ui.models.ExistingLocations;
import com.mehboob.hunzabykea.ui.models.LocationModel;
import com.mehboob.hunzabykea.utils.SharedPref;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity{
    private ActivitySearchBinding binding;
    private PredefinedLocationsAdapter adapter;
    private ArrayList<ExistingLocations> list;
    private ArrayList<ExistingLocations> filterList1;
    private SharedPref sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
   sharedPref=new SharedPref(this);
        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.searchView.clearFocus();

        initViews();
//35.92252430578264, 74.32375521127818
//

        list.add(new ExistingLocations("Gilgit", new LocationModel("36.2834242132134", "3241234231432141234")));
        list.add(new ExistingLocations("Canopy nexus", new LocationModel("35.92252430578264", "74.32375521127818")));
        list.add(new ExistingLocations("Basin", new LocationModel("36.2834242132134", "3241234231432141234")));
        list.add(new ExistingLocations("Basin upper", new LocationModel("36.2834242132134", "3241234231432141234")));
        list.add(new ExistingLocations("Basin pine", new LocationModel("36.2834242132134", "3241234231432141234")));
        list.add(new ExistingLocations("Basin upper bala", new LocationModel("36.2834242132134", "3241234231432141234")));
        list.add(new ExistingLocations("jutyal", new LocationModel("36.2834242132134", "3241234231432141234")));
        list.add(new ExistingLocations("jutyal upper", new LocationModel("36.2834242132134", "3241234231432141234")));
        list.add(new ExistingLocations("jutyal lower", new LocationModel("36.2834242132134", "3241234231432141234")));
        list.add(new ExistingLocations("nli main", new LocationModel("36.2834242132134", "3241234231432141234")));
        list.add(new ExistingLocations("nli back", new LocationModel("36.2834242132134", "3241234231432141234")));
        list.add(new ExistingLocations("kiu gilgit", new LocationModel("36.2834242132134", "3241234231432141234")));
        list.add(new ExistingLocations("kiu hunza", new LocationModel("36.2834242132134", "3241234231432141234")));
        list.add(new ExistingLocations("kiu baltistan", new LocationModel("36.2834242132134", "3241234231432141234")));
        list.add(new ExistingLocations("kiu ghizer", new LocationModel("36.2834242132134", "3241234231432141234")));
        list.add(new ExistingLocations("phq gilgit", new LocationModel("36.2834242132134", "3241234231432141234")));
        list.add(new ExistingLocations("phq ghizer", new LocationModel("36.2834242132134", "3241234231432141234")));
        list.add(new ExistingLocations("phq hunza", new LocationModel("36.2834242132134", "3241234231432141234")));
        list.add(new ExistingLocations("phq baltistan", new LocationModel("36.2834242132134", "3241234231432141234")));
        list.add(new ExistingLocations("stp konodas", new LocationModel("36.2834242132134", "3241234231432141234")));
        list.add(new ExistingLocations("stp giglit", new LocationModel("36.2834242132134", "3241234231432141234")));
        list.add(new ExistingLocations("stp basin", new LocationModel("36.2834242132134", "3241234231432141234")));
        list.add(new ExistingLocations("basin khari", new LocationModel("36.2834242132134", "3241234231432141234")));
        list.add(new ExistingLocations("city hospital kashorate", new LocationModel("36.2834242132134", "3241234231432141234")));
        list.add(new ExistingLocations("city hospital sultanabad", new LocationModel("36.2834242132134", "3241234231432141234")));
        list.add(new ExistingLocations("city hospital nagar", new LocationModel("36.2834242132134", "3241234231432141234")));
        list.add(new ExistingLocations("city hospital gakuch", new LocationModel("36.2834242132134", "3241234231432141234")));
        list.add(new ExistingLocations("city hospital damas", new LocationModel("36.2834242132134", "3241234231432141234")));

//        adapter = new PredefinedLocationsAdapter(this, list);
//        binding.recyclerLocations.setLayoutManager(new LinearLayoutManager(this));
//        binding.recyclerLocations.setAdapter(adapter);



//        binding.searchView.setOnQueryTextListener(new android.widget.SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                MyFilter(newText);
//                return true;
//            }
//        });
//
//        list.add(new ExistingLocations("gilgit",new LocationModel("123.123","12312.123")));
//        list.add(new ExistingLocations("basin",new LocationModel("123.123","12312.123")));
//        list.add(new ExistingLocations("kashorate",new LocationModel("123.123","12312.123")));
//        list.add(new ExistingLocations("jutyal",new LocationModel("123.123","12312.123")));
//        list.add(new ExistingLocations("konodas",new LocationModel("123.123","12312.123")));
//        list.add(new ExistingLocations("gilgit",new LocationModel("123.123","12312.123")));
//        list.add(new ExistingLocations("gilgit",new LocationModel("123.123","12312.123")));
//        list.add(new ExistingLocations("basin",new LocationModel("123.123","12312.123")));
//        list.add(new ExistingLocations("konodas",new LocationModel("123.123","12312.123")));
//        list.add(new ExistingLocations("kashorate",new LocationModel("123.123","12312.123")));
//        list.add(new ExistingLocations("gilgit",new LocationModel("123.123","12312.123")));
        binding.recyclerLocations.setHasFixedSize(true);
        adapter = new PredefinedLocationsAdapter(this, list);
        binding.recyclerLocations.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerLocations.setAdapter(adapter);

        adapter.setOnItemClickListener(new PredefinedLocationsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {


                finish();
            }
        });


    binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            return true;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            MyFilter(newText);
            return true;
        }
    });


    }

    private void MyFilter(String Text) {
        ArrayList<ExistingLocations> arrayList = new ArrayList<>();

        for (ExistingLocations item : list) {
            if (item.getLocationTitle().toLowerCase().contains(Text.toLowerCase())) {
                arrayList.add(item);
            }
        }

        if (arrayList.isEmpty()) {
            Toast.makeText(this, "No Data Found", Toast.LENGTH_SHORT).show();
        } else {
            adapter.setFilterList(arrayList);
        }
    }
    private void filterList(String newText) {
        List<ExistingLocations> filterList   = new ArrayList<>();
        for (ExistingLocations locations : list) {
            if (locations.getLocationTitle().toLowerCase().contains(newText.toLowerCase())) {
                filterList.add(locations);
            }

            if (filterList.isEmpty())
            {
                Toast.makeText(this, "No Data Found", Toast.LENGTH_SHORT).show();
            }
            else
            {

                adapter.filterList(filterList1);
            }
        }

    }

    private void initViews() {

        list = new ArrayList<>();


    }




//    private void filter(String text) {
//        // creating a new array list to filter our data.
//        ArrayList<ExistingLocations> filteredlist = new ArrayList<ExistingLocations>();
//
//        // running a for loop to compare elements.
//        for (ExistingLocations item : list) {
//            // checking if the entered string matched with any item of our recycler view.
//            if (item.getLocationTitle().toLowerCase().contains(text.toLowerCase())) {
//                // if the item is matched we are
//                // adding it to our filtered list.
//                filteredlist.add(item);
//            }
//        }
//        if (filteredlist.isEmpty()) {
//            // if no item is added in filtered list we are
//            // displaying a toast message as no data found.
//            Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show();
//        } else {
//            // at last we are passing that filtered
//            // list to our adapter class.
//            adapter.filterList(filteredlist);
//        }
//    }
}