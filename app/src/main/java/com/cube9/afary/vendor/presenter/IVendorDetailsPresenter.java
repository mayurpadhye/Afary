package com.cube9.afary.vendor.presenter;

import java.io.File;

public interface IVendorDetailsPresenter {
    void submitVendorDetails(String cat_id, String vendor_id, String service_id, File profile_image, File document_image);


}
