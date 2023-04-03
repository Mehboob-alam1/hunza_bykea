package com.mehboob.hunzabykea.ui.fragments;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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
import com.google.android.material.snackbar.Snackbar;
import com.mehboob.hunzabykea.R;
import com.mehboob.hunzabykea.databinding.FragmentHomeBinding;
import com.mehboob.hunzabykea.ui.MapsActivity;
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
                Intent i = new Intent(requireContext(),MapsActivity.class);
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