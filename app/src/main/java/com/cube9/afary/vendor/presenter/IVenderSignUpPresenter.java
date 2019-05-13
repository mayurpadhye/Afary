package com.cube9.afary.vendor.presenter;

import android.app.Dialog;

import java.util.List;

public interface IVenderSignUpPresenter {
    void clear();
  //  void getCountry(List<String> countryList);
    void requestCountryList();
    int setLoadingDialoVisibility(int visibility);
    //void doRegistration()
    void validateData(String f_name,String l_name,String mobile_no,String email,String password,String confirm_pass,String country);
}
