package com.mehboob.hunzabykea.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;

import com.mehboob.hunzabykea.databinding.ActivitySelectVehicleBinding;
import com.mehboob.hunzabykea.ui.models.FareModel;
import com.mehboob.hunzabykea.utils.SharedPref;

public class SelectVehicleActivity extends AppCompatActivity {
    private ActivitySelectVehicleBinding binding;
private String vehicleSelected;
private String fare;
private String nearbies;
private SharedPref sharedPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySelectVehicleBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

         sharedPref= new SharedPref(this);

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

            if (binding.radioButtonAcCar.isChecked()){
                vehicleSelected= binding.radioButtonAcCar.getText().toString();
                fare=binding.txtPriceAcCar.getText().toString();
                nearbies=binding.txtPriceAcCar.getText().toString();
            }
            if (binding.radioButtonBike.isChecked()){
                vehicleSelected=binding.radioButtonBike.getText().toString();
               fare=binding.txtPriceBike.getText().toString();
               nearbies=binding.txtPriceBike.getText().toString();
            }

            if (binding.radioButtonCar.isChecked()){
                vehicleSelected=binding.radioButtonCar.getText().toString();
                fare=binding.txtPriceCar.getText().toString();
                nearbies=binding.txtPriceCar.getText().toString();
            }

            FareModel fareModel= new FareModel(vehicleSelected,fare,nearbies);
            sharedPref.saveSelectedVehicle(fareModel);
            startActivity(new Intent(SelectVehicleActivity.this,PaymentMethodActivity.class));



        });
    }
}