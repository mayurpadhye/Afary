package com.cube9.afary.user.signup;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.cube9.afary.R;
import com.cube9.afary.helperClass.CustomUtils;
import com.cube9.afary.login.LoginActivity;
import com.cube9.afary.network.RestInterface;
import com.cube9.afary.network.RetrofitClient;
import com.cube9.afary.network.WebServiceURLs;
import com.cube9.afary.user.UserSignUpActivity;
import com.cube9.afary.user.home.HomeActivity;
import com.google.gson.JsonElement;
import com.valdesekamdem.library.mdtoast.MDToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import dmax.dialog.SpotsDialog;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class CompleteRegistrationActivity extends AppCompatActivity {

    @BindView(R.id.ll_city)
    LinearLayout ll_city;
    @BindView(R.id.ll_security)
    LinearLayout ll_security;
    @BindView(R.id.et_date)
    EditText et_date;
    @BindView(R.id.btn_register)
    Button btn_register;

    @BindView(R.id.et_security_ans)
    EditText et_security_ans;
    @BindView(R.id.sp_city)
    Spinner sp_city;
    @BindView(R.id.sp_security_questions)
    Spinner sp_security_questions;

    @BindView(R.id.ll_state)
    LinearLayout ll_state;

    @BindView(R.id.sp_state)
    Spinner sp_state;
    AlertDialog waiting_dialog;
    String country_code = "";
    String f_name = "", last_name = "", mobile = "", email = "", password, country_name = "";
    int year, month, day;
    static final int DATE_PICKER_ID = 0;

    List<String> list_state = new ArrayList<>();
    List<String> list_state_id = new ArrayList<>();

    List<String> list_city_name = new ArrayList<>();
    List<String> list_city_id = new ArrayList<>();
    String state_code = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_registration);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // window.setStatusBarColor();
        }
        ButterKnife.bind(this);
        onClick();
        waiting_dialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage(R.string.loading)
                .setCancelable(false)

                .build();
        f_name = getIntent().getStringExtra("f_name");
        last_name = getIntent().getStringExtra("l_name");
        mobile = getIntent().getStringExtra("mobile_no");
        email = getIntent().getStringExtra("email");
        password = getIntent().getStringExtra("password");
        country_name = getIntent().getStringExtra("country");
        country_code = getIntent().getStringExtra("country_code");


        getState();

        ArrayAdapter<String> spinnerArrayAdapter1 = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item,
                        getResources().getStringArray(R.array.select_security_questions)); //selected item will look like a spinner set from XML
        spinnerArrayAdapter1.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);
        sp_security_questions.setAdapter(spinnerArrayAdapter1);


        sp_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                if (list_state.get(position).equals(getResources().getString(R.string.select_state))) {
                    // country_id="";
                } else {
                    getCity(list_state_id.get(position));

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

    }


    public void getState() {
        try {
            waiting_dialog.show();
            RetrofitClient retrofitClient = new RetrofitClient();
            RestInterface service = retrofitClient.getAPIClient(WebServiceURLs.DOMAIN_NAME);
            service.getState(country_code, new Callback<JsonElement>() {
                @Override
                public void success(JsonElement jsonElement, Response response) {

                    try {
                        list_state.clear();
                        list_state_id.clear();
                        JSONObject jsonObject = new JSONObject(jsonElement.toString());

                        String status = jsonObject.getString("status");
                        if (status.equals("1")) {
                            JSONArray states_list = jsonObject.getJSONArray("states_list");
                            for (int i = 0; i < states_list.length(); i++) {
                                JSONObject j1 = states_list.getJSONObject(i);
                                String id = j1.getString("id");
                                String name = j1.getString("name");
                                list_state.add(name);
                                list_state_id.add(id);
                            }
                            list_state.add(0, getResources().getString(R.string.select_state));
                            list_state_id.add(0, "select_id");
                            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                                    (CompleteRegistrationActivity.this, android.R.layout.simple_spinner_item,
                                            list_state); //selected item will look like a spinner set from XML
                            spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                                    .simple_spinner_dropdown_item);
                            sp_state.setAdapter(spinnerArrayAdapter);
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
                    CustomUtils.showToast(getResources().getString(R.string.check_internet), CompleteRegistrationActivity.this, MDToast.TYPE_WARNING);
                }
            });
        } catch (Exception e) {
            waiting_dialog.dismiss();
            e.printStackTrace();

        }
    }


    public void getCity(String state_id) {
        try {
            waiting_dialog.show();
            RetrofitClient retrofitClient = new RetrofitClient();
            RestInterface service = retrofitClient.getAPIClient(WebServiceURLs.DOMAIN_NAME);
            service.getCity(state_id, new Callback<JsonElement>() {
                @Override
                public void success(JsonElement jsonElement, Response response) {

                    try {
                        list_city_name.clear();
                        list_city_id.clear();
                        JSONObject jsonObject = new JSONObject(jsonElement.toString());

                        String status = jsonObject.getString("status");
                        if (status.equals("1")) {
                            JSONArray cities = jsonObject.getJSONArray("cities");
                            for (int i = 0; i < cities.length(); i++) {
                                JSONObject j1 = cities.getJSONObject(i);
                                String id = j1.getString("id");
                                String name = j1.getString("name");
                                list_city_name.add(name);
                                list_city_id.add(id);
                            }
                            list_city_name.add(0, getResources().getString(R.string.select_city));
                            list_city_id.add(0, "select_id");
                            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                                    (CompleteRegistrationActivity.this, android.R.layout.simple_spinner_item,
                                            list_city_name); //selected item will look like a spinner set from XML
                            spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                                    .simple_spinner_dropdown_item);
                            sp_city.setAdapter(spinnerArrayAdapter);
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
                    CustomUtils.showToast(getResources().getString(R.string.check_internet), CompleteRegistrationActivity.this, MDToast.TYPE_WARNING);
                }
            });
        } catch (Exception e) {
            waiting_dialog.dismiss();
            e.printStackTrace();

        }
    }


    public void validation() {
        if (sp_state.getSelectedItem().toString().equals(getResources().getString(R.string.select_state))) {
            CustomUtils.showToast(getResources().getString(R.string.please_select_state), CompleteRegistrationActivity.this, MDToast.TYPE_ERROR);
            return;
        } else if (sp_state.getSelectedItem().toString().equals(getResources().getString(R.string.select_city))) {
            CustomUtils.showToast(getResources().getString(R.string.please_select_city), CompleteRegistrationActivity.this, MDToast.TYPE_ERROR);
            return;
        } else if (et_date.getText().toString().trim().length() == 0) {
            et_date.requestFocus();
            et_date.setError(getResources().getString(R.string.select_date_of_birth));
            return;
        } else if (sp_security_questions.getSelectedItem().toString().equals("Select your security question")) {
            CustomUtils.showToast(getResources().getString(R.string.please_select_security_question), CompleteRegistrationActivity.this, MDToast.TYPE_ERROR);
            return;
        } else if (et_security_ans.getText().toString().trim().length() == 0) {
            et_security_ans.setError(getResources().getString(R.string.select_security_ans));
            return;
        }

        Register();


    }


    public void Register() {
        try {
            waiting_dialog.show();
            RetrofitClient retrofitClient = new RetrofitClient();
            RestInterface service = retrofitClient.getAPIClient(WebServiceURLs.DOMAIN_NAME);
            service.register_user(f_name,last_name,mobile,email,country_name,sp_state.getSelectedItem().toString(),sp_city.getSelectedItem().toString(),"1345",et_date.getText().toString(),sp_security_questions.getSelectedItem().toString(),et_security_ans.getText().toString(),password, new Callback<JsonElement>() {
                @Override
                public void success(JsonElement jsonElement, Response response) {

                    try {

                        JSONObject jsonObject = new JSONObject(jsonElement.toString());

                        String status = jsonObject.getString("status");
                        if (status.equals("1")) {
                            String messge=jsonObject.getString("messge");

                            CustomUtils.showToast(messge+" "+getResources().getString(R.string.login_to_continue),CompleteRegistrationActivity.this,MDToast.TYPE_SUCCESS);
                            startActivity(new Intent(CompleteRegistrationActivity.this,LoginActivity.class));
                            finish();

                        }
                        else
                        {
                            CustomUtils.showToast(""+getResources().getString(R.string.unable_to_register),CompleteRegistrationActivity.this,MDToast.TYPE_SUCCESS);

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
                    CustomUtils.showToast(getResources().getString(R.string.check_internet), CompleteRegistrationActivity.this, MDToast.TYPE_WARNING);
                }
            });
        } catch (Exception e) {
            waiting_dialog.dismiss();
            e.printStackTrace();

        }
    }


    private void SelectDate() {

        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);


        showDialog(DATE_PICKER_ID);

    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_PICKER_ID:

                // open datepicker dialog.
                // set date picker for current date
                // add pickerListener listner to date picker
                return new DatePickerDialog(this, pickerListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        @Override
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {

            year = selectedYear;
            month = selectedMonth;
            day = selectedDay;

            // Show selected date
            et_date.setText(new StringBuilder().append(month + 1)
                    .append("-").append(day).append("-").append(year)
                    .append(" "));

        }
    };

    public void onClick() {
/*et_date.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

    }
});*/

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(CompleteRegistrationActivity.this,HomeActivity.class));
                validation();
            }
        });
        sp_city.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ll_city.setBackground(getResources().getDrawable(R.drawable.edittext_shadow));

                int paddingDp = 20;
                float density = getResources().getDisplayMetrics().density;
                int paddingPixel = (int) (paddingDp * density);
                // et_confirm_password.setPadding(paddingPixel,paddingPixel,paddingPixel,paddingPixel);

                ll_city.requestFocus();
                et_date.setBackground(getResources().getDrawable(R.drawable.rectangle_background));
                ll_security.setBackground(getResources().getDrawable(R.drawable.rectangle_background));
                et_security_ans.setBackground(getResources().getDrawable(R.drawable.rectangle_background));

                return false;
            }


        });

        et_date.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    et_date.setBackground(getResources().getDrawable(R.drawable.edittext_shadow));
                    SelectDate();
                    int paddingDp = 20;
                    float density = getResources().getDisplayMetrics().density;
                    int paddingPixel = (int) (paddingDp * density);
                    et_date.setPadding(paddingPixel, paddingPixel, paddingPixel, paddingPixel);

                    et_date.requestFocus();
                    ll_city.setBackground(getResources().getDrawable(R.drawable.rectangle_background));
                    ll_security.setBackground(getResources().getDrawable(R.drawable.rectangle_background));
                    et_security_ans.setBackground(getResources().getDrawable(R.drawable.rectangle_background));

                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(et_date, InputMethodManager.SHOW_IMPLICIT);
                }
            }
        });

        sp_security_questions.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ll_security.setBackground(getResources().getDrawable(R.drawable.edittext_shadow));

                int paddingDp = 20;
                float density = getResources().getDisplayMetrics().density;
                int paddingPixel = (int) (paddingDp * density);
                // et_confirm_password.setPadding(paddingPixel,paddingPixel,paddingPixel,paddingPixel);

                ll_security.requestFocus();
                et_date.setBackground(getResources().getDrawable(R.drawable.rectangle_background));
                ll_city.setBackground(getResources().getDrawable(R.drawable.rectangle_background));
                et_security_ans.setBackground(getResources().getDrawable(R.drawable.rectangle_background));

                return false;
            }


        });
        et_security_ans.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    et_security_ans.setBackground(getResources().getDrawable(R.drawable.edittext_shadow));

                    int paddingDp = 20;
                    float density = getResources().getDisplayMetrics().density;
                    int paddingPixel = (int) (paddingDp * density);
                    et_security_ans.setPadding(paddingPixel, paddingPixel, paddingPixel, paddingPixel);

                    et_security_ans.requestFocus();
                    ll_city.setBackground(getResources().getDrawable(R.drawable.rectangle_background));
                    ll_security.setBackground(getResources().getDrawable(R.drawable.rectangle_background));
                    et_date.setBackground(getResources().getDrawable(R.drawable.rectangle_background));

                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(et_security_ans, InputMethodManager.SHOW_IMPLICIT);
                }
            }
        });
    }

}
