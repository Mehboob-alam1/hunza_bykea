package com.mehboob.hunzabykea;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mehboob.hunzabykea.databinding.ActivityDriverDetailsBinding;
import com.mehboob.hunzabykea.ui.models.ActiveOrders;
import com.mehboob.hunzabykea.ui.models.VehicleDetailsClass;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class DriverDetailsActivity extends AppCompatActivity {
    private ActivityDriverDetailsBinding binding;

    private String image,vehicleData;
    private String driverData;
    private ActiveOrders activeOrders;
    private VehicleDetailsClass vehicleDetailsClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDriverDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        image = getIntent().getStringExtra("img");
        driverData = getIntent().getStringExtra("driver");
        vehicleData = getIntent().getStringExtra("vehicle");

        Glide.with(this)
                .load(Uri.parse(image))
                .into(binding.userImage);


        Gson gson = new Gson();
        Type type = new TypeToken<ActiveOrders>() {
        }.getType();
        activeOrders = gson.fromJson(driverData, type);

        Type typeD = new TypeToken<VehicleDetailsClass>() {
        }.getType();
vehicleDetailsClass=gson.fromJson(vehicleData,typeD);

        binding.btnBack.setOnClickListener(v -> {
            onBackPressed();
        });
   binding.txtName.setText(activeOrders.getDriverName());
   binding.txtNumber.setText(activeOrders.getDriverPhoneNumber());
   binding.txtVehicleModel.setText(vehicleDetailsClass.getVehicleModel());
   binding.txtPlateNumber.setText("-----");

       binding.btnCall.setOnClickListener(v -> {
            onCallBtnClick(activeOrders.getDriverPhoneNumber());
        });

       binding.btnChat.setOnClickListener(v -> {
            sendSMS(activeOrders.getDriverPhoneNumber());
        });

    }
    private void onCallBtnClick(String phonenumber) {
        if (Build.VERSION.SDK_INT < 23) {
            phoneCall(phonenumber);
        } else {

            if (ActivityCompat.checkSelfPermission(this,
                    android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {

                phoneCall(phonenumber);
            } else {
                final String[] PERMISSIONS_STORAGE = {Manifest.permission.CALL_PHONE};
                //Asking request Permissions
                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, 9);
            }
        }
    }


    private void phoneCall(String phoneNumber) {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + phoneNumber));
            this.startActivity(callIntent);
        } else {
            Toast.makeText(this, "You don't assign permission.", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendSMS(String phoneNumber) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) // At least KitKat
        {
            String defaultSmsPackageName = Telephony.Sms.getDefaultSmsPackage(this); // Need to change the build to API 19

            Intent sendIntent = new Intent(Intent.ACTION_SEND);
            sendIntent.setType("text/plain");
            sendIntent.putExtra(Intent.EXTRA_TEXT, "text");

            if (defaultSmsPackageName != null)// Can be null in case that there is no default, then the user would be able to choose
            // any app that support this intent.
            {
                sendIntent.setPackage(defaultSmsPackageName);
            }
            startActivity(sendIntent);

        } else // For early versions, do what worked for you before.
        {
            Intent smsIntent = new Intent(android.content.Intent.ACTION_VIEW);
            smsIntent.setType("vnd.android-dir/mms-sms");
            smsIntent.putExtra("address", phoneNumber);
            smsIntent.putExtra("sms_body", "message");
            startActivity(smsIntent);
        }
    }
}