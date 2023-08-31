package com.mehboob.hunzabykea.ui.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mehboob.hunzabykea.Constants;
import com.mehboob.hunzabykea.R;
import com.mehboob.hunzabykea.databinding.FragmentActiveNowBinding;
import com.mehboob.hunzabykea.databinding.FragmentCompletedBinding;
import com.mehboob.hunzabykea.ui.adapters.ActiveRideAdapter;
import com.mehboob.hunzabykea.ui.adapters.CompletedAdapter;
import com.mehboob.hunzabykea.ui.models.ActiveRides;
import com.mehboob.hunzabykea.ui.models.CompletedRides;
import com.mehboob.hunzabykea.utils.SharedPref;

import java.util.ArrayList;

public class CompletedFragment extends Fragment {

    private FragmentCompletedBinding binding;
    private ArrayList<CompletedRides> listRider;
    private CompletedAdapter adapter;

    private SharedPref sharedPref;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCompletedBinding.inflate(inflater, container, false);

        listRider = new ArrayList<>();
        sharedPref = new SharedPref(getContext());

        fetchCompletedRide();


        return binding.getRoot();
    }


    private void fetchCompletedRide() {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(Constants.USER_COMPLETED_RIDES);

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

                    adapter = new CompletedAdapter(listRider, getContext());
                    binding.CompletedRideRec.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
                    binding.CompletedRideRec.setAdapter(adapter);
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
            binding.CompletedRideRec.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
            binding.CompletedRideRec.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }

    }
}