package com.mehboob.hunzabykea.ui;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.mehboob.hunzabykea.R;
import com.mehboob.hunzabykea.databinding.ActivityBoardingBinding;
import com.mehboob.hunzabykea.ui.adapters.ViewPagerAdapter23;
import com.mehboob.hunzabykea.ui.models.ViewPagerClass;

import java.util.ArrayList;

public class BoardingActivity extends AppCompatActivity {
    private ActivityBoardingBinding binding;


 ViewPagerAdapter23 adapter23;

 ArrayList<ViewPagerClass> list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBoardingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        list = new ArrayList<>();


list.add(new ViewPagerClass(R.drawable.frame2,getResources().getString(R.string.frame2Heading),getResources().getString(R.string.frame2description)));
list.add(new ViewPagerClass(R.drawable.frame3,getResources().getString(R.string.frame3Heading),getResources().getString(R.string.frame3description)));
list.add(new ViewPagerClass(R.drawable.frame4,getResources().getString(R.string.frame4Heading),getResources().getString(R.string.frame4description)));


        binding.btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.imageview.setVisibility(View.GONE);

                binding.textView2.setVisibility(View.GONE);
                binding.btnStart.setVisibility(View.INVISIBLE);
                binding.btnNext.setVisibility(View.VISIBLE);
                binding.dotsIndicator.setVisibility(View.VISIBLE);
                binding.viewpager2.setVisibility(View.VISIBLE);




                adapter23 = new ViewPagerAdapter23(BoardingActivity.this,list);
                binding.viewpager2.setAdapter(adapter23);
                binding.dotsIndicator.attachTo(binding.viewpager2);

            }
        });

        binding.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(BoardingActivity.this, "Clicked successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(BoardingActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });







    }



}