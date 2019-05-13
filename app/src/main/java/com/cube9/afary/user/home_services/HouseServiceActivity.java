package com.cube9.afary.user.home_services;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cube9.afary.R;
import com.cube9.afary.helperClass.CustomUtils;
import com.cube9.afary.helperClass.PrefManager;
import com.cube9.afary.network.RestInterface;
import com.cube9.afary.network.RetrofitClient;
import com.cube9.afary.network.WebServiceURLs;
import com.cube9.afary.user.UserSignUpActivity;
import com.cube9.afary.user.home.HomeActivity;
import com.cube9.afary.user.payment.PaymentActivity;
import com.google.gson.JsonElement;
import com.valdesekamdem.library.mdtoast.MDToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dmax.dialog.SpotsDialog;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class HouseServiceActivity extends AppCompatActivity {
    AlertDialog waiting_dialog;
    @BindView(R.id.sp_house_category)
    Spinner sp_house_category;
    @BindView(R.id.sp_no_rooms)
    Spinner sp_no_rooms;
    @BindView(R.id.sp_area)
    Spinner sp_area;
    @BindView(R.id.sp_budget)
    Spinner sp_budget;
    @BindView(R.id.sp_immediate)
    Spinner sp_immediate;
    @BindView(R.id.et_state)
    EditText et_state;
    @BindView(R.id.et_city)
    EditText et_city;
    @BindView(R.id.et_pincode)
    EditText et_pincode;
    @BindView(R.id.btn_confirm)
    Button btn_confirm;
    @BindView(R.id.rg_type)
    RadioGroup rg_type;
String type="";

@BindView(R.id.tv_location)
    TextView tv_location;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_service);
        ButterKnife.bind(this);
        initView();
        tv_location.setText(""+PrefManager.getInstance(HouseServiceActivity.this).getLocation());

    }

    private void initView() {
        waiting_dialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage(R.string.loading)
                .setCancelable(false)

                .build();
        rg_type.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                // This will get the radiobutton that has changed in its check state
                RadioButton checkedRadioButton = (RadioButton)group.findViewById(checkedId);
                // This puts the value (true/false) into the variable
                boolean isChecked = checkedRadioButton.isChecked();
                // If the radiobutton that has changed in check state is now checked...
                if (isChecked)
                {
                    // Changes the textview's text to "Checked: example radiobutton text"

                    Toast.makeText(HouseServiceActivity.this, ""+checkedRadioButton.getText(), Toast.LENGTH_SHORT).show();
                    if (checkedRadioButton.getText().equals("rent"))
                    {
                        type="1";
                    }
                    else{
                        type="2";
                    }

                }
            }
        });
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                (HouseServiceActivity.this, android.R.layout.simple_spinner_item,
                        getResources().getStringArray(R.array.house_category)); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);
        sp_house_category.setAdapter(spinnerArrayAdapter);

        ArrayAdapter<String> spinnerArrayAdapter1 = new ArrayAdapter<String>
                (HouseServiceActivity.this, android.R.layout.simple_spinner_item,
                        getResources().getStringArray(R.array.number_of_rooms)); //selected item will look like a spinner set from XML
        spinnerArrayAdapter1.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);
        sp_no_rooms.setAdapter(spinnerArrayAdapter1);


        ArrayAdapter<String> spinnerArrayAdapter2 = new ArrayAdapter<String>
                (HouseServiceActivity.this, android.R.layout.simple_spinner_item,
                        getResources().getStringArray(R.array.budget)); //selected item will look like a spinner set from XML
        spinnerArrayAdapter2.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);
        sp_budget.setAdapter(spinnerArrayAdapter2);

        ArrayAdapter<String> spinnerArrayAdapter3 = new ArrayAdapter<String>
                (HouseServiceActivity.this, android.R.layout.simple_spinner_item,
                        getResources().getStringArray(R.array.immediate_required)); //selected item will look like a spinner set from XML
        spinnerArrayAdapter3.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);
        sp_immediate.setAdapter(spinnerArrayAdapter3);

        ArrayAdapter<String> spinnerArrayAdapter4 = new ArrayAdapter<String>
                (HouseServiceActivity.this, android.R.layout.simple_spinner_item,
                        getResources().getStringArray(R.array.area_sq_meter)); //selected item will look like a spinner set from XML
        spinnerArrayAdapter4.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);
        sp_area.setAdapter(spinnerArrayAdapter4);
    }

    @OnClick(R.id.btn_confirm)
    public void submitData()
    {
        if (type.equals(""))
        {
            CustomUtils.showToast(getResources().getString(R.string.select_service_type),HouseServiceActivity.this,MDToast.TYPE_ERROR);
            return;
        }

        if (type.equals(""))
        {
            CustomUtils.showToast(getResources().getString(R.string.select_service_type),HouseServiceActivity.this,MDToast.TYPE_ERROR);
            return;
        }
        else if (sp_house_category.getSelectedItem().toString().equals("Select House Category"))
        {
            CustomUtils.showToast(getResources().getString(R.string.select_house_cat),HouseServiceActivity.this,MDToast.TYPE_ERROR);
            return;
        }
        else if (sp_no_rooms.getSelectedItem().toString().equals("Select Number of Rooms"))
        {
            CustomUtils.showToast(getResources().getString(R.string.select_rooms),HouseServiceActivity.this,MDToast.TYPE_ERROR);
            return;
        }

        else if (sp_area.getSelectedItem().toString().equals("Select Area"))
        {
            CustomUtils.showToast(getResources().getString(R.string.select_area),HouseServiceActivity.this,MDToast.TYPE_ERROR);
            return;
        }

        else if (sp_budget.getSelectedItem().toString().equals("Select Budget"))
        {
            CustomUtils.showToast(getResources().getString(R.string.select_budget),HouseServiceActivity.this,MDToast.TYPE_ERROR);
            return;
        }

        else if (sp_immediate.getSelectedItem().toString().equals("Immediate Required"))
        {
            CustomUtils.showToast(getResources().getString(R.string.select_requrment),HouseServiceActivity.this,MDToast.TYPE_ERROR);
            return;
        }

        else if (et_state.getText().toString().trim().length()==0)
        {
            et_state.requestFocus();
            et_state.setError(""+getResources().getString(R.string.enter_state));
            return;
        }
        else if (et_city.getText().toString().trim().length()==0)
        {
            et_state.setError("");
            et_city.requestFocus();
            et_city.setError(""+getResources().getString(R.string.enter_city));
            return;
        }
        else if (et_pincode.getText().toString().trim().length()==0)
        {
            et_city.setError("");
            et_pincode.requestFocus();
            et_pincode.setError(""+getResources().getString(R.string.enter_pincode));
            return;
        }
        submitHouseRequest();
    }


    public void submitHouseRequest()
    {
        try {
            waiting_dialog.show();
            RetrofitClient retrofitClient = new RetrofitClient();
            RestInterface service = retrofitClient.getAPIClient(WebServiceURLs.DOMAIN_NAME);
            service.SubmitHouseRequest(PrefManager.getInstance(HouseServiceActivity.this).getUserId(),type,sp_no_rooms.getSelectedItem().toString(),sp_budget.getSelectedItem().toString(),sp_area.getSelectedItem().toString(),et_state.getText().toString(),et_city.getText().toString(),et_pincode.getText().toString(),sp_house_category.getSelectedItem().toString(),"1" ,new Callback<JsonElement>() {
                @Override
                public void success(JsonElement jsonElement, Response response) {

                    try {

                        JSONObject jsonObject = new JSONObject(jsonElement.toString());
                        String status=jsonObject.getString("status");

                        if (status.equals("1"))
                        {
                            CustomUtils.showToast("Successfully submitted",HouseServiceActivity.this,MDToast.TYPE_INFO);
                            startActivity(new Intent(HouseServiceActivity.this,PaymentActivity.class));
                            finish();
                        }
                        else
                        {
                            CustomUtils.showToast("Unable to submit the data",HouseServiceActivity.this,MDToast.TYPE_ERROR);
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
                    CustomUtils.showToast(getResources().getString(R.string.check_internet), HouseServiceActivity.this, MDToast.TYPE_WARNING);
                }
            });
        } catch (Exception e) {
            waiting_dialog.dismiss();
            e.printStackTrace();

        }
    }

}
