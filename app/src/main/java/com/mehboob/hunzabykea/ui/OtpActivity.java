package com.mehboob.hunzabykea.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;

import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import com.mehboob.hunzabykea.ProfileActivity;
import com.mehboob.hunzabykea.databinding.ActivityOtpBinding;

import java.util.concurrent.TimeUnit;

import in.aabhasjindal.otptextview.OTPListener;

public class OtpActivity extends AppCompatActivity {
    private ActivityOtpBinding binding;
    private String number, verificatonID;
//
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOtpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        number = (getIntent().getStringExtra("number"));
        verificatonID = getIntent().getStringExtra("verificationID");


        binding.txtNumber.setText(number);
        countDownTimer();
        OtpListner();
        binding.txtSentAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OtpAgain();
                OtpListner();

                countDownTimer();
            }
        });

    }


    private void countDownTimer() {
        new CountDownTimer(60000, 1000) {

            @Override
            public void onTick(long l) {
                binding.txtTimer.setText(String.valueOf(l / 1000));

            }

            @Override
            public void onFinish() {
                binding.txtSentAgain.setVisibility(View.VISIBLE);

            }
        }.start();
    }

    private void OtpListner() {
        binding.otpView.setOtpListener(new OTPListener() {
            @Override
            public void onInteractionListener() {


            }

            @Override
            public void onOTPComplete(String otp) {

                if (verificatonID != null) {
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificatonID, otp);
                    FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                Intent proIntent = new Intent(OtpActivity.this,ProfileActivity.class);
                                proIntent.putExtra("pronumber",number);
                                startActivity(proIntent);
//                                startActivity(new Intent(OtpActivity.this, DashboardActivity.class));
                                finish();

                            } else {
                                binding.txtInvalidOtp.setVisibility(View.VISIBLE);
                                Toast.makeText(OtpActivity.this, "verification code invalid", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(OtpActivity.this, "Exception" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();


                        }
                    });
                }
            }


        });


    }

    private void OtpAgain() {

        PhoneAuthProvider.getInstance().verifyPhoneNumber(number
                , 60l
                , TimeUnit.SECONDS
                , OtpActivity.this
                , new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {

                    }

                    @Override
                    public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {


                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                        binding.otpView.requestFocusOTP();
                        startActivity(new Intent(OtpActivity.this, DashboardActivity.class));
                    }
                });
    }
}