package com.mehboob.hunzabykea.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mehboob.hunzabykea.R;
import com.mehboob.hunzabykea.ui.models.Banner;

import java.util.ArrayList;

public class BannerAdapter extends RecyclerView.Adapter<BannerAdapter.BannerHolder>{
private Context context;
private ArrayList<Banner> list;

    public BannerAdapter(Context context, ArrayList<Banner> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public BannerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.banner_layout,parent,false);

        return new BannerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BannerHolder holder, int position) {
    Banner data=    list.get(position);

    holder.bannerImage.setImageResource(data.getBannerImage());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public   class BannerHolder extends RecyclerView.ViewHolder{
        private ImageView bannerImage;
        public BannerHolder(@NonNull View itemView) {
            super(itemView);

            bannerImage=itemView.findViewById(R.id.bannerImage);
        }
    }
}
