package com.mehboob.hunzabykea.ui.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mehboob.hunzabykea.R;
import com.mehboob.hunzabykea.databinding.FragmentHomeBinding;
import com.mehboob.hunzabykea.ui.adapters.BannerAdapter;
import com.mehboob.hunzabykea.ui.models.Banner;

import java.util.ArrayList;


public class HomeFragment extends Fragment {
    private BannerAdapter bannerAdapter;
    private ArrayList<Banner> listBanner;
    private FragmentHomeBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        binding=FragmentHomeBinding.inflate(getLayoutInflater());
        listBanner= new ArrayList<>();
        setBanners();




        return binding.getRoot();
    }
    private void setBanners() {

        listBanner.add(new Banner(R.drawable.picture,"1"));
        listBanner.add(new Banner(R.drawable.picture,"1"));
        listBanner.add(new Banner(R.drawable.picture,"1"));

        bannerAdapter= new BannerAdapter(getContext(),listBanner);
        binding.topBannerRecycler.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL,false));
        binding.bottomBannerRecycler.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL,false));
        binding.topBannerRecycler.setAdapter(bannerAdapter);


        binding.bottomBannerRecycler.setAdapter(bannerAdapter);

    }
}