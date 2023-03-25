package com.mehboob.hunzabykea.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.mehboob.hunzabykea.R;
import com.mehboob.hunzabykea.databinding.ActivityBoardingBinding;
import com.mehboob.hunzabykea.ui.adapters.ViewPagerAdapter;
import com.mehboob.hunzabykea.ui.models.ViewPagerClass;

import java.util.ArrayList;

public class BoardingActivity extends AppCompatActivity {
    private ActivityBoardingBinding binding;


    private ViewPagerAdapter adapter;

    private ArrayList<ViewPagerClass> list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBoardingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        list = new ArrayList<>();


        list.add(new ViewPagerClass(R.drawable.frame2, getResources().getString(R.string.frame2Heading), getResources().getString(R.string.frame2description)));
        list.add(new ViewPagerClass(R.drawable.frame3, getResources().getString(R.string.frame3Heading), getResources().getString(R.string.frame3description)));
        list.add(new ViewPagerClass(R.drawable.frame4, getResources().getString(R.string.frame4Heading), getResources().getString(R.string.frame4description)));


        binding.btnStart.setOnClickListener(view -> {
            binding.imageview.setVisibility(View.GONE);

            binding.textView2.setVisibility(View.GONE);
            binding.btnStart.setVisibility(View.INVISIBLE);
            binding.btnNext.setVisibility(View.VISIBLE);
            binding.dotsIndicator.setVisibility(View.VISIBLE);
            binding.viewpager2.setVisibility(View.VISIBLE);


            adapter = new ViewPagerAdapter(BoardingActivity.this);
            binding.viewpager2.setAdapter(adapter);
            binding.dotsIndicator.attachTo(binding.viewpager2);

        });
binding.viewpager2.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        switch (position){
            case 0:
                controlButtons();
                break;
            case 1:
                controlButtons();
                break;
            case 2:
                binding.btnNext.setVisibility(View.INVISIBLE);
                binding.btnStart.setVisibility(View.VISIBLE);

                binding.btnStart.setOnClickListener(v -> {
                   // Intent intent = new Intent(BoardingActivity.this, LoginActivity.class);
            startActivity(new Intent(BoardingActivity.this,LoginActivity.class));
                });
        }


    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
});
        binding.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(BoardingActivity.this, "Clicked successfully", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(BoardingActivity.this, LoginActivity.class);
//                startActivity(intent);
                binding.viewpager2.setCurrentItem(binding.viewpager2.getCurrentItem() + 1, true);
            }
        });


    }

    private void controlButtons() {

            binding.btnNext.setVisibility(View.VISIBLE);
            binding.btnStart.setVisibility(View.INVISIBLE);

    }


}