package com.cube9.afary.user.home_services.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cube9.afary.R;
import com.cube9.afary.network.WebServiceURLs;
import com.cube9.afary.user.home_services.HomeServiceActivity;
import com.cube9.afary.user.home_services.helperClass.HomeServicesPojo;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;

import java.util.ArrayList;
import java.util.List;

public class HomeServicesAdapter extends RecyclerView.Adapter<HomeServicesAdapter.MyViewHolder> {
    List<HomeServicesPojo> homeServicesPojoList;

    Context mctx;
    Shimmer shimmer;

    public HomeServicesAdapter(List<HomeServicesPojo> homeServicesPojoList, Context mctx) {
        this.homeServicesPojoList = homeServicesPojoList;
        this.mctx = mctx;
        shimmer = new Shimmer();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_home_services, parent, false);
        //  itemView.getLayoutParams().width = (int) (getScreenWidth() / 1.5);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final HomeServicesPojo items = homeServicesPojoList.get(position);
        holder.tv_service_name.setText(items.getService_name());
        //  Glide.with(mctx).load(WebServiceURLs.IMAGE_URL+items.getService_image()).into(holder.iv_services_images);
        holder.ll_house.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mctx instanceof HomeServiceActivity) {((HomeServiceActivity) mctx).homeServices(items.getService_id(),items.getService_name());}
            }
        });
    }

    @Override
    public int getItemCount() {
        return homeServicesPojoList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_services_images;
        TextView tv_service_name, tv_desc;
        CardView cv_main;
        LinearLayout ll_house;

        public MyViewHolder(View view) {
            super(view);
            iv_services_images = view.findViewById(R.id.iv_services_images);
            tv_service_name = view.findViewById(R.id.tv_service_name);
            tv_desc = view.findViewById(R.id.tv_desc);
            ll_house = view.findViewById(R.id.ll_house);
            cv_main = view.findViewById(R.id.cv_main);
            //   shimmer.start(shimmer_premium);
        }
    }


}
