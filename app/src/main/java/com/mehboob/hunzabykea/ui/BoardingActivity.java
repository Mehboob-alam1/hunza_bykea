package com.mehboob.hunzabykea.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.mehboob.hunzabykea.R;
import com.mehboob.hunzabykea.databinding.ActivityBoardingBinding;

public class BoardingActivity extends AppCompatActivity {
    private ActivityBoardingBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBoardingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());




    }
}