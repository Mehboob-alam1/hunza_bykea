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
import com.mehboob.hunzabykea.databinding.FragmentCancelledBinding;
import com.mehboob.hunzabykea.ui.adapters.ActiveRideAdapter;
import com.mehboob.hunzabykea.ui.adapters.CancelledAdapter;
import com.mehboob.hunzabykea.ui.adapters.CompletedAdapter;
import com.mehboob.hunzabykea.ui.models.ActiveRides;
import com.mehboob.hunzabykea.ui.models.CompletedRides;
import com.mehboob.hunzabykea.utils.SharedPref;

import java.util.ArrayList;


public class CancelledFragment extends Fragment {
    private FragmentCancelledBinding binding;
    private SharedPref sharedPref;
    private ArrayList<CompletedRides> listRider;
    private CancelledAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCancelledBinding.inflate(inflater, container, false);

        sharedPref = new SharedPref(getContext());

        listRider = new ArrayList<>();
//        fetchCancelledRide();

        fetchCancelledRide();


        return binding.getRoot();
    }


    private void fetchCancelledRide() {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(Constants.USER_CANCELLED_RIDES);

        databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    listRider.clear();
                    binding.noData.getRoot().setVisibility(View.GONE);
                    for (DataSnapshot snap : snapshot.getChildren()) {
                        CompletedRides completedRides = snap.getValue(CompletedRides.class);

                        listRider.add(completedRides);
                    }

                    adapter = new CancelledAdapter(listRider, getContext());
                    binding.CancelledRec.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
                    binding.CancelledRec.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

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

    @Override
    public void onResume() {
        super.onResume();


        if (adapter != null) {
            binding.CancelledRec.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
            binding.CancelledRec.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }

    }
}