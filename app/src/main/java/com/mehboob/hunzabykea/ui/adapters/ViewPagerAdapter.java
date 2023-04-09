package com.mehboob.hunzabykea.ui.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import com.mehboob.hunzabykea.R;
import com.mehboob.hunzabykea.ui.models.ViewPagerClass;

import java.util.ArrayList;

public class ViewPagerAdapter extends PagerAdapter {
    private Context ctx;


    public ViewPagerAdapter(Context context) {
        this.ctx = context;

    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }



    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        LayoutInflater layoutInflater = (LayoutInflater) ctx.getSystemService(ctx.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.viewpagerslider, container, false);

        ImageView imageView;


        imageView = view.findViewById(R.id.slider_image);
        TextView heading = view.findViewById(R.id.textviewHeading);
        TextView desc = view.findViewById(R.id.textviewDescription);

        switch (position) {
            case 0:
                imageView.setImageResource(R.drawable.frame2);
                heading.setText("Book Your Rider");
                desc.setText("With Hunza Bikeya, You can book a bike rider in hunza anywhere anytime.");
                break;
            case 1:
                imageView.setImageResource(R.drawable.frame3);
                heading.setText("Best Experience of Riding");
                desc.setText("Let’s keep burning, to archive yours goals,We \n" +
                        "provide professional taxi services for you");
                break;
            case 2:
                imageView.setImageResource(R.drawable.frame4);
                heading.setText("Let's Start");
                desc.setText("Your satisfaction is our number one priority\n" +
                        "Let’s make your day great with Hunza Bikeya!");
                break;

        }


        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {


        container.removeView((View) object);

    }
}
