package com.cube9.afary.vendor.presenter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.cube9.afary.R;
import com.cube9.afary.user.home_services.HomeServiceActivity;
import com.cube9.afary.user.home_services.adapter.HomeServicesAdapter;
import com.cube9.afary.user.home_services.helperClass.HomeServicesPojo;
import com.cube9.afary.vendor.model.SkillServicesPojo;
import com.romainpiel.shimmer.Shimmer;

import java.util.List;

public class SkillListAdapter extends RecyclerView.Adapter<SkillListAdapter.MyViewHolder> {
    List<SkillServicesPojo> homeServicesPojoList;

    Context mctx;
    Shimmer shimmer;

    public SkillListAdapter(List<SkillServicesPojo> homeServicesPojoList, Context mctx) {
        this.homeServicesPojoList = homeServicesPojoList;
        this.mctx = mctx;
        shimmer = new Shimmer();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_select_skills, parent, false);
        //  itemView.getLayoutParams().width = (int) (getScreenWidth() / 1.5);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final SkillServicesPojo items = homeServicesPojoList.get(position);
        holder.tv_service_name.setText(items.getService_name());
        //  Glide.with(mctx).load(WebServiceURLs.IMAGE_URL+items.getService_image()).into(holder.iv_services_images);

    }

    @Override
    public int getItemCount() {
        return homeServicesPojoList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_service_name;
       Switch sw_service;

        public MyViewHolder(View view) {
            super(view);

            tv_service_name = view.findViewById(R.id.tv_service_name);
            sw_service = view.findViewById(R.id.sw_service);

            //   shimmer.start(shimmer_premium);
        }
    }

}
