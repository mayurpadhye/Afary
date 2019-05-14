package com.cube9.afary.vendor.model;

import com.cube9.afary.R;
import com.cube9.afary.helperClass.CustomUtils;
import com.cube9.afary.network.RestInterface;
import com.cube9.afary.network.RetrofitClient;
import com.cube9.afary.network.WebServiceURLs;
import com.cube9.afary.user.home_services.HomeServiceActivity;
import com.cube9.afary.user.home_services.adapter.HomeServicesAdapter;
import com.cube9.afary.user.home_services.helperClass.HomeServicesPojo;
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

public class SkillServicesPojo implements IVendorSignUpModel.getSkillServicesInterface {
    String service_name,service_id,service_image,serives_availability,description;
List<SkillServicesPojo> homeServicesPojoList=new ArrayList<>();
    public SkillServicesPojo() {

    }

    public SkillServicesPojo(String service_name, String service_id, String service_image, String serives_availability, String description) {
        this.service_name = service_name;
        this.service_id = service_id;
        this.service_image = service_image;
        this.serives_availability = serives_availability;
        this.description = description;
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

    public String getService_image() {
        return service_image;
    }

    public void setService_image(String service_image) {
        this.service_image = service_image;
    }

    public String getSerives_availability() {
        return serives_availability;
    }

    public void setSerives_availability(String serives_availability) {
        this.serives_availability = serives_availability;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    @Override
    public void getSkillList(final getSkills getSkills) {


        RetrofitClient retrofitClient = new RetrofitClient();
        RestInterface service = retrofitClient.getAPIClient(WebServiceURLs.DOMAIN_NAME);
        service.getHomeServices("2",new Callback<JsonElement>() {
            @Override
            public void success(JsonElement jsonElement, Response response) {

                try {

                    JSONObject jsonObject = new JSONObject(jsonElement.toString());

                    String status = jsonObject.getString("status");
                    if (status.equals("1")) {
                        homeServicesPojoList.clear();
                        JSONArray servicelist=jsonObject.getJSONArray("servicelist");

                        for (int i=0;i<servicelist.length();i++)
                        {
                            JSONObject j1=servicelist.getJSONObject(i);
                            String service_id=j1.getString("service_id");
                            String service_name=j1.getString("service_name");
                            String service_description=j1.getString("service_description");
                            String cat_id=j1.getString("cat_id");
                            String status1=j1.getString("status");
                            // String image_url=j1.getString("image_url");
                            String created_date=j1.getString("created_date");
                            homeServicesPojoList.add(new SkillServicesPojo(service_name,service_id,"",service_description,service_description));
                        }
                       getSkills.onFinished(homeServicesPojoList);


                    } else {
                        getSkills.onFinished(homeServicesPojoList);
                    }



                } catch (JSONException | NullPointerException e) {

                    e.printStackTrace();

                }
            }

            @Override
            public void failure(RetrofitError error) {
              getSkills.onFailure(error);
            }
        });
    }
}
