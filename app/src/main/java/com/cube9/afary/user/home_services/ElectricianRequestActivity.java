package com.cube9.afary.user.home_services;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.cube9.afary.R;
import com.cube9.afary.helperClass.CustomUtils;
import com.cube9.afary.helperClass.PlacesAutoCompleteAdapter;
import com.cube9.afary.helperClass.PrefManager;
import com.cube9.afary.login.LoginActivity;
import com.cube9.afary.network.RestInterface;
import com.cube9.afary.network.RetrofitClient;
import com.cube9.afary.network.WebServiceURLs;
import com.cube9.afary.search_address.SearchAddressActivity;
import com.cube9.afary.user.home.HomeActivity;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.gson.JsonElement;
import com.valdesekamdem.library.mdtoast.MDToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dmax.dialog.SpotsDialog;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ElectricianRequestActivity extends AppCompatActivity {

    @BindView(R.id.iv_back)
    ImageView iv_back;

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.et_address)
    AutoCompleteTextView et_address;
    @BindView(R.id.et_date)
    EditText et_date;
    @BindView(R.id.et_time)
    EditText et_time;
    @BindView(R.id.et_amount)
    EditText et_amount;
    @BindView(R.id.et_desc)
    EditText et_desc;

    @BindView(R.id.btn_submit)
    Button btn_submit;

    @BindView(R.id.et_house)
    EditText et_house;

    @BindView(R.id.et_street)
    EditText et_street;
    @BindView(R.id.tv_service_name)
    TextView tv_service_name;

    Calendar myCalendar;
    String lat = "", lan = "";
    AlertDialog waiting_dialog;
    String intent_to="";
String service_name="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_electrician_request);
        ButterKnife.bind(this);
        intent_to=getIntent().getStringExtra("intent_to");
        service_name=getIntent().getStringExtra("service_name");
        tv_title.setText(service_name);
        tv_service_name.setText(service_name);

         iv_back.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 finish();
             }
         });


        waiting_dialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage(R.string.loading)
                .setCancelable(false)
                .build();
        myCalendar = Calendar.getInstance();
        et_address.setAdapter(new PlacesAutoCompleteAdapter(ElectricianRequestActivity.this, R.layout.autocomplete_list_item));
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        et_date.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(ElectricianRequestActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        et_time.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(ElectricianRequestActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        et_time.setText(selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });


        et_address.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get data associated with the specified position
                // in the list (AdapterView)
                String description = (String) parent.getItemAtPosition(position);
                //  Toast.makeText(ElectricianRequestActivity.this, description, Toast.LENGTH_SHORT).show();
                getLocationFromAddress(description);
            }
        });
        et_address.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Toast.makeText(ElectricianRequestActivity.this, ""+getLocationFromAddress(s.toString()), Toast.LENGTH_SHORT).show();;


            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //getLocationFromAddress(s.toString());
            }
        });
    }

    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        et_date.setText(sdf.format(myCalendar.getTime()));
    }

    @OnClick(R.id.btn_submit)
    public void onSubmitClick() {

        if (et_address.getText().toString().trim().length() == 0) {
            et_address.requestFocus();
            et_address.setError("" + getResources().getString(R.string.enter_address));
            return;
        }

        if (et_house.getText().toString().trim().length() == 0) {
            et_house.requestFocus();
            et_house.setError("" + getResources().getString(R.string.enter_house));
            return;
        }
        if (et_street.getText().toString().trim().length() == 0) {
            et_street.requestFocus();
            et_street.setError("" + getResources().getString(R.string.enter_street));
            return;
        } else if (et_date.getText().toString().trim().length() == 0) {
            et_date.requestFocus();
            et_date.setError(getResources().getString(R.string.select_date));
            return;
        } else if (et_time.getText().toString().trim().length() == 0) {
            et_time.requestFocus();
            et_time.setError(getResources().getString(R.string.setlect_time));
            return;
        } else if (et_amount.getText().toString().trim().length() == 0) {
            et_amount.requestFocus();
            et_amount.setError(getResources().getString(R.string.enter_amount));
            return;
        } else if (et_desc.getText().toString().trim().length() == 0) {
            et_desc.requestFocus();
            et_desc.setError(getResources().getString(R.string.enter_desc));
            return;
        }
        if (intent_to.equals("electrician"))
        submitForm();
        else
            submitPlumberForm();
    }

    @SuppressLint("RestrictedApi")
    public Barcode.GeoPoint getLocationFromAddress(String strAddress) {

        Geocoder coder = new Geocoder(this);
        List<Address> address;
        Barcode.GeoPoint p1 = null;

        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }


            Log.i("addresssssssss", "" + address.toString());
            if (address.size() > 0) {
                Address location = address.get(0);
                location.getLatitude();
                location.getLongitude();
                lat = "" + location.getLatitude();
                lan = "" + location.getLongitude();
                //Toast.makeText(this, ""+location.getLatitude(), Toast.LENGTH_SHORT).show();
                p1 = new Barcode.GeoPoint((double) (location.getLatitude() * 1E6),
                        (double) (location.getLongitude() * 1E6));
            }
            /**/


        } catch (IOException e) {
            e.printStackTrace();
        }
        return p1;
    }


    public void submitForm() {
        try {
            waiting_dialog.show();
            RetrofitClient retrofitClient = new RetrofitClient();
            RestInterface service = retrofitClient.getAPIClient(WebServiceURLs.DOMAIN_NAME);
            service.electrication_request(PrefManager.getInstance(ElectricianRequestActivity.this).getUserId(), lat, lan, et_address.getText().toString(), et_date.getText().toString(), et_time.getText().toString(), et_amount.getText().toString(), et_desc.getText().toString(), new Callback<JsonElement>() {
                @Override
                public void success(JsonElement jsonElement, Response response) {

                    try {

                        JSONObject jsonObject = new JSONObject(jsonElement.toString());

                        String status = jsonObject.getString("status");
                        if (status.equals("1")) {
                            CustomUtils.showToast(jsonObject.getString("messge"), ElectricianRequestActivity.this, MDToast.TYPE_INFO);
                            et_address.setText("");
                            et_date.setText("");
                            et_time.setText("");
                            et_amount.setText("");
                            et_desc.setText("");
                            et_house.setText("");
                            et_street.setText("");

                        } else {
                            CustomUtils.showToast(jsonObject.getString("messge"), ElectricianRequestActivity.this, MDToast.TYPE_INFO);

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
                    CustomUtils.showToast(getResources().getString(R.string.check_internet), ElectricianRequestActivity.this, MDToast.TYPE_WARNING);
                }
            });
        } catch (Exception e) {
            waiting_dialog.dismiss();
            e.printStackTrace();

        }

    }//subMitFormClose

    public void submitPlumberForm() {
        try {
            waiting_dialog.show();
            RetrofitClient retrofitClient = new RetrofitClient();
            RestInterface service = retrofitClient.getAPIClient(WebServiceURLs.DOMAIN_NAME);
            service.plumber_request(PrefManager.getInstance(ElectricianRequestActivity.this).getUserId(), lat, lan, et_address.getText().toString(), et_date.getText().toString(), et_time.getText().toString(), et_amount.getText().toString(), et_desc.getText().toString(), new Callback<JsonElement>() {
                @Override
                public void success(JsonElement jsonElement, Response response) {

                    try {

                        JSONObject jsonObject = new JSONObject(jsonElement.toString());

                        String status = jsonObject.getString("status");
                        if (status.equals("1")) {
                            CustomUtils.showToast(jsonObject.getString("messge"), ElectricianRequestActivity.this, MDToast.TYPE_INFO);
                            et_address.setText("");
                            et_date.setText("");
                            et_time.setText("");
                            et_amount.setText("");
                            et_desc.setText("");
                            et_house.setText("");
                            et_street.setText("");

                        } else {
                            CustomUtils.showToast(jsonObject.getString("messge"), ElectricianRequestActivity.this, MDToast.TYPE_INFO);

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
                    CustomUtils.showToast(getResources().getString(R.string.check_internet), ElectricianRequestActivity.this, MDToast.TYPE_WARNING);
                }
            });
        } catch (Exception e) {
            waiting_dialog.dismiss();
            e.printStackTrace();

        }

    }//subMitFormClose


}
