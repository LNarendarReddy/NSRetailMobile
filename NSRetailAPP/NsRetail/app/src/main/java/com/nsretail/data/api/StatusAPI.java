package com.nsretail.data.api;

import com.google.gson.JsonObject;
import com.nsretail.data.model.BranchModel.Branch;
import com.nsretail.data.model.CategoryModel.Category;
import com.nsretail.data.model.DispatchModel.Dispatch;
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
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface StatusAPI {


    @POST("user/getuserlogin")
    Call<Response> login(@Query("UserName") String UserName,
                         @Query("Password") String Password,
                         @Query("AppVersion") String AppVersion);


    @GET("{endpoint}")
    Call<List<Branch>> getBranch(@Path(value = "endpoint", encoded = true) String endpoint, @Query("Userid") int userId);

    @GET("master/getcategory")
    Call<List<Category>> getCategory(@Query("Userid") int userId);

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

    @GET("stockentry/getitem")
    Call<ItemModel> getEntryItemData(@Query("UseWHConnection") boolean useWHConnection, @Query("ItemCode") String itemCode);

    @GET("stockdispatch/getitem")
    Call<ItemModel> getDispatchItemData(@Query("UseWHConnection") boolean useWHConnection, @Query("CategoryID") int categoryId, @Query("ItemCode") String itemCode);

    @GET("stockdispatch/getdispatch")
    Call<Dispatch> getDispatch(@Query("CategoryID") int categoryId,
                               @Query("UserID") int userId,
                               @Query("UseWHConnection") boolean useWHConnection);

    @POST("stockdispatch/savedispatch")
    Call<ResponseBody> saveDispatch(@Query("UseWHConnection") boolean useWHConnection, @Query("jsonstring") JsonObject jsonObject);

    @POST("stockdispatch/savedispatchdetail")
    Call<ResponseBody> saveItemDispatch(@Query("UseWHConnection") boolean useWHConnection, @Query("jsonstring") JsonObject jsonObject);

    @POST("stockdispatch/deletedispatchdetail")
    Call<ResponseBody> deleteDispatch(@Query("StockDispatchDetailID") int dispatchId, @Query("UseWHConnection") boolean useWHConnection);

    @POST("stockdispatch/updatedispatch")
    Call<ResponseBody> updateDispatch(@Query("StockDispatchID") int dispatchId, @Query("UseWHConnection") boolean useWHConnection);

    @POST("stockdispatch/discarddispatch")
    Call<ResponseBody> discardDispatch(@Query("StockDispatchID") int dispatchId, @Query("UserID") int userId, @Query("UseWHConnection") boolean useWHConnection);


    // StockCounting

//    @GET("{endpoint}")
//    Call<List<Branch>> getBranch(@Query("Userid") int userId);


}