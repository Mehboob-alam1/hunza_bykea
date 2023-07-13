package com.mehboob.hunzabykea.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.mehboob.hunzabykea.R;
import com.mehboob.hunzabykea.databinding.ActivitySearchingForDriverBinding;

public class SearchingForDriverActivity extends AppCompatActivity {
private ActivitySearchingForDriverBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySearchingForDriverBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


    }
}