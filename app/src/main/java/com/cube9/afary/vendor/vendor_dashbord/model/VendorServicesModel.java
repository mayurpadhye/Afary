package com.cube9.afary.vendor.vendor_dashbord.model;

import com.cube9.afary.network.RestInterface;
import com.cube9.afary.network.RetrofitClient;
import com.cube9.afary.network.WebServiceURLs;
import com.cube9.afary.vendor.model.IVendorSignUpModel;
import com.cube9.afary.vendor.model.SkillServicesPojo;
import com.google.gson.JsonElement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class VendorServicesModel implements IVendorServiceModel {
List<VendorServicesModel> vendorServicesModelList=new ArrayList<>();

String service_name="",service_id="";

public VendorServicesModel()
{

}

    public VendorServicesModel(String service_name, String service_id) {

        this.service_name = service_name;
        this.service_id = service_id;
    }

    public String getService_name() {
        return service_name;
    }

    public void setService_name(String service_name) {
        this.service_name = service_name;
    }

    public String getService_id() {
        return service_id;
    }

    public void setService_id(String service_id) {
        this.service_id = service_id;
    }

    @Override
    public void getVendorServices(final GetVendorServicesInterface getVendorServicesInterface,String vendor_id) {

        RetrofitClient retrofitClient = new RetrofitClient();
        RestInterface service = retrofitClient.getAPIClient(WebServiceURLs.DOMAIN_NAME);
        service.get_vendor_services(vendor_id,new Callback<JsonElement>() {
            @Override
            public void success(JsonElement jsonElement, Response response) {

                try {

                    JSONObject jsonObject = new JSONObject(jsonElement.toString());

                    String status = jsonObject.getString("status");
                    if (status.equals("1")) {
                        vendorServicesModelList.clear();
                        JSONArray services_list=jsonObject.getJSONArray("services_list");

                        for (int i=0;i<services_list.length();i++)
                        {
                            JSONObject j1=services_list.getJSONObject(i);
                            String service_name=j1.getString("service_name");
                            String service_id=j1.getString("service_id");

                            vendorServicesModelList.add(new VendorServicesModel(service_name,service_id));
                        }
                        getVendorServicesInterface.onResultFinished(vendorServicesModelList);


                    } else {
                        getVendorServicesInterface.onResultFinished(vendorServicesModelList);
                    }



                } catch (JSONException | NullPointerException e) {

                    e.printStackTrace();

                }
            }

            @Override
            public void failure(RetrofitError error) {
                getVendorServicesInterface.onFailure(error);
            }
        });

    }
}
