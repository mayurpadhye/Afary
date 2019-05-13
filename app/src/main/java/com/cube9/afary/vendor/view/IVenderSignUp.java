package com.cube9.afary.vendor.view;

import java.util.List;

public interface IVenderSignUp {

    public void showProgressBar();
    public void hideProgressBar();
    public void doRegistration(String status);
    public void responseFailure(Throwable t);
    public void getCountryResult(List<String> countryList,List<String> countryListId);
    public int validateData(int result,int position);



}
