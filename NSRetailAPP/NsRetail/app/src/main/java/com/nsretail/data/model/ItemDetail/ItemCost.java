package com.nsretail.data.model.ItemDetail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ItemCost implements Serializable {

    @SerializedName("COSTPRICEWT")
    @Expose
    public double costPriceWT;
    @SerializedName("COSTPRICEWOT")
    @Expose
    public double costPriceWOT;
    @SerializedName("GSTCODE")
    @Expose
    public String gstCode;
}
