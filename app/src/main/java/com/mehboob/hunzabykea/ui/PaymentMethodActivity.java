package com.mehboob.hunzabykea.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mehboob.hunzabykea.Constants;
import com.mehboob.hunzabykea.databinding.ActivityPaymentMethodBinding;
import com.mehboob.hunzabykea.ui.models.OrderPlace;
import com.mehboob.hunzabykea.utils.SharedPref;

import java.util.Random;
import java.util.UUID;

public class PaymentMethodActivity extends AppCompatActivity {
private ActivityPaymentMethodBinding binding;
private SharedPref sharedPref;
 String pushId;
private DatabaseReference mRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding=ActivityPaymentMethodBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        sharedPref= new SharedPref(this);
      mRef=FirebaseDatabase.getInstance().getReference();

        binding.btnContinue.setOnClickListener(v -> {
            if (!binding.radioCashMoney.isChecked())
                Toast.makeText(this, "Choose the payment method", Toast.LENGTH_SHORT).show();
            else {
                pushId= UUID.randomUUID().toString();
                sharedPref.savePaymentMethod("Cash");
                uploadToCloud(pushId,String.valueOf(System.currentTimeMillis()));
            }
        });
    }

    private void uploadToCloud(String pushId,String time) {


        OrderPlace orderPlace = new OrderPlace(sharedPref.fetchUserId(),sharedPref.fetchLocation().getLatitude(),
                sharedPref.fetchLocation().getLongitude(),sharedPref.fetchSelectedVehicle().getVehicle(),sharedPref.fetchSelectedVehicle().getFare(),
                sharedPref.fetchSelectedVehicle().getNearBy(),sharedPref.fetchPaymentMethod(),pushId,time);
        mRef.child(Constants.HUNZA_BYKEA).child(Constants.ORDERS).child(sharedPref.fetchUserId()).child(pushId).setValue(orderPlace).
                addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        Toast.makeText(this, "Order placed successfully", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(e -> {
                    Toast.makeText(this, ""+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}