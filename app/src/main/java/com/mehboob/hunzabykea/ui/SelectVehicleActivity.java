package com.mehboob.hunzabykea.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.mehboob.hunzabykea.databinding.ActivitySelectVehicleBinding;

public class SelectVehicleActivity extends AppCompatActivity {
    private ActivitySelectVehicleBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySelectVehicleBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.btnBack.setOnClickListener(v -> {
            finish();
        });


        checkBoxStates();
    }

    private void checkBoxStates() {

        binding.radioButtonBike.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                binding.radioButtonCar.setChecked(false);
                binding.radioButtonAcCar.setChecked(false);
            }

        });

        binding.radioButtonCar.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                binding.radioButtonBike.setChecked(false);
                binding.radioButtonAcCar.setChecked(false);
            }
        });


        binding.radioButtonAcCar.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                binding.radioButtonBike.setChecked(false);
                binding.radioButtonCar.setChecked(false);
            }
        });



        binding.btnContinueOrder.setOnClickListener(v -> {


        });
    }
}