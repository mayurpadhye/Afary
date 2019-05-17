package com.cube9.afary.vendor.vendor_dashbord.view;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cube9.afary.R;
import com.cube9.afary.helperClass.CustomUtils;
import com.cube9.afary.helperClass.PrefManager;
import com.cube9.afary.vendor.vendor_dashbord.model.VendorServicesModel;
import com.cube9.afary.vendor.vendor_dashbord.presenter.IVendorHomePresenter;
import com.cube9.afary.vendor.vendor_dashbord.presenter.VendorHomePresenterImpl;
import com.cube9.afary.vendor.vendor_dashbord.presenter.VendorServicesAdapter;
import com.cube9.afary.vendor.view.RecyclerviewItemClickListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VendorHomeFragment extends Fragment  implements  IVendorHome{

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;
View v;
IVendorHomePresenter iVendorHomePresenter;
    @BindView(R.id.rv_my_services)
    RecyclerView rv_my_services;
    public VendorHomeFragment() {
        // Required empty public constructor
    }


    public static VendorHomeFragment newInstance(String param1, String param2) {
        VendorHomeFragment fragment = new VendorHomeFragment();
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
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_vendor_home, container, false);
        ButterKnife.bind(this,v);
        iVendorHomePresenter=new VendorHomePresenterImpl(this,getActivity(),new VendorServicesModel());
        iVendorHomePresenter.requestVendorServices(PrefManager.getInstance(getActivity()).getUserId());

        GridLayoutManager manager = new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false);
        rv_my_services.setLayoutManager(manager);
        return v;
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

    @Override
    public void ShowProgressDialog() {
        CustomUtils.ShowDialog(getActivity());

    }

    @Override
    public void DismissProgressDailog() {
CustomUtils.DismissDialog();
    }

    @Override
    public void GetVendorServices(List<VendorServicesModel> vendorServicesModelList) {

        if (vendorServicesModelList.size()>0)
        {
            VendorServicesAdapter vendorServicesAdapter=new VendorServicesAdapter(vendorServicesModelList, getActivity(), new RecyclerviewItemClickListener() {
                @Override
                public void onItemClick(View v, int position, boolean isChecked) {

                }
            });

            rv_my_services.setAdapter(vendorServicesAdapter);
        }

    }

    @Override
    public void onResponseFail(Throwable t) {

    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
