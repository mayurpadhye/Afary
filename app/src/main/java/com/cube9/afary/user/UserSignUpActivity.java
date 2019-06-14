package com.cube9.afary.user;

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
import android.widget.Toast;

import com.cube9.afary.R;
import com.cube9.afary.helperClass.CustomUtils;
import com.cube9.afary.network.RestInterface;
import com.cube9.afary.network.RetrofitClient;
import com.cube9.afary.network.WebServiceURLs;
import com.cube9.afary.user.signup.CompleteRegistrationActivity;
import com.google.gson.JsonElement;
import com.valdesekamdem.library.mdtoast.MDToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dmax.dialog.SpotsDialog;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class UserSignUpActivity extends AppCompatActivity {
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
    AlertDialog waiting_dialog;
    List<String> listCountryName=new ArrayList<>();
    List<String> listCountryId=new ArrayList<>();
    String country_id="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_user_sign_up);
        ButterKnife.bind(this);
        waiting_dialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage(R.string.loading)
                .setCancelable(false)
                .build();
      //  waiting_dialog.show();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // window.setStatusBarColor();
        }
        onClick();
        getCountry();

    }


    public void checkEmail() {


        try {
            waiting_dialog.show();
            RetrofitClient retrofitClient = new RetrofitClient();
            RestInterface service = retrofitClient.getAPIClient(WebServiceURLs.DOMAIN_NAME);
            service.CheckEmail(et_email.getText().toString(), et_mobile_no.getText().toString().trim(), new Callback<JsonElement>() {
                @Override
                public void success(JsonElement jsonElement, Response response) {

                    try {
                        JSONObject jsonObject = new JSONObject(jsonElement.toString());
                        String email_status = jsonObject.getString("email_status");
                        String email_message = jsonObject.getString("email_message");
                        String mobile_status = jsonObject.getString("mobile_status");
                        String mobile_message = jsonObject.getString("mobile_message");
                        waiting_dialog.dismiss();
                        if (mobile_status.equals("1"))
                        {
                            CustomUtils.showToast(mobile_message,UserSignUpActivity.this,MDToast.TYPE_ERROR);
                            return;
                        } else if (email_status.equals("1"))
                        {
                            CustomUtils.showToast(email_message,UserSignUpActivity.this,MDToast.TYPE_ERROR);
                            return;
                        }
                        else
                        {
                            Intent i=new Intent(UserSignUpActivity.this,CompleteRegistrationActivity.class);
                            i.putExtra("f_name",et_f_name.getText().toString());
                            i.putExtra("l_name",et_last_name.getText().toString());
                            i.putExtra("mobile_no",et_mobile_no.getText().toString());
                            i.putExtra("email",et_email.getText().toString());
                            i.putExtra("password",et_password.getText().toString());
                            i.putExtra("country",sp_country.getSelectedItem().toString());
                            i.putExtra("country_code",country_id);
                            startActivity(i);
                        }




                    } catch (JSONException | NullPointerException e) {
                        waiting_dialog.dismiss();
                        e.printStackTrace();

                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    waiting_dialog.dismiss();
                    // Toast.makeText(UserSignUpActivity.this ,getResources().getString(R.string.check_internet), Toast.LENGTH_LONG ).show();
                    CustomUtils.showToast(getResources().getString(R.string.check_internet), UserSignUpActivity.this, MDToast.TYPE_WARNING);
                }
            });
        } catch (Exception e) {
            waiting_dialog.dismiss();
            e.printStackTrace();

        }
    }

    public void getCountry()
    {
        try {
            waiting_dialog.show();
            RetrofitClient retrofitClient = new RetrofitClient();
            RestInterface service = retrofitClient.getAPIClient(WebServiceURLs.DOMAIN_NAME);
            service.getCountry( new Callback<JsonElement>() {
                @Override
                public void success(JsonElement jsonElement, Response response) {

                    try {
                        listCountryName.clear();
                        listCountryId.clear();
                        JSONObject jsonObject = new JSONObject(jsonElement.toString());

                        JSONArray country=jsonObject.getJSONArray("country");
                        for (int i=0;i<country.length();i++)
                        {
                            JSONObject j1=country.getJSONObject(i);
                            String id=j1.getString("id");
                            String name=j1.getString("name");
                            listCountryName.add(name);
                            listCountryId.add(id);
                        }
                        listCountryName.add(0,getResources().getString(R.string.select_country));
                        listCountryId.add(0,"select_id");
                        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                                (UserSignUpActivity.this, android.R.layout.simple_spinner_item,
                                        listCountryName ); //selected item will look like a spinner set from XML
                        spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                                .simple_spinner_dropdown_item);
                        sp_country.setAdapter(spinnerArrayAdapter);
                        waiting_dialog.dismiss();
                    } catch (JSONException | NullPointerException e) {
                        waiting_dialog.dismiss();
                        e.printStackTrace();

                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    waiting_dialog.dismiss();
                    // Toast.makeText(UserSignUpActivity.this ,getResources().getString(R.string.check_internet), Toast.LENGTH_LONG ).show();
                    CustomUtils.showToast(getResources().getString(R.string.check_internet), UserSignUpActivity.this, MDToast.TYPE_WARNING);
                }
            });
        } catch (Exception e) {
            waiting_dialog.dismiss();
            e.printStackTrace();

        }
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
    public void onNextClick() {
        submitForm();
    }

    public void submitForm() {
        //  SignUp();
        if (isValidateFirstName(et_f_name.getText().toString().trim()) == false) {
            if (et_f_name.getText().toString().trim().length() == 0) {
                et_f_name.requestFocus();
                et_f_name.setError(getResources().getString(R.string.enter_first_name));
            }
            /*else
            {
                et_f_name.requestFocus();
                et_f_name.setError(getResources().getString(R.string.first_name_error));
            }*/

            return;
        } else {
            et_f_name.setError(null);
        }
        if (isValidateLastName(et_last_name.getText().toString().trim()) == false) {
            et_last_name.requestFocus();
            if (et_last_name.getText().toString().trim().length() == 0) {

                et_last_name.setError(getResources().getString(R.string.enter_last_name));
            }
           /* else
                tl_last_name.setError(getResources().getString(R.string.last_name_error));*/
            return;
        } else {
            et_last_name.setError(null);
        }

        if (!isValidMobile(et_mobile_no.getText().toString().trim())) {
            et_mobile_no.requestFocus();
            if (et_mobile_no.getText().toString().trim().length() == 0) {
                et_mobile_no.setError(getResources().getString(R.string.enter_mobile_no));
            } else {
                et_mobile_no.setError(getResources().getString(R.string.mobile_error));
            }

            return;
        } else {
            et_mobile_no.setError(null);
        }
        if (!isValidMail(et_email.getText().toString().trim())) {
            et_email.requestFocus();
            if (et_email.getText().toString().trim().length() == 0) {
                et_email.setError(getResources().getString(R.string.enter_email_id));
            } else {
                et_email.setError(getResources().getString(R.string.email_error));
            }

            return;
        } else {
            et_email.setError(null);
        }


        if (!validatePassword(et_password.getText().toString().trim())) {
            et_password.requestFocus();
            et_password.setError(getResources().getString(R.string.password_error));
            return;
        } else {
            et_password.setError(null);
        }
        if (!validateConfirmPassword(et_confirm_password.getText().toString().trim())) {
            et_confirm_password.requestFocus();
            if (et_confirm_password.getText().toString().trim().length() == 0) {
                et_confirm_password.setError(getResources().getString(R.string.enter_confirm_pass));
            } else
                et_confirm_password.setError(getResources().getString(R.string.c_password_error));
            return;
        } else {
            et_confirm_password.setError(null);
        }

        if (sp_country.getSelectedItem().toString().equals("Select country")) {
            CustomUtils.showToast("Please Select Country", UserSignUpActivity.this, MDToast.TYPE_WARNING);
            return;
        }
        checkEmail();
        // RegisterUser();
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
            et_email.setError("Not Valid Email");
        }
        return check;
    }

    private boolean isValidMobile(String phone) {
        boolean check = false;
        if (!Pattern.matches("[a-zA-Z]+", phone)) {
            if (phone.length() < 8 || phone.length() > 15) {
                // if(phone.length() != 10) {
                check = false;
                et_mobile_no.setError("Not Valid Number");
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

    public boolean validateConfirmPassword(String Cpassword) {
        if (Cpassword.length() > 0 && Cpassword.equals(et_password.getText().toString())) {
            return true;
        } else
            return false;
    }


}


