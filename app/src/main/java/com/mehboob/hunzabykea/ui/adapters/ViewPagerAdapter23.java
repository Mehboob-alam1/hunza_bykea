package com.mehboob.hunzabykea.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mehboob.hunzabykea.R;
import com.mehboob.hunzabykea.ui.models.ViewPagerClass;

import java.util.ArrayList;

public class ViewPagerAdapter23 extends RecyclerView.Adapter<ViewPagerAdapter23.MyHolder> {
    private Context context;
    private ArrayList<ViewPagerClass> list;

    public ViewPagerAdapter23(Context context, ArrayList<ViewPagerClass> list) {
        this.context = context;
        this.list = list;
    }
//    int imagesArrr[] =
//            {
//                    R.drawable.frame2,
//                    R.drawable.frame3,
//                    R.drawable.frame4
//            };
//    String headingArrr[]=
//            {
//
//                    context.getResources().getString(R.string.frame2Heading),
//                    context.getResources().getString(R.string.frame3Heading),
//                    context.getResources().getString(R.string.frame4Heading)
//            };
//
//    String descriptionArrr[]=
//            {
//
//                    context.getResources().getString(R.string.frame2description),
//                    context.getResources().getString(R.string.frame3description),
//                    context.getResources().getString(R.string.frame4description)
//            };



    @NonNull
    @Override
    public ViewPagerAdapter23.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewpagerslider,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewPagerAdapter23.MyHolder holder, int position) {
        ViewPagerClass viewPagerClass = list.get(position);

        holder.imageView.setImageResource(viewPagerClass.getImage());
        holder.tv1.setText(viewPagerClass.getHeading());
        holder.tv2.setText(viewPagerClass.getDescription());


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView tv1,tv2;
        public MyHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageview2);
            tv1 = itemView.findViewById(R.id.textviewHeading);
            tv2 = itemView.findViewById(R.id.textviewDescription);
        }
    }
}
