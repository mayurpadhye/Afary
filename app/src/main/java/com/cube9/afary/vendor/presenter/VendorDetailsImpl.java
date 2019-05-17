package com.cube9.afary.vendor.presenter;

import android.content.Context;

import com.cube9.afary.vendor.model.IVendorDetailsModel;
import com.cube9.afary.vendor.model.VendorDetailsModel;
import com.cube9.afary.vendor.view.IVenderDetailsView;

import java.io.File;

public class VendorDetailsImpl implements IVendorDetailsPresenter {
    IVenderDetailsView iVenderDetailsView;
    Context context;
IVendorDetailsModel iVendorDetailsModel;
    public VendorDetailsImpl(IVenderDetailsView iVenderDetailsView, Context context) {
        this.iVenderDetailsView = iVenderDetailsView;
        this.context = context;
        iVendorDetailsModel=new VendorDetailsModel();
    }

    @Override
    public void submitVendorDetails(String cat_id, String vendor_id, String service_id, File profile_image, File document_image) {

        iVenderDetailsView.showProgressBar();
    int result= iVendorDetailsModel.completeVendorSignUp(new IVendorDetailsModel.getVendorDetailsInterface() {
        @Override
        public void onResultFinished(int status) {
            iVenderDetailsView.hideProgressBar();
            iVenderDetailsView.completeVendorRegistration(status);
        }

        @Override
        public void onResultFailure(Throwable t) {
            iVenderDetailsView.hideProgressBar();
            iVenderDetailsView.failure(t);
        }
    }, cat_id, vendor_id, service_id, profile_image, document_image);




    }
}
