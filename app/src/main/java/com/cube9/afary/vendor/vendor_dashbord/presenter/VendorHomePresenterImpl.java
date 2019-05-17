package com.cube9.afary.vendor.vendor_dashbord.presenter;

import android.content.Context;

import com.cube9.afary.vendor.model.IVendorDetailsModel;
import com.cube9.afary.vendor.vendor_dashbord.model.IVendorServiceModel;
import com.cube9.afary.vendor.vendor_dashbord.model.VendorServicesModel;
import com.cube9.afary.vendor.vendor_dashbord.view.IVendorHome;
import com.cube9.afary.vendor.view.IVenderDetailsView;

import java.util.List;

public class VendorHomePresenterImpl implements IVendorHomePresenter,IVendorServiceModel.GetVendorServicesInterface{

    IVendorHome iVendorHome;
    Context context;
    IVendorServiceModel iVendorServiceModel;

    public VendorHomePresenterImpl(IVendorHome iVendorHome, Context context, IVendorServiceModel iVendorServiceModel) {
        this.iVendorHome = iVendorHome;
        this.context = context;
        this.iVendorServiceModel = iVendorServiceModel;
    }

    @Override
    public void requestVendorServices(String vendor_id) {
        iVendorHome.ShowProgressDialog();
        iVendorServiceModel.getVendorServices(this,vendor_id);
    }

    @Override
    public void onResultFinished(List<VendorServicesModel> vendorServicesModelList) {
        iVendorHome.DismissProgressDailog();
        iVendorHome.GetVendorServices(vendorServicesModelList);
    }

    @Override
    public void onFailure(Throwable t) {
        iVendorHome.DismissProgressDailog();
        iVendorHome.onResponseFail(t);
    }
}
