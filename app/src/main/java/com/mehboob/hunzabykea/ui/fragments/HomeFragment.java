package com.mehboob.hunzabykea.ui.fragments;

import static android.app.PendingIntent.FLAG_IMMUTABLE;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.mehboob.hunzabykea.MapsActivity;
import com.mehboob.hunzabykea.R;
import com.mehboob.hunzabykea.databinding.FragmentHomeBinding;


import com.mehboob.hunzabykea.ui.adapters.BannerAdapter;
import com.mehboob.hunzabykea.ui.models.Banner;

import java.util.ArrayList;


public class HomeFragment extends Fragment {
    private BannerAdapter bannerAdapter;
    private ArrayList<Banner> listBanner;
    private FragmentHomeBinding binding;
    private static final int PERMISSION_REQUEST_CODE = 200;
    private ConstraintLayout layout;
    private static final String TAG ="HomeFragment";
    private static final int ERROR_DIALOG_REQUEST=9001;
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        binding=FragmentHomeBinding.inflate(getLayoutInflater());
        listBanner= new ArrayList<>();
        layout=view.findViewById(R.id.layout);
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

        binding.cardRide.setOnClickListener(v -> {

//            if (checkPermission()){
//              //  Snackbar.make(layout, "Permission already granted.", Snackbar.LENGTH_LONG).show();
//                Toast.makeText(requireContext(), "Permission already granted", Toast.LENGTH_SHORT).show();
//
//            }else{
//               // Snackbar.make(layout, "Permission not granted.", Snackbar.LENGTH_LONG).show();
//                Toast.makeText(requireContext(), "Permission not granted", Toast.LENGTH_SHORT).show();
//                requestPermission();
//
//            }
            if (isServicesOk()){
                Intent i = new Intent(requireContext(), MapsActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                getContext().startActivity(i);
            }

        });

    }

    public  boolean isServicesOk(){
        Log.d(TAG,"isServicesOK: checking google services version");

        int available= GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(requireContext());
        if (available== ConnectionResult.SUCCESS){
            //everything is ok
            Log.d(TAG,"isServicesOk: Google play services is working");
            return true;
        }
        else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            //an error occured but we can resolve it
            Log.d(TAG,"isServicesOk: an error occured but we can fix it");
            Dialog dialog =GoogleApiAvailability.getInstance().getErrorDialog(requireActivity(),available,ERROR_DIALOG_REQUEST);
            dialog.show();
        }
        else{
            Toast.makeText(requireContext(), "You can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }
}