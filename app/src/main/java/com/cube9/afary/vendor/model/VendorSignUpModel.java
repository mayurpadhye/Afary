package com.cube9.afary.vendor.model;

import android.widget.ArrayAdapter;

import com.cube9.afary.R;
import com.cube9.afary.helperClass.CustomUtils;
import com.cube9.afary.network.RestInterface;
import com.cube9.afary.network.RetrofitClient;
import com.cube9.afary.network.WebServiceURLs;
import com.cube9.afary.user.UserSignUpActivity;
import com.google.gson.JsonElement;
import com.valdesekamdem.library.mdtoast.MDToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class VendorSignUpModel implements IVendorSignUpModel {

List<String> listCountryName=new ArrayList<>();
List<String> listCountryId=new ArrayList<>();
    @Override
    public void getCountryList(final getCountryListInterface getCountryListInterface) {

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

                    getCountryListInterface.onFinished(listCountryName,listCountryId);




                } catch (JSONException | NullPointerException e) {

                    e.printStackTrace();

                }
            }

            @Override
            public void failure(RetrofitError error) {

                // Toast.makeText(UserSignUpActivity.this ,getResources().getString(R.string.check_internet), Toast.LENGTH_LONG ).show();
                //CustomUtils.showToast(getResources().getString(R.string.check_internet), UserSignUpActivity.this, MDToast.TYPE_WARNING);
            }
        });

    }
}
