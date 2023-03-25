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

import com.mehboob.hunzabykea.R;
import com.mehboob.hunzabykea.databinding.ActivityLoginBinding;

import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private int state = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        binding.edittextNumber.addTextChangedListener(new TextWatcher() {
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
        binding.btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkValidation();
            }
        });
    }


    private void checkValidation() {

        if (binding.edittextNumber.getText().toString().isEmpty() || binding.edittextNumber
                .getText().toString().trim().length() != 11) {
            Toast.makeText(this, "Enter a Valid Number ", Toast.LENGTH_SHORT).show();

            binding.textviewError.setVisibility(View.VISIBLE);

            changeButtonState(1);
        } else {


            changeButtonState(0);
            String number = binding.edittextNumber.getText().toString();

            Intent intent = new Intent(LoginActivity.this, OtpActivity.class);
            intent.putExtra("number", number);
            startActivity(intent);
        }
    }

    private void changeButtonState(int state) {
        if (state == 0) {
            binding.btnConnect.setBackground(ContextCompat.getDrawable(this, R.drawable.getstart_back));
        } else {
            binding.btnConnect.setBackground(ContextCompat.getDrawable(this, R.drawable.grey_back));
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        changeButtonState(1);
    }
}