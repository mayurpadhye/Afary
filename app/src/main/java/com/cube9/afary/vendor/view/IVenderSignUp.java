package com.cube9.afary.vendor.view;

import com.cube9.afary.vendor.model.SkillServicesPojo;

import java.util.List;

public interface IVenderSignUp {

    public void showProgressBar();
    public void hideProgressBar();
    public void doRegistration(String status);
    public void responseFailure(Throwable t);
    public void getCountryResult(List<String> countryList,List<String> countryListId);
    public int validateData(int result,int position);

    interface ISelectSkill{
        public void showProgressDailog();
        public void hideProgressDailog();
        public void getSubcategory(List<SkillServicesPojo> skillServicesPojoList);
        public void responseFailureSubCategory(Throwable t);

    }



}
