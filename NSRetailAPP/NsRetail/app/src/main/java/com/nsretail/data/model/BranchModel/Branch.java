package com.nsretail.data.model.BranchModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Branch {
    @SerializedName("BRANCHID")
    @Expose
    public int branchId;
    @SerializedName("BRANCHNAME")
    @Expose
    public String branchName;
    @SerializedName("BRANCHCODE")
    @Expose
    public String branchCode;
    @SerializedName("ADDRESS")
    @Expose
    public String address;
    @SerializedName("PHONENO")
    @Expose
    public String phoneNo;
    @SerializedName("LANDLINE")
    @Expose
    public String landline;
    @SerializedName("EMAILID")
    @Expose
    public String emailId;
    @SerializedName("MULTIEDITTHRESHOLD")
    @Expose
    public String multiEditThreshold;
}
