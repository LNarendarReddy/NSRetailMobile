package com.nsretail.data.api;

import com.google.gson.JsonObject;
import com.nsretail.data.model.GSTModel.GST;
import com.nsretail.data.model.ItemModel.ItemModel;
import com.nsretail.data.model.StockEntry.Invoice;
import com.nsretail.data.model.SupplierModel.Supplier;
import com.nsretail.data.model.UserModel.Response;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface StatusAPI {


    @POST("user/getuserlogin")
    Call<Response> login(@Query("UserName") String UserName,
                         @Query("Password") String Password,
                         @Query("AppVersion") String AppVersion);


    @GET("master/getsupplier")
    Call<List<Supplier>> supplierData(@Query("UseWHConnection") boolean useWHConnection);

    @GET("master/getgst")
    Call<List<GST>> gstData(@Query("UseWHConnection") boolean useWHConnection);

    @GET("stockentry/getinvoice")
    Call<Invoice> getInvoiceData(@Query("CategoryID") int categoryId,
                                 @Query("UserID") int userId,
                                 @Query("UseWHConnection") boolean useWHConnection);

    @POST("stockentry/saveinvoice")
    Call<ResponseBody> saveInvoiceData(@Query("UseWHConnection") boolean useWHConnection, @Query("jsonstring") JsonObject jsonObject);

    @GET("item/getitem")
    Call<ItemModel> getItemData(@Query("UseWHConnection") boolean useWHConnection, @Query("ItemCode") String itemCode);


}