package com.cube9.afary.vendor.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.cube9.afary.R;
import com.cube9.afary.user.home_services.HomeServiceActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SelectCategoryFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
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
    public static String MainServiceId="";
    private String mParam1;
    private String mParam2;
@BindView(R.id.iv_previous)
    ImageView iv_previous;
    private OnFragmentInteractionListener mListener;
View v;
    public SelectCategoryFragment() {

    }


    public static SelectCategoryFragment newInstance(String param1, String param2) {
        SelectCategoryFragment fragment = new SelectCategoryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v= inflater.inflate(R.layout.fragment_select_category, container, false);
        ButterKnife.bind(this,v);

        return v;
    }
    @OnClick(R.id.ll_home_services)
    public void onHomeServiceClick(View view) {
        MainServiceId="2";
        ((VendorDetailsActivity)getActivity()).handleNextClick();




    }
    @OnClick(R.id.iv_previous)
    public void onPreviousClick()
    {
        ((VendorDetailsActivity)getActivity()).previousClick();
    }


    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(Uri uri);
    }
}
