package com.cube9.afary.vendor.vendor_dashbord.view;

import com.cube9.afary.vendor.vendor_dashbord.model.VendorServicesModel;

import java.util.List;

public interface IVendorHome {
    public void ShowProgressDialog();
    public void DismissProgressDailog();
    public void GetVendorServices(List<VendorServicesModel> vendorServicesModelList);
    public void onResponseFail(Throwable t);


}
