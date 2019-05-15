package com.cube9.afary.vendor.presenter;

import android.content.Context;
import android.widget.Toast;

import com.cube9.afary.R;
import com.cube9.afary.helperClass.CustomUtils;
import com.cube9.afary.user.UserSignUpActivity;
import com.cube9.afary.vendor.model.IVendorSignUpModel;
import com.cube9.afary.vendor.model.SkillServicesPojo;
import com.cube9.afary.vendor.view.IVenderSignUp;
import com.cube9.afary.vendor.view.VendorSignUpActivity;
import com.valdesekamdem.library.mdtoast.MDToast;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VendorSignUpPresenter implements IVenderSignUpPresenter,IVendorSignUpModel.getCountryListInterface,IVenderSignUpPresenter.ISkillServices {
    IVenderSignUp iVenderSignUp;
    IVendorSignUpModel iVendorSignUpModel;
    Context context;
IVenderSignUp.ISelectSkill iSelectSkill;
IVendorSignUpModel.getSkillServicesInterface getSkillServicesInterface;
IVendorSignUpModel.getSkillServicesInterface.getSkills getSkills;


    public VendorSignUpPresenter(Context context, IVenderSignUp.ISelectSkill iSelectSkill, IVendorSignUpModel.getSkillServicesInterface getSkillServicesInterface) {
        this.context = context;
        this.iSelectSkill = iSelectSkill;
        this.getSkillServicesInterface = getSkillServicesInterface;



    }

    public VendorSignUpPresenter(IVenderSignUp iVenderSignUp, IVendorSignUpModel iVendorSignUpModel, Context context) {
        this.iVenderSignUp = iVenderSignUp;
        this.context=context;
        this.iVendorSignUpModel = iVendorSignUpModel;
    }

    @Override
    public void clear() {

    }

    @Override
    public void requestCountryList() {
        iVenderSignUp.showProgressBar();
        iVendorSignUpModel.getCountryList(this);

    }


    @Override
    public int setLoadingDialoVisibility(int visibility) {
        return visibility;
    }

    @Override
    public void validateData(String f_name, String l_name, String mobile_no, String email, String password, String confirm_pass, String country) {

        //  SignUp();
        if (f_name.length() == 0) {
            iVenderSignUp.validateData(0,1);
            return;

        } else {

        }
        if (l_name.length() == 0) {

            iVenderSignUp.validateData(0,2);
            return;
        } else {

        }

        if (!isValidMobile(mobile_no)) {

            if (mobile_no.length() == 0) {
                iVenderSignUp.validateData(0,3);
               // et_mobile_no.setError(getResources().getString(R.string.enter_mobile_no));
            } else {
                iVenderSignUp.validateData(0,4);
               // et_mobile_no.setError(getResources().getString(R.string.mobile_error));
            }

            return;
        } else {

        }
        if (!isValidMail(email)) {

            if (email.length() == 0) {
                iVenderSignUp.validateData(0,5);
               // et_email.setError(getResources().getString(R.string.enter_email_id));
            } else {
                iVenderSignUp.validateData(0,6);
                //et_email.setError(getResources().getString(R.string.email_error));
            }

            return;
        } else {
          //  et_email.setError(null);
        }


        if (!validatePassword(password)) {
            iVenderSignUp.validateData(0,7);
            return;
        } else {
           // et_password.setError(null);
        }
        if (!validateConfirmPassword(confirm_pass,password)) {
         //   et_confirm_password.requestFocus();
            if (confirm_pass.length() == 0) {
                iVenderSignUp.validateData(0,8);
              //  et_confirm_password.setError(getResources().getString(R.string.enter_confirm_pass));
            } else
                iVenderSignUp.validateData(0,9);
              //  et_confirm_password.setError(getResources().getString(R.string.c_password_error));

            return;
        } else {
           // et_confirm_password.setError(null);
        }

        if (country.equals(context.getResources().getString(R.string.select_country))) {
            iVenderSignUp.validateData(0,10);
        //    CustomUtils.showToast("Please Select Country", UserSignUpActivity.this, MDToast.TYPE_WARNING);
            return;
        }
        iVenderSignUp.validateData(1,0);
    }


    private boolean isValidMail(String email) {
        boolean check;
        Pattern p;
        Matcher m;

        String EMAIL_STRING = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        p = Pattern.compile(EMAIL_STRING);

        m = p.matcher(email);
        check = m.matches();

        if (!check) {
            //et_email.setError("Not Valid Email");
        }
        return check;
    }

    private boolean isValidMobile(String phone) {
        boolean check = false;
        if (!Pattern.matches("[a-zA-Z]+", phone)) {
            if (phone.length() < 8 || phone.length() > 15) {
                // if(phone.length() != 10) {
                check = false;
                //et_mobile_no.setError("Not Valid Number");
            } else {
                check = true;
            }
        } else {
            check = false;
        }
        return check;
    }

    public boolean isValidateFirstName(String firstName) {
        if (firstName.trim().equals(""))
            return false;
        else
            return firstName.matches("[a-zA-Z]*");

    } // end method validateFirstName

    // validate last name
    public boolean isValidateLastName(String lastName) {
        if (lastName.trim().equals(""))
            return false;
        else
            return lastName.matches("[a-zA-Z]*");
    }

    public boolean validatePassword(String password) {
        if (password.length() > 0)
            return true;
        else
            return false;
    }

    public boolean validateConfirmPassword(String Cpassword,String pass) {
        if (Cpassword.length() > 0 && Cpassword.equals(pass)) {
            return true;
        } else
            return false;
    }

    @Override
    public void onFinished(List<String> countryList,List<String> countryListId) {
        if (iVenderSignUp!=null)
        {
            iVenderSignUp.getCountryResult(countryList,countryListId);
            iVenderSignUp.hideProgressBar();
        }

    }




    @Override
    public void onFailure(Throwable t) {

        if (iVenderSignUp!=null)
        {
            iVenderSignUp.responseFailure(t);
            iVenderSignUp.hideProgressBar();
        }
        if (iSelectSkill!=null)
        {
            iSelectSkill.hideProgressDailog();
            iSelectSkill.responseFailureSubCategory(t);
        }

    }

    @Override
    public void requestSjillServices() {
        iSelectSkill.showProgressDailog();
        getSkillServicesInterface.getSkillList(new IVendorSignUpModel.getSkillServicesInterface.getSkills() {
            @Override
            public void onFinished(List<SkillServicesPojo> skillServicesPojoList) {

                iSelectSkill.getSubcategory(skillServicesPojoList);
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }
}
