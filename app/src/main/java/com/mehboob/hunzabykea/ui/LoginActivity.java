package com.mehboob.hunzabykea.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.mehboob.hunzabykea.R;
import com.mehboob.hunzabykea.databinding.ActivityLoginBinding;

import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private int state = 1;
    private ProgressDialog progressDialog;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        progressDialog = new ProgressDialog(this);

        auth = FirebaseAuth.getInstance();

//        if (auth.getCurrentUser() !=null){
//            startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
//        }


        binding.numberEditBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                changeButtonState(0);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.connectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkValidation();
            }
        });
    }


    private void checkValidation() {

        if (binding.numberEditBox.getText().toString().isEmpty() || binding.numberEditBox
                .getText().toString().trim().length() != 10) {
            Toast.makeText(this, "Enter a Valid Number ", Toast.LENGTH_SHORT).show();

            binding.errorTxt.setVisibility(View.VISIBLE);

            changeButtonState(1);
        } else {


            changeButtonState(0);

            String countryCode = binding.ccp.getSelectedCountryCode();
            String number = binding.numberEditBox.getText().toString();
            String phoneNumber = "+" + countryCode + number;
            sendOtp(phoneNumber);
        }
    }

    private void sendOtp(String phoneNumber) {
        progressDialog.setTitle("Sending Otp");
        progressDialog.setMessage("Please Wait");
        progressDialog.setCancelable(false);
        progressDialog.show();

        PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneNumber,
                60L,
                TimeUnit.SECONDS, LoginActivity.this,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {


                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {

                        Log.d("Exception", e.getMessage());
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {


                        progressDialog.dismiss();

                        Intent intent = new Intent(LoginActivity.this, OtpActivity.class);
                        intent.putExtra("verificationID", s);
                        intent.putExtra("number", phoneNumber);
                        startActivity(intent);


                    }
                });
    }

    private void changeButtonState(int state) {
        if (state == 0) {
            binding.connectBtn.setBackground(ContextCompat.getDrawable(this, R.drawable.getstart_back));
        } else {
            binding.connectBtn.setBackground(ContextCompat.getDrawable(this, R.drawable.grey_back));
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        changeButtonState(1);
    }
}