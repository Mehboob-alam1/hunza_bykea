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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mehboob.hunzabykea.Constants;
import com.mehboob.hunzabykea.R;
import com.mehboob.hunzabykea.databinding.FragmentActiveNowBinding;
import com.mehboob.hunzabykea.databinding.FragmentCancelledBinding;
import com.mehboob.hunzabykea.ui.adapters.ActiveRideAdapter;
import com.mehboob.hunzabykea.ui.models.ActiveRides;
import com.mehboob.hunzabykea.utils.SharedPref;

import java.util.ArrayList;


public class CancelledFragment extends Fragment {
    private FragmentCancelledBinding binding;
    private SharedPref sharedPref;
    private ArrayList<ActiveRides> listRider;
    private ActiveRideAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCancelledBinding.inflate(inflater, container, false);

        sharedPref = new SharedPref(getContext());

        listRider = new ArrayList<>();
        fetchCancelledRide();


        return binding.getRoot();
    }


    private void fetchCancelledRide() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child(Constants.HUNZA_BYKEA).child("orders").child(sharedPref.fetchUserId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    listRider.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        ActiveRides rides = dataSnapshot.getValue(ActiveRides.class);
                        if (!rides.isStatus()) { // Check if the status is false
                            listRider.add(rides);
                        }
                    }
                    if (listRider.isEmpty()) {
                        binding.noData.getRoot().setVisibility(View.VISIBLE);
                    } else {
                        binding.noData.getRoot().setVisibility(View.GONE);
                        adapter = new ActiveRideAdapter(listRider, getContext(), "Cancelled");
                        binding.activeriderRec.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
                        binding.activeriderRec.setAdapter(adapter);
                    }
                } else {
                    binding.noData.getRoot().setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                binding.noData.getRoot().setVisibility(View.VISIBLE);
            }
        });
    }

}