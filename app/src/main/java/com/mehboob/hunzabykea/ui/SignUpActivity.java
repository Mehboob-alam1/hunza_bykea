package com.mehboob.hunzabykea.ui;

import static com.mehboob.hunzabykea.utils.HideKeyboard.hideKeyboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mehboob.hunzabykea.ProfileActivity;
import com.mehboob.hunzabykea.databinding.ActivitySignupBinding;
import com.mehboob.hunzabykea.utils.SharedPref;

import java.util.concurrent.TimeUnit;

import in.aabhasjindal.otptextview.OTPListener;

public class SignUpActivity extends AppCompatActivity {
    private ActivitySignupBinding binding;
    private String number, verificatonID;
//
private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private SharedPref sharedPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Sending Otp");
        progressDialog.setMessage("PLease Wait");
        progressDialog.setCancelable(false);
        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser()!=null)
        {
            startActivity(new Intent(SignUpActivity.this, ProfileActivity.class));
        }


        binding.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkValidation();
            }
        });

//
//
//
//
        binding.btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            }
        });
    }
    //
//
    public void checkValidation()
    {
        if(binding.etEmailAddress.getText().toString().isEmpty()){
            showSnackBar("Add your email address");

        }else if (binding.etPassword.getText().toString().isEmpty()) {

            showSnackBar("Set a password");
        }else{


            String email= binding.etEmailAddress.getText().toString();
            String password= binding.etPassword.getText().toString();
            hideKeyboard(this);
            createAccount(email,password);
        }
    }

    private void createAccount(String email, String password) {
        progressDialog.dismiss();

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information

                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        progressDialog.dismiss();

                        showSnackBar("User created successfully!");
                        updateUi();
                    } else {
                        // If sign in fails, display a message to the user.
                        progressDialog.dismiss();

                        showSnackBar(task.getException().toString());

                    }
                });
    }

    private void updateUi() {

        Intent i = new Intent(SignUpActivity.this,ProfileActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser()!=null){
            startActivity(new Intent(SignUpActivity.this,DashboardActivity.class));
        }
    }
    private  void showSnackBar(String message){
        Snackbar snackbar = Snackbar.make(
                findViewById(android.R.id.content),message
                ,
                Snackbar.LENGTH_SHORT
        );
        snackbar.show();
    }
}