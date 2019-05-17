package com.cube9.afary.vendor.vendor_dashbord.presenter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.cube9.afary.R;
import com.cube9.afary.vendor.model.SkillServicesPojo;
import com.cube9.afary.vendor.presenter.SkillListAdapter;
import com.cube9.afary.vendor.vendor_dashbord.model.VendorServicesModel;
import com.cube9.afary.vendor.view.RecyclerviewItemClickListener;
import com.romainpiel.shimmer.Shimmer;

import java.util.List;

public class VendorServicesAdapter  extends RecyclerView.Adapter<VendorServicesAdapter.MyViewHolder> {

    List<VendorServicesModel> homeServicesPojoList;

    Context mctx;
    Shimmer shimmer;
    RecyclerviewItemClickListener recyclerviewItemClickListener;
    public VendorServicesAdapter(List<VendorServicesModel> homeServicesPojoList, Context mctx, RecyclerviewItemClickListener recyclerviewItemClickListener) {
        this.homeServicesPojoList = homeServicesPojoList;
        this.mctx = mctx;
        this.recyclerviewItemClickListener=recyclerviewItemClickListener;
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
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final VendorServicesModel items = homeServicesPojoList.get(position);
        holder.tv_service_name.setText(items.getService_name());
        //  Glide.with(mctx).load(WebServiceURLs.IMAGE_URL+items.getService_image()).into(holder.iv_services_images);

    }

    @Override
    public int getItemCount() {
        return homeServicesPojoList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_service_name;


        public MyViewHolder(View view) {
            super(view);

            tv_service_name = view.findViewById(R.id.tv_service_name);


            //   shimmer.start(shimmer_premium);
        }
    }

}
