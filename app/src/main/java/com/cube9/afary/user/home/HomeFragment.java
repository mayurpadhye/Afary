package com.cube9.afary.user.home;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.cube9.afary.R;
import com.cube9.afary.user.home_services.HomeServiceActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    Unbinder unbinder;
     View v;
    @BindView(R.id.ll_article)
    LinearLayout ll_article;
    @BindView(R.id.ll_home_services)
    LinearLayout ll_home_services;
    @BindView(R.id.ll_medical_consultation)
    LinearLayout ll_medical_consultation;
    @BindView(R.id.ll_pharmacy)
    LinearLayout ll_pharmacy;
    @BindView(R.id.ll_car_services)
    LinearLayout ll_car_services;
    @BindView(R.id.ll_pay_taxi)
    LinearLayout ll_pay_taxi;
    @BindView(R.id.ll_delivery)
    LinearLayout ll_delivery;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_home, container, false);
// bind view using butter knife
        unbinder = ButterKnife.bind(this, v);
        return v;
    }

    @OnClick(R.id.ll_home_services)
    public void onHomeServiceClick(View view) {


        startActivity(new Intent(getActivity(),HomeServiceActivity.class));
       /* HomeActivity.CURRENT_TAG=HomeActivity.TAG_HOME_SERVICE;
        HomeServiceFragment homeServiceFragment = new HomeServiceFragment();
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fl_main, homeServiceFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();*/

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        // unbind the view to free some memory
        unbinder.unbind();
    }
}
