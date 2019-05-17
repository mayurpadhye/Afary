package com.cube9.afary.login;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import com.cube9.afary.R;
import com.cube9.afary.helperClass.CustomUtils;
import com.cube9.afary.helperClass.PrefManager;
import com.cube9.afary.network.RestInterface;
import com.cube9.afary.network.RetrofitClient;
import com.cube9.afary.network.WebServiceURLs;
import com.cube9.afary.user.home.HomeActivity;
import com.cube9.afary.user.signup.CompleteRegistrationActivity;
import com.cube9.afary.vendor.vendor_dashbord.VendorHomeActivity;
import com.cube9.afary.vendor.view.VendorDetailsActivity;
import com.google.gson.JsonElement;
import com.valdesekamdem.library.mdtoast.MDToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import dmax.dialog.SpotsDialog;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class LoginActivity extends AppCompatActivity {
    EditText et_email, et_password;
    AlertDialog waiting_dialog;

    @BindView(R.id.btn_login)
    Button btn_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        initView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // window.setStatusBarColor();
        }
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();
            }
        });
        et_email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    et_email.setBackground(getResources().getDrawable(R.drawable.edittext_shadow));
                    et_email.setHint(getResources().getString(R.string.email));
                    int paddingDp = 20;
                    float density = getResources().getDisplayMetrics().density;
                    int paddingPixel = (int) (paddingDp * density);
                    et_email.setPadding(paddingPixel, paddingPixel, paddingPixel, paddingPixel);

                    et_email.requestFocus();
                    et_password.setBackground(getResources().getDrawable(R.drawable.rectangle_background));


                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(et_email, InputMethodManager.SHOW_IMPLICIT);
                }
            }
        });

        et_password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    et_password.setHint(getResources().getString(R.string.password));
                    et_email.setBackground(getResources().getDrawable(R.drawable.rectangle_background));
                    et_password.setBackground(getResources().getDrawable(R.drawable.edittext_shadow));
                    int paddingDp = 20;
                    et_password.requestFocus();
                    float density = getResources().getDisplayMetrics().density;
                    int paddingPixel = (int) (paddingDp * density);
                    et_password.setPadding(paddingPixel, paddingPixel, paddingPixel, paddingPixel);

                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(et_password, InputMethodManager.SHOW_IMPLICIT);
                }
            }
        });


    }

    public void validate() {
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

        if (et_password.getText().toString().trim().length() == 0) {
            et_password.setError(getResources().getString(R.string.password_error));
            return;
        }

        Login();
    }

    public void Login() {

        try {
            waiting_dialog.show();
            RetrofitClient retrofitClient = new RetrofitClient();
            RestInterface service = retrofitClient.getAPIClient(WebServiceURLs.DOMAIN_NAME);
            service.LoginToApp(et_email.getText().toString(), et_password.getText().toString(), new Callback<JsonElement>() {
                @Override
                public void success(JsonElement jsonElement, Response response) {

                    try {

                        final JSONObject jsonObject = new JSONObject(jsonElement.toString());

                        String status = jsonObject.getString("status");
                        if (status.equals("1")) {
                            PrefManager.getInstance(LoginActivity.this).setIsLogin(true);
                            String messge = jsonObject.getString("messge");
                            JSONObject user_details = null;
                            if (jsonObject.has("user_details") && !jsonObject.has("vendor_details") )
                            {
                                 user_details=jsonObject.getJSONObject("user_details");
                                final String user_id=user_details.getString("user_id");
                                final String first_name=user_details.getString("first_name");
                                final String last_name=user_details.getString("last_name");
                                final String mobile_no=user_details.getString("mobile_no");
                                final String email=user_details.getString("email");
                                String password=user_details.getString("password");
                                String country=user_details.getString("country");
                                String state=user_details.getString("state");
                                String city=user_details.getString("city");
                                String pincode=user_details.getString("pincode");
                                String date_of_birth=user_details.getString("date_of_birth");
                                String security_question=user_details.getString("security_question");
                                String answer=user_details.getString("answer");
                                PrefManager.getInstance(LoginActivity.this).setUserId(user_id);
                                PrefManager.getInstance(LoginActivity.this).setFirstName(first_name);
                                PrefManager.getInstance(LoginActivity.this).setLastName(last_name);
                                PrefManager.getInstance(LoginActivity.this).setEmail(email);
                                PrefManager.getInstance(LoginActivity.this).setMobile(mobile_no);
                                PrefManager.getInstance(LoginActivity.this).setLoginAs("CUSTOMER");
                                startActivity(new Intent(LoginActivity.this,HomeActivity.class));
                                finish();
                            }



                            if (jsonObject.has("vendor_details") && !jsonObject.has("user_details"))
                            {
                                JSONObject vendor_details=jsonObject.getJSONObject("vendor_details");
                                final String user_id=vendor_details.getString("vendor_id");
                                final String first_name=vendor_details.getString("firstname");
                                final String last_name=vendor_details.getString("lastname");
                                final String mobile_no=vendor_details.getString("contactno");
                                final String email=vendor_details.getString("email");
                                String password=vendor_details.getString("password");
                                String country=vendor_details.getString("country");
                                String state=vendor_details.getString("state");
                                String city=vendor_details.getString("city");
                                String pincode=vendor_details.getString("pincode");
                                String reg_process_flag=vendor_details.getString("reg_process_flag");
                                PrefManager.getInstance(LoginActivity.this).setUserId(user_id);
                                PrefManager.getInstance(LoginActivity.this).setFirstName(first_name);
                                PrefManager.getInstance(LoginActivity.this).setLastName(last_name);
                                PrefManager.getInstance(LoginActivity.this).setEmail(email);
                                PrefManager.getInstance(LoginActivity.this).setMobile(mobile_no);
                                PrefManager.getInstance(LoginActivity.this).setLoginAs("VENDOR");
                                if (reg_process_flag.equals("1"))
                                {
                                    startActivity(new Intent(LoginActivity.this,VendorDetailsActivity.class));
                                    finish();
                                }
                                else
                                {
                                    startActivity(new Intent(LoginActivity.this,VendorHomeActivity.class));
                                    finish();
                                }

                            }


                            if (jsonObject.has("vendor_details") && jsonObject.has("user_details"))
                            {
                                final Dialog dialog=new Dialog(LoginActivity.this);
                                dialog.setContentView(R.layout.dailog_choose_role);
                                RadioButton rb_cust_login=dialog.findViewById(R.id.rb_cust_login);
                                RadioButton rb_vendor_login=dialog.findViewById(R.id.rb_vendor_login);

                                final JSONObject finalUser_details = user_details;
                                rb_cust_login.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        final String user_id;
                                        try {
                                            user_id = finalUser_details.getString("user_id");
                                            final String first_name= finalUser_details.getString("first_name");
                                            final String last_name= finalUser_details.getString("last_name");
                                            final String mobile_no= finalUser_details.getString("mobile_no");
                                            final String email= finalUser_details.getString("email");
                                            String password= finalUser_details.getString("password");
                                            String country= finalUser_details.getString("country");
                                            String state= finalUser_details.getString("state");
                                            String city= finalUser_details.getString("city");
                                            String pincode= finalUser_details.getString("pincode");
                                            String date_of_birth= finalUser_details.getString("date_of_birth");
                                            String security_question= finalUser_details.getString("security_question");
                                            String answer= finalUser_details.getString("answer");
                                            PrefManager.getInstance(LoginActivity.this).setUserId(user_id);
                                            PrefManager.getInstance(LoginActivity.this).setFirstName(first_name);
                                            PrefManager.getInstance(LoginActivity.this).setLastName(last_name);
                                            PrefManager.getInstance(LoginActivity.this).setEmail(email);
                                            PrefManager.getInstance(LoginActivity.this).setMobile(mobile_no);
                                            PrefManager.getInstance(LoginActivity.this).setLoginAs("CUSTOMER");
                                            startActivity(new Intent(LoginActivity.this,HomeActivity.class));
                                            finish();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                });

                                rb_vendor_login.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        try {
                                            JSONObject vendor_details=jsonObject.getJSONObject("vendor_details");
                                            final String user_id=vendor_details.getString("vendor_id");
                                            final String first_name=vendor_details.getString("firstname");
                                            final String last_name=vendor_details.getString("lastname");
                                            final String mobile_no=vendor_details.getString("contactno");
                                            final String email=vendor_details.getString("email");
                                            String password=vendor_details.getString("password");
                                            String country=vendor_details.getString("country");
                                            String state=vendor_details.getString("state");
                                            String city=vendor_details.getString("city");
                                            String pincode=vendor_details.getString("pincode");
                                            String reg_process_flag=vendor_details.getString("reg_process_flag");
                                            PrefManager.getInstance(LoginActivity.this).setUserId(user_id);
                                            PrefManager.getInstance(LoginActivity.this).setFirstName(first_name);
                                            PrefManager.getInstance(LoginActivity.this).setLastName(last_name);
                                            PrefManager.getInstance(LoginActivity.this).setEmail(email);
                                            PrefManager.getInstance(LoginActivity.this).setMobile(mobile_no);
                                            PrefManager.getInstance(LoginActivity.this).setLoginAs("VENDOR");

                                            if (reg_process_flag.equals("1"))
                                            {
                                                startActivity(new Intent(LoginActivity.this,VendorDetailsActivity.class));
                                                finish();
                                            }
                                            else
                                            {
                                                startActivity(new Intent(LoginActivity.this,VendorHomeActivity.class));
                                                finish();
                                            }


                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                });
                                dialog.show();
                            }







                        } else {

                        }


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
                    CustomUtils.showToast(getResources().getString(R.string.check_internet), LoginActivity.this, MDToast.TYPE_WARNING);
                }
            });
        } catch (Exception e) {
            waiting_dialog.dismiss();
            e.printStackTrace();

        }

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

    public void initView() {
        waiting_dialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage(R.string.loading)
                .setCancelable(false)

                .build();
        et_password = findViewById(R.id.et_password);
        et_email = findViewById(R.id.et_email);
    }
}
