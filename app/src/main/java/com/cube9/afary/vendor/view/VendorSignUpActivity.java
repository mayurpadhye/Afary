package com.cube9.afary.vendor.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.cube9.afary.R;
import com.cube9.afary.helperClass.CustomUtils;
import com.cube9.afary.user.UserSignUpActivity;
import com.cube9.afary.user.signup.CompleteRegistrationActivity;
import com.cube9.afary.vendor.model.VendorSignUpModel;
import com.cube9.afary.vendor.presenter.VendorSignUpPresenter;
import com.valdesekamdem.library.mdtoast.MDToast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VendorSignUpActivity extends AppCompatActivity implements IVenderSignUp {
    @BindView(R.id.et_f_name)
    EditText et_f_name;
    @BindView(R.id.et_last_name)
    EditText et_last_name;
    @BindView(R.id.et_mobile_no)
    EditText et_mobile_no;
    @BindView(R.id.et_email)
    EditText et_email;
    @BindView(R.id.et_password)
    EditText et_password;
    @BindView(R.id.et_confirm_password)
    EditText et_confirm_password;
    @BindView(R.id.btn_next)
    Button btn_next;
    @BindView(R.id.ll_country_spinner)
    LinearLayout ll_country_spinner;
    @BindView(R.id.sp_country)
    Spinner sp_country;
    List<String> listCountryName=new ArrayList<>();
    List<String> listCountryId=new ArrayList<>();
    VendorSignUpPresenter vendorSignUpPresenter;
String country_id="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_sign_up);
        ButterKnife.bind(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // window.setStatusBarColor();
        }
        vendorSignUpPresenter = new VendorSignUpPresenter(this, new VendorSignUpModel(),VendorSignUpActivity.this);
        vendorSignUpPresenter.requestCountryList();
        onClick();
    }

    @Override
    public void showProgressBar() {
        CustomUtils.ShowDialog(VendorSignUpActivity.this);
    }

    @Override
    public void hideProgressBar() {
        CustomUtils.DismissDialog();
    }

    @Override
    public void doRegistration(String status) {

    }

    @Override
    public void responseFailure(Throwable t) {

    }

    @Override
    public void getCountryResult(List<String> countryList,List<String> countryListId) {
        countryList.add(0,getResources().getString(R.string.select_country));
        countryListId.add(0,getResources().getString(R.string.select_country));

        listCountryName.addAll(countryList);
        listCountryId.addAll(countryListId);

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                (VendorSignUpActivity.this, android.R.layout.simple_spinner_item,
                        countryList); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);
        sp_country.setAdapter(spinnerArrayAdapter);
    }

    @Override
    public int validateData(int result,int position) {
        if (result == 0) {

         switch (position)
           {
               case 1:
                   et_f_name.requestFocus();
                   et_f_name.setError(getResources().getString(R.string.enter_first_name));
                   break;
               case 2:
                   et_last_name.requestFocus();
                   et_last_name.setError(getResources().getString(R.string.enter_last_name));
                   break;

               case 3:
                   et_mobile_no.requestFocus();
                   et_mobile_no.setError(getResources().getString(R.string.enter_mobile_no));
                   break;
               case 4:
                   et_mobile_no.requestFocus();
                   et_mobile_no.setError(getResources().getString(R.string.mobile_error));
                   break;

               case 5:
                   et_email.requestFocus();
                   et_email.setError(getResources().getString(R.string.enter_email_id));
                   break;
               case 6:
                   et_email.requestFocus();
                   et_email.setError(getResources().getString(R.string.email_error));
                   break;

               case 7:
                   et_password.requestFocus();
                   et_password.setError(getResources().getString(R.string.password_error));
                   break;

               case 8:
                   et_confirm_password.requestFocus();
                   et_confirm_password.setError(getResources().getString(R.string.enter_confirm_pass));
                //   MDToast.makeText(VendorSignUpActivity.this, "Please Select Country", MDToast.LENGTH_SHORT).show();
                   break;

               case 9:
                   et_confirm_password.requestFocus();
                   et_confirm_password.setError(getResources().getString(R.string.c_password_error));
                   //   MDToast.makeText(VendorSignUpActivity.this, "Please Select Country", MDToast.LENGTH_SHORT).show();
                   break;
               case 10:
                   MDToast.makeText(VendorSignUpActivity.this, "Please Select Country", MDToast.LENGTH_SHORT).show();
                   break;

           }


        }
        else
        {
            Intent i=new Intent(VendorSignUpActivity.this,CompleteVenderSignUpActivity.class);
            i.putExtra("f_name",et_f_name.getText().toString());
            i.putExtra("l_name",et_last_name.getText().toString());
            i.putExtra("mobile_no",et_mobile_no.getText().toString());
            i.putExtra("email",et_email.getText().toString());
            i.putExtra("password",et_password.getText().toString());
            i.putExtra("country",sp_country.getSelectedItem().toString());
            i.putExtra("country_code",country_id);
            startActivity(i);

        }
        return result;
    }

    public void onClick() {


    sp_country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                if (listCountryName.get(position).equals(getResources().getString(R.string.select_country)))
                {
                    country_id="";
                }
                else
                {
                    country_id=listCountryId.get(position);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        et_f_name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    et_f_name.setBackground(getResources().getDrawable(R.drawable.edittext_shadow));

                    int paddingDp = 20;
                    float density = getResources().getDisplayMetrics().density;
                    int paddingPixel = (int) (paddingDp * density);
                    et_f_name.setPadding(paddingPixel, paddingPixel, paddingPixel, paddingPixel);

                    et_f_name.requestFocus();
                    et_last_name.setBackground(getResources().getDrawable(R.drawable.rectangle_background));
                    et_mobile_no.setBackground(getResources().getDrawable(R.drawable.rectangle_background));
                    et_email.setBackground(getResources().getDrawable(R.drawable.rectangle_background));
                    et_password.setBackground(getResources().getDrawable(R.drawable.rectangle_background));
                    et_confirm_password.setBackground(getResources().getDrawable(R.drawable.rectangle_background));
                    ll_country_spinner.setBackground(getResources().getDrawable(R.drawable.rectangle_background));


                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(et_f_name, InputMethodManager.SHOW_IMPLICIT);
                }
            }
        });

        et_last_name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    et_last_name.setBackground(getResources().getDrawable(R.drawable.edittext_shadow));

                    int paddingDp = 20;
                    float density = getResources().getDisplayMetrics().density;
                    int paddingPixel = (int) (paddingDp * density);
                    et_last_name.setPadding(paddingPixel, paddingPixel, paddingPixel, paddingPixel);

                    et_last_name.requestFocus();
                    et_f_name.setBackground(getResources().getDrawable(R.drawable.rectangle_background));
                    et_mobile_no.setBackground(getResources().getDrawable(R.drawable.rectangle_background));
                    et_email.setBackground(getResources().getDrawable(R.drawable.rectangle_background));
                    et_password.setBackground(getResources().getDrawable(R.drawable.rectangle_background));
                    et_confirm_password.setBackground(getResources().getDrawable(R.drawable.rectangle_background));
                    ll_country_spinner.setBackground(getResources().getDrawable(R.drawable.rectangle_background));


                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(et_last_name, InputMethodManager.SHOW_IMPLICIT);
                }
            }
        });

        et_mobile_no.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    et_mobile_no.setBackground(getResources().getDrawable(R.drawable.edittext_shadow));

                    int paddingDp = 20;
                    float density = getResources().getDisplayMetrics().density;
                    int paddingPixel = (int) (paddingDp * density);
                    et_mobile_no.setPadding(paddingPixel, paddingPixel, paddingPixel, paddingPixel);

                    et_mobile_no.requestFocus();
                    et_f_name.setBackground(getResources().getDrawable(R.drawable.rectangle_background));
                    et_last_name.setBackground(getResources().getDrawable(R.drawable.rectangle_background));
                    et_email.setBackground(getResources().getDrawable(R.drawable.rectangle_background));
                    et_password.setBackground(getResources().getDrawable(R.drawable.rectangle_background));
                    et_confirm_password.setBackground(getResources().getDrawable(R.drawable.rectangle_background));
                    ll_country_spinner.setBackground(getResources().getDrawable(R.drawable.rectangle_background));


                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(et_mobile_no, InputMethodManager.SHOW_IMPLICIT);
                }
            }
        });

        et_email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    et_email.setBackground(getResources().getDrawable(R.drawable.edittext_shadow));

                    int paddingDp = 20;
                    float density = getResources().getDisplayMetrics().density;
                    int paddingPixel = (int) (paddingDp * density);
                    et_email.setPadding(paddingPixel, paddingPixel, paddingPixel, paddingPixel);

                    et_email.requestFocus();
                    et_f_name.setBackground(getResources().getDrawable(R.drawable.rectangle_background));
                    et_last_name.setBackground(getResources().getDrawable(R.drawable.rectangle_background));
                    et_mobile_no.setBackground(getResources().getDrawable(R.drawable.rectangle_background));
                    et_password.setBackground(getResources().getDrawable(R.drawable.rectangle_background));
                    et_confirm_password.setBackground(getResources().getDrawable(R.drawable.rectangle_background));
                    ll_country_spinner.setBackground(getResources().getDrawable(R.drawable.rectangle_background));


                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(et_email, InputMethodManager.SHOW_IMPLICIT);
                }
            }
        });

        et_password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    et_password.setBackground(getResources().getDrawable(R.drawable.edittext_shadow));

                    int paddingDp = 20;
                    float density = getResources().getDisplayMetrics().density;
                    int paddingPixel = (int) (paddingDp * density);
                    et_password.setPadding(paddingPixel, paddingPixel, paddingPixel, paddingPixel);

                    et_password.requestFocus();
                    et_f_name.setBackground(getResources().getDrawable(R.drawable.rectangle_background));
                    et_last_name.setBackground(getResources().getDrawable(R.drawable.rectangle_background));
                    et_mobile_no.setBackground(getResources().getDrawable(R.drawable.rectangle_background));
                    et_email.setBackground(getResources().getDrawable(R.drawable.rectangle_background));
                    et_confirm_password.setBackground(getResources().getDrawable(R.drawable.rectangle_background));
                    ll_country_spinner.setBackground(getResources().getDrawable(R.drawable.rectangle_background));


                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(et_password, InputMethodManager.SHOW_IMPLICIT);
                }
            }
        });

        et_confirm_password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    et_confirm_password.setBackground(getResources().getDrawable(R.drawable.edittext_shadow));

                    int paddingDp = 20;
                    float density = getResources().getDisplayMetrics().density;
                    int paddingPixel = (int) (paddingDp * density);
                    et_confirm_password.setPadding(paddingPixel, paddingPixel, paddingPixel, paddingPixel);

                    et_confirm_password.requestFocus();
                    et_f_name.setBackground(getResources().getDrawable(R.drawable.rectangle_background));
                    et_last_name.setBackground(getResources().getDrawable(R.drawable.rectangle_background));
                    et_mobile_no.setBackground(getResources().getDrawable(R.drawable.rectangle_background));
                    et_email.setBackground(getResources().getDrawable(R.drawable.rectangle_background));
                    et_password.setBackground(getResources().getDrawable(R.drawable.rectangle_background));
                    //  et_confirm_password.setBackground(getResources().getDrawable(R.drawable.rectangle_background));
                    ll_country_spinner.setBackground(getResources().getDrawable(R.drawable.rectangle_background));


                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(et_confirm_password, InputMethodManager.SHOW_IMPLICIT);
                }
            }
        });

        sp_country.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ll_country_spinner.setBackground(getResources().getDrawable(R.drawable.edittext_shadow));

                int paddingDp = 20;
                float density = getResources().getDisplayMetrics().density;
                int paddingPixel = (int) (paddingDp * density);
                // et_confirm_password.setPadding(paddingPixel,paddingPixel,paddingPixel,paddingPixel);

                ll_country_spinner.requestFocus();
                et_f_name.setBackground(getResources().getDrawable(R.drawable.rectangle_background));
                et_last_name.setBackground(getResources().getDrawable(R.drawable.rectangle_background));
                et_mobile_no.setBackground(getResources().getDrawable(R.drawable.rectangle_background));
                et_email.setBackground(getResources().getDrawable(R.drawable.rectangle_background));
                et_password.setBackground(getResources().getDrawable(R.drawable.rectangle_background));
                et_confirm_password.setBackground(getResources().getDrawable(R.drawable.rectangle_background));
                return false;
            }


        });
    }

    @OnClick(R.id.btn_next)
    public void onNextClick()
    {
        vendorSignUpPresenter.validateData(et_f_name.getText().toString(),et_last_name.getText().toString().trim(),et_mobile_no.getText().toString().trim(),et_email.getText().toString().trim(),et_password.getText().toString().trim(),et_confirm_password.getText().toString().trim(),sp_country.getSelectedItem().toString());
    }
}
