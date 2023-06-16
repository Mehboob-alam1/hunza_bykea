package com.mehboob.hunzabykea.ui.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mehboob.hunzabykea.R;
import com.mehboob.hunzabykea.databinding.FragmentActiveNowBinding;
import com.mehboob.hunzabykea.ui.adapters.ActiveRideAdapter;
import com.mehboob.hunzabykea.ui.adapters.BannerAdapter;
import com.mehboob.hunzabykea.ui.models.ActiveRideModel;
import com.mehboob.hunzabykea.ui.models.Banner;

import java.util.ArrayList;

public class ActiveNowFragment extends Fragment {

    FragmentActiveNowBinding binding;
    ArrayList<ActiveRideModel> listRider;
    ActiveRideAdapter adapter;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentActiveNowBinding.inflate(inflater,container,false);

        listRider= new ArrayList<>();


        listRider.add(new ActiveRideModel("Khan1"));
        listRider.add(new ActiveRideModel("Khan2"));
        listRider.add(new ActiveRideModel("Khan3"));
        listRider.add(new ActiveRideModel("Khan4"));



        adapter= new ActiveRideAdapter(listRider,getContext());
        binding.activeriderRec.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL,false));
        binding.activeriderRec.setAdapter(adapter);


        return binding.getRoot();
    }
}