package com.mehboob.hunzabykea.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.mehboob.hunzabykea.R;
import com.mehboob.hunzabykea.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        binding.btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.editTextNumber.getText().toString().isEmpty())
                {
                    Toast.makeText(LoginActivity.this, "Enter the number", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    String number = binding.editTextNumber.getText().toString();
                    Intent intent = new Intent(LoginActivity.this,OtpActivity.class);

                    startActivity(intent);
                }
            }
        });
    }
}