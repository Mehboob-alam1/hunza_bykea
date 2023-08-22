package com.mehboob.hunzabykea.ui.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.mehboob.hunzabykea.R;
import com.mehboob.hunzabykea.databinding.FragmentBookingBinding;
import com.mehboob.hunzabykea.ui.adapters.BookingTabsAdapter;

public class BookingFragment extends Fragment {

    FragmentBookingBinding binding;

    BookingTabsAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentBookingBinding.inflate(inflater,container,false);

        adapter = new BookingTabsAdapter(requireActivity());

        binding.tabViewpager.setAdapter(adapter);


        binding.bookingTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                binding.tabViewpager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        binding.tabViewpager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                binding.bookingTabLayout.getTabAt(position).select();


            }
        });



        return binding.getRoot();
    }
}