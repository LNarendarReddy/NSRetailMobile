package com.nsretail.data.model.GSTModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GST {
    @SerializedName("GSTID")
    @Expose
    public int gstId;
    @SerializedName("GSTCODE")
    @Expose
    public String gstCode;
    @SerializedName("CGST")
    @Expose
    public double cGST;
    @SerializedName("SGST")
    @Expose
    public double sGST;
    @SerializedName("IGST")
    @Expose
    public double iGST;
    @SerializedName("CESS")
    @Expose
    public double cess;
    @SerializedName("GSTVALUE")
    @Expose
    public double gstValue;
}
