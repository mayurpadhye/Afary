package com.cube9.afary.vendor.model;

import java.io.File;

public interface IVendorDetailsModel {
    interface getVendorDetailsInterface
    {
        void onResultFinished(int status);
        void onResultFailure(Throwable t);

    }


    int completeVendorSignUp(getVendorDetailsInterface getVendorDetailsInterface,String cat_id, String vendor_id, String service_id, File profile_image, File document_image);
}
