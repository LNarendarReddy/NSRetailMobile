package com.nsretail.data.model.CountingModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CountingModel {

    @SerializedName("Message")
    @Expose
    public String message;

    @SerializedName("STOCKCOUNTINGID")
    @Expose
    public int stockCountingId;
    @SerializedName("BRANCHID")
    @Expose
    public int branchId;
    @SerializedName("BRANCHNAME")
    @Expose
    public String branchName;

}