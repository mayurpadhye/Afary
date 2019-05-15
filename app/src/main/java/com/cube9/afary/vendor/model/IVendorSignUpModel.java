package com.cube9.afary.vendor.model;

import java.util.List;

public interface IVendorSignUpModel {

   interface  getCountryListInterface
   {
       void  onFinished(List<String> countryList,List<String> countryListId);

       void onFailure(Throwable t);

   }

   void getCountryList(getCountryListInterface getCountryListInterface);

   interface getSkillServicesInterface{

       interface getSkills
       {
           void onFinished(List<SkillServicesPojo> skillServicesPojoList);
           void onFailure(Throwable t);
       }

       void getSkillList(getSkills getSkills);
   }

}
