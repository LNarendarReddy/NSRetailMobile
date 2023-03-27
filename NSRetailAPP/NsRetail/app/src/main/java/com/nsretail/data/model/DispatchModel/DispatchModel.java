package com.nsretail.data.model.DispatchModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DispatchModel {
    
    @SerializedName("Message")
    @Expose
    public String message;

    @SerializedName("STOCKDISPATCHID")
    @Expose
    public int stockDispatchId;
    @SerializedName("FROMBRANCHID")
    @Expose
    public int fromBranchId;
    @SerializedName("TOBRANCHID")
    @Expose
    public int toBranchId;
    @SerializedName("CATEGORYID")
    @Expose
    public int categoryId;
    @SerializedName("CATEGORYNAME")
    @Expose
    public String categoryName;
    @SerializedName("FROMBRANCHNAME")
    @Expose
    public String fromBranchName;
    @SerializedName("BRANCHCODE")
    @Expose
    public String branchCode;
    @SerializedName("TOBRANCHNAME")
    @Expose
    public String toBranchName;
    @SerializedName("CREATEDBY")
    @Expose
    public String createdBy;
    @SerializedName("CREATEDDATE")
    @Expose
    public String createdDate;
    @SerializedName("UPDATEDBY")
    @Expose
    public String updatedBy;
    @SerializedName("UPDATEDATE")
    @Expose
    public String updatedDate;
    @SerializedName("DISPATCHNUMBER")
    @Expose
    public String dispatchNumber;

}
