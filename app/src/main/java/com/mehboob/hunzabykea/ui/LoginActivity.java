package com.mehboob.hunzabykea.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.mehboob.hunzabykea.R;
import com.mehboob.hunzabykea.databinding.ActivityLoginBinding;

import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {

   private ActivityLoginBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());



        binding.btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

checkValidation();
            }
        });
    }

    private void checkValidation() {

        if (binding.edittextNumber.getText().toString().isEmpty() || binding.edittextNumber
                .getText().toString().trim().length()!=11)
        {
            Toast.makeText(this, "Enter a Valid Number ", Toast.LENGTH_SHORT).show();
            binding.textviewError.setVisibility(View.VISIBLE);
        }

        else
        {


            String number = binding.edittextNumber.getText().toString();

            Intent intent = new Intent(LoginActivity.this,OtpActivity.class);
            intent.putExtra("number",number);
            startActivity(intent);
        }
    }


}