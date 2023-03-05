package com.nsretail.data.api;

import com.nsretail.data.model.UserModel.Response;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface StatusAPI {


    @POST("user/getuserlogin")
    Call<Response> login(@Query("UserName") String UserName,
                         @Query("Password") String Password,
                         @Query("AppVersion") String AppVersion);

   /* @GET("loanPlans")
    Call<LoanPlansData> plansData();

    @FormUrlEncoded
    @POST("user/getuserlogin?")
    Call<Result> googleLogin(@Field("first_name") String first_name,
                             @Field("last_name") String last_name,
                             @Field("email") String email,
                             @Field("access_token") String access_token);
    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"})
    @POST("/api/emandateInititate")
    Call<SuccessModel> eMandate(@Header("Authorization") String authHeader, @Body JsonObject Object);*/

}