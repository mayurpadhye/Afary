package com.cube9.afary.user.home_services;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.cube9.afary.R;
import com.cube9.afary.helperClass.CustomUtils;
import com.cube9.afary.helperClass.PrefManager;
import com.cube9.afary.login.LoginActivity;
import com.cube9.afary.network.RestInterface;
import com.cube9.afary.network.RetrofitClient;
import com.cube9.afary.network.WebServiceURLs;
import com.cube9.afary.user.home.HomeActivity;
import com.cube9.afary.user.home_services.adapter.HomeServicesAdapter;
import com.cube9.afary.user.home_services.helperClass.HomeServicesPojo;
import com.google.gson.JsonElement;
import com.valdesekamdem.library.mdtoast.MDToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import dmax.dialog.SpotsDialog;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class HomeServiceActivity extends AppCompatActivity  {
    @BindView(R.id.rv_home_services)
    RecyclerView rv_home_services;
    AlertDialog waiting_dialog;
@BindView(R.id.tv_location)
    TextView tv_location;
    List<HomeServicesPojo> homeServicesPojoList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_service);
        ButterKnife.bind(this);
        tv_location.setText(PrefManager.getInstance(HomeServiceActivity.this).getLocation());
        initView();
        getHomeServices();

    }

    public void getHomeServices()
    {

        try {
            waiting_dialog.show();
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
                                homeServicesPojoList.add(new HomeServicesPojo(service_name,service_id,"",service_description,service_description));
                            }
                            HomeServicesAdapter adapter = new HomeServicesAdapter(homeServicesPojoList,HomeServiceActivity.this);
                            rv_home_services.setAdapter(adapter);


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
                    CustomUtils.showToast(getResources().getString(R.string.check_internet), HomeServiceActivity.this, MDToast.TYPE_WARNING);
                }
            });
        } catch (Exception e) {
            waiting_dialog.dismiss();
            e.printStackTrace();

        }
    }//getHomeServicesClose

    public void initView()
    {
        waiting_dialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage(R.string.loading)
                .setCancelable(false)
                .build();
        GridLayoutManager manager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        rv_home_services.setLayoutManager(manager);
    }

    public void homeServices(String service_type,String service_name)
    {
        switch (service_type)
        {
            case "1":
                startActivity(new Intent(HomeServiceActivity.this,HouseServiceActivity.class));
                break;
            case "2":
                startActivity(new Intent(HomeServiceActivity.this,ElectricianRequestActivity.class).putExtra("intent_to","electrician").putExtra("service_name",service_name));
                break;
            case "3":
                startActivity(new Intent(HomeServiceActivity.this,ElectricianRequestActivity.class).putExtra("intent_to","plumber").putExtra("service_name",service_name));
                break;
                default:
                  CustomUtils.showToast("Coming Soon",HomeServiceActivity.this,MDToast.TYPE_INFO);
                  break;

        }
    }
}
