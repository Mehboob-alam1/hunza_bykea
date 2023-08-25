package com.mehboob.hunzabykea.ui;

import static com.mehboob.hunzabykea.utils.HideKeyboard.hideKeyboard;

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

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.mehboob.hunzabykea.MapsActivity;
import com.mehboob.hunzabykea.R;
import com.mehboob.hunzabykea.databinding.ActivityLoginBinding;
import com.mehboob.hunzabykea.utils.SharedPref;

import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private int state = 1;
    private ProgressDialog progressDialog;
    FirebaseAuth auth;
    private SharedPref sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Sending Otp");
        progressDialog.setMessage("PLease Wait");
        progressDialog.setCancelable(false);

        auth = FirebaseAuth.getInstance();
        sharedPref = new SharedPref(this);


        binding.btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkValidation();
            }
        });


        binding.btnSignUp.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this,SignUpActivity.class));
        });
    }


    private void checkValidation() {

        if (binding.etEmailAddress.getText().toString().isEmpty()) {
            showSnackBar("Enter email");


        } else if (binding.etPassword.getText().toString().isEmpty()) {

            showSnackBar("Enter password");
        } else {
            String email= binding.etEmailAddress.getText().toString();
            String password= binding.etPassword.getText().toString();
            hideKeyboard(this);
            doLogin(email,password);
        }
    }

    private void doLogin(String email, String password) {

        progressDialog.show();
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                progressDialog.dismiss();
                showSnackBar("User login successfull");
                startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
            } else {
                progressDialog.dismiss();
                showSnackBar("Something went wrong");
            }
        }).addOnFailureListener(e -> {
            progressDialog.dismiss();
            showSnackBar(e.getLocalizedMessage());
        });

    }


    @Override
    protected void onStart() {
        super.onStart();


        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this, MapsActivity.class));
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if ((progressDialog != null) && progressDialog.isShowing())
            progressDialog.dismiss();
        progressDialog = null;
    }

    private void showSnackBar(String message) {
        Snackbar snackbar = Snackbar.make(
                findViewById(android.R.id.content), message
                ,
                Snackbar.LENGTH_SHORT
        );
        snackbar.show();
    }
}