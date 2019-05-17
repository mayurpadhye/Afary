package com.cube9.afary.vendor.vendor_dashbord.model;

import java.util.List;

public interface IVendorServiceModel {

    interface  GetVendorServicesInterface
    {
        void onResultFinished(List<VendorServicesModel> vendorServicesModelList);
        void onFailure(Throwable t);
    }

    public void getVendorServices(GetVendorServicesInterface getVendorServicesInterface,String vendor_id);
}
