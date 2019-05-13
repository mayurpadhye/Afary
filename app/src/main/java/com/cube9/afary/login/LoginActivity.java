package com.cube9.afary.login;

import android.app.AlertDialog;
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

import com.cube9.afary.R;
import com.cube9.afary.helperClass.CustomUtils;
import com.cube9.afary.helperClass.PrefManager;
import com.cube9.afary.network.RestInterface;
import com.cube9.afary.network.RetrofitClient;
import com.cube9.afary.network.WebServiceURLs;
import com.cube9.afary.user.home.HomeActivity;
import com.cube9.afary.user.signup.CompleteRegistrationActivity;
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

                        JSONObject jsonObject = new JSONObject(jsonElement.toString());

                        String status = jsonObject.getString("status");
                        if (status.equals("1")) {
                            PrefManager.getInstance(LoginActivity.this).setIsLogin(true);
                            String messge = jsonObject.getString("messge");
                            JSONObject user_details=jsonObject.getJSONObject("user_details");
                            String user_id=user_details.getString("user_id");
                            String first_name=user_details.getString("first_name");
                            String last_name=user_details.getString("last_name");
                            String mobile_no=user_details.getString("mobile_no");
                            String email=user_details.getString("email");
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
                            startActivity(new Intent(LoginActivity.this,HomeActivity.class));
                            finish();


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
