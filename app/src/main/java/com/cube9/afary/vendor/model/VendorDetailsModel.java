package com.cube9.afary.vendor.model;

import android.content.Intent;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.cube9.afary.R;
import com.cube9.afary.helperClass.CustomUtils;
import com.cube9.afary.login.LoginActivity;
import com.cube9.afary.network.RestInterface;
import com.cube9.afary.network.RetrofitClient;
import com.cube9.afary.network.WebServiceURLs;
import com.cube9.afary.user.signup.CompleteRegistrationActivity;
import com.google.gson.JsonElement;
import com.valdesekamdem.library.mdtoast.MDToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.MultipartTypedOutput;
import retrofit.mime.TypedFile;
import retrofit.mime.TypedString;

public class VendorDetailsModel implements IVendorDetailsModel {
    int  result=0;
    @Override
    public int completeVendorSignUp(String cat_id, String vendor_id, String service_id, File profile_image,File document_image) {

        MultipartTypedOutput multipartTypedOutput = new MultipartTypedOutput();
        multipartTypedOutput.addPart("cat_id", new TypedString(cat_id));
        multipartTypedOutput.addPart("vendor_id", new TypedString(vendor_id));
        multipartTypedOutput.addPart("service_id", new TypedString(service_id));
     if (profile_image!=null)
        {
            multipartTypedOutput.addPart("profile_image", new TypedFile("application/octet-stream", profile_image));
        }
         if (document_image!=null)
        {
            multipartTypedOutput.addPart("document_image", new TypedFile("application/octet-stream", document_image));
        }



        RetrofitClient retrofitClient = new RetrofitClient();
        final RestInterface service = retrofitClient.getAPIClient(WebServiceURLs.DOMAIN_NAME);

        service.submit_vendor_details(multipartTypedOutput, new Callback<JsonElement>() {
            @Override
            public void success(JsonElement jsonElement, Response response) {

                try {
                    JSONObject jsonObject=new JSONObject(jsonElement.toString());
                    String status=jsonObject.getString("status");
                    String message=jsonObject.getString("message");
                    if (status.equals("1"))
                    {
                        result=1;
                    }
                    else
                    {
                          result=0;
                    }
                } catch (Exception je) {
                    result=0;
                    je.printStackTrace();
                    Log.i("ressss_fail",""+je.toString());
                }
            }

            @Override
            public void failure(RetrofitError error) { result=0;
                error.printStackTrace();


            }
        });
           return result;
    }
}
