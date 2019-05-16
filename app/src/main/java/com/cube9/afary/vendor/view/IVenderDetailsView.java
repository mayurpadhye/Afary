package com.cube9.afary.vendor.view;

public interface IVenderDetailsView {
    public void showProgressBar();
    public void hideProgressBar();
    public void completeVendorRegistration(int result);
    public void failure(Throwable t);

}
