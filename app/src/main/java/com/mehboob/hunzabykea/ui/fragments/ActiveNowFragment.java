package com.mehboob.hunzabykea.ui.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mehboob.hunzabykea.Constants;
import com.mehboob.hunzabykea.R;
import com.mehboob.hunzabykea.databinding.FragmentActiveNowBinding;
import com.mehboob.hunzabykea.ui.adapters.ActiveRideAdapter;
import com.mehboob.hunzabykea.ui.adapters.BannerAdapter;
import com.mehboob.hunzabykea.ui.models.ActiveRideModel;
import com.mehboob.hunzabykea.ui.models.ActiveRides;
import com.mehboob.hunzabykea.ui.models.Banner;
import com.mehboob.hunzabykea.utils.SharedPref;

import java.util.ArrayList;

public class ActiveNowFragment extends Fragment {

    private FragmentActiveNowBinding binding;
    private ArrayList<ActiveRides> listRider;
    private ActiveRideAdapter adapter;

    private SharedPref sharedPref;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentActiveNowBinding.inflate(inflater, container, false);

        listRider = new ArrayList<>();
        sharedPref = new SharedPref(getContext());

        fetchActiveRide();


        return binding.getRoot();
    }


    private void fetchActiveRide() {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(Constants.USER_ACTIVE_RIDES);

        databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    ActiveRides rides = snapshot.getValue(ActiveRides.class);


                    listRider.clear();

                  if (rides.isStatus()){
                      listRider.add(rides);
                      binding.noData.getRoot().setVisibility(View.GONE);

                  }else{
                      binding.noData.getRoot().setVisibility(View.VISIBLE);
                  }
                    adapter = new ActiveRideAdapter(listRider, getContext(),"Active");
                    binding.activeriderRec.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
                    binding.activeriderRec.setAdapter(adapter);
                    adapter.notifyDataSetChanged();


                } else {
                    binding.noData.getRoot().setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                binding.noData.getRoot().setVisibility(View.VISIBLE);
                Toast.makeText(requireContext(), ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();


    if (adapter != null) {
        binding.activeriderRec.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        binding.activeriderRec.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    }
}