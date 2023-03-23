package com.nsretail.data.model.SupplierModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Supplier {

    @SerializedName("DEALERID")
    @Expose
    public int dealerId;
    @SerializedName("DEALERNAME")
    @Expose
    public String dealerName;
    @SerializedName("ADDRESS")
    @Expose
    public String address;
    @SerializedName("STATEID")
    @Expose
    public int stateId;
    @SerializedName("PHONENO")
    @Expose
    public String phoneNo;
    @SerializedName("GSTIN")
    @Expose
    public String gstin;
    @SerializedName("PANNUMBER")
    @Expose
    public String panNumber;
    @SerializedName("EMAILID")
    @Expose
    public String emailId;
    @SerializedName("CREATEDBY")
    @Expose
    public String createdId;
    @SerializedName("CREATEDDATE")
    @Expose
    public String createdDate;
    @SerializedName("UPDATEDBY")
    @Expose
    public String updatedBy;
    @SerializedName("UPDATEDDATE")
    @Expose
    public String updatedDate;

}
