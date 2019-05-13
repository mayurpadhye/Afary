package com.cube9.afary.network;



import com.google.gson.JsonElement;

import java.util.Map;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Headers;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.PartMap;
import retrofit.mime.MultipartTypedOutput;

/**
 * Rest Interface -> to access web_services using Retrofit
 */

public interface RestInterface {





    @FormUrlEncoded
    @POST(WebServiceURLs.USER_LOGIN)
    void LoginToApp(@Field("email") String email, @Field("password") String password, Callback<JsonElement> callback);


    @FormUrlEncoded
    @POST(WebServiceURLs.GET_STATE)
    void getState(@Field("country_id") String country_id, Callback<JsonElement> callback);

    @FormUrlEncoded
    @POST(WebServiceURLs.REGISTER_USER)
    void register_user(
            @Field("first_name") String first_name,
            @Field("last_name") String last_name,
            @Field("mobile_no") String mobile_no,
            @Field("email") String email,
            @Field("country") String country,
            @Field("state") String state,
            @Field("city") String city,
            @Field("pincode") String pincode,
            @Field("date_of_birth") String date_of_birth,
            @Field("security_question") String security_question,
            @Field("answer") String answer,
            @Field("password") String password,

                       Callback<JsonElement> callback);



    @FormUrlEncoded
    @POST(WebServiceURLs.CHECK_DUPLICATE)
    void CheckEmail(@Field("email") String email, @Field("mobile_no") String mobile_no, Callback<JsonElement> callback);

    @FormUrlEncoded
    @POST(WebServiceURLs.GET_CITY)
    void getCity(@Field("state_id") String state_id, Callback<JsonElement> callback);



    @GET(WebServiceURLs.GET_COUNTRY)
    void getCountry(Callback<JsonElement> callback);

    @FormUrlEncoded
    @POST(WebServiceURLs.GET_HOME_SERVICES)
    void getHomeServices(@Field("cat_id")String cat_id, Callback<JsonElement> callback);

    @FormUrlEncoded
    @POST(WebServiceURLs.ELECTRICIAN_REQUEST)
    void electrication_request(@Field("user_id")String user_id,@Field("lat") String lat,@Field("lng") String lng,@Field("address") String address,@Field("desire_date") String desire_date,
            @Field("desire_time") String desire_time,
            @Field("proposed_amt") String proposed_amt,
            @Field("breakdown_desc") String breakdown_desc
            , Callback<JsonElement> callback);




    @FormUrlEncoded
    @POST(WebServiceURLs.PLUMBER_REQUEST)
    void plumber_request(@Field("user_id")String user_id,@Field("lat") String lat,@Field("lng") String lng,@Field("address") String address,@Field("desire_date") String desire_date,
                               @Field("desire_time") String desire_time,
                               @Field("proposed_amt") String proposed_amt,
                               @Field("breakdown_desc") String breakdown_desc
            , Callback<JsonElement> callback);

    @FormUrlEncoded
    @POST(WebServiceURLs.SUBMIT_HOUSE_REQUEST)
    void SubmitHouseRequest(
            @Field("user_id")String user_id,
            @Field("house_type")String house_type,
            @Field("no_of_rooms")String no_of_rooms,
            @Field("budjet")String budjet,
            @Field("area")String area,
            @Field("state")String state,
            @Field("city")String city,
            @Field("pincode")String pincode,
            @Field("house_category")String house_category,
            @Field("service_id")String service_id,
                            Callback<JsonElement> callback);

}
