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
import com.cube9.afary.network.RetrofitClient2;
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
    public int completeVendorSignUp(final getVendorDetailsInterface getVendorDetailsInterface, String cat_id, String vendor_id, String service_id, File profile_image, File document_image) {

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



        RetrofitClient2 retrofitClient = new RetrofitClient2();
        final RestInterface service = retrofitClient.getAPIClient(WebServiceURLs.DOMAIN_NAME);

        service.submit_vendor_details(multipartTypedOutput, new Callback<String>() {
            @Override
            public void success(String jsonElement, Response response) {

                try {
                    JSONObject jsonObject=new JSONObject(jsonElement);
                    String status=jsonObject.getString("status");
                    String messge=jsonObject.getString("messge");
                    if (status.equals("1"))
                    {
                        getVendorDetailsInterface.onResultFinished(1);
                    }
                    else
                    {
                        getVendorDetailsInterface.onResultFinished(0);
                    }
                } catch (Exception je) {
                    getVendorDetailsInterface.onResultFinished(0);
                    je.printStackTrace();
                    Log.i("ressss_fail",""+je.toString());
                }
            }

            @Override
            public void failure(RetrofitError error) { result=0;
                error.printStackTrace();
                getVendorDetailsInterface.onResultFinished(0);

            }
        });
return result;
    }
}
