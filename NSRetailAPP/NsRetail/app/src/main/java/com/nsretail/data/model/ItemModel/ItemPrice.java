package com.nsretail.data.model.ItemModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ItemPrice implements Serializable {

    @SerializedName("ITEMCODEID")
    @Expose
    public int itemCodeId;
    @SerializedName("ITEMPRICEID")
    @Expose
    public int itemPriceId;
    @SerializedName("MRP")
    @Expose
    public double mrp;
    @SerializedName("SALEPRICE")
    @Expose
    public double salePrice;
    @SerializedName("GSTID")
    @Expose
    public int gstId;
    @SerializedName("COSTPRICEWT")
    @Expose
    public double costPriceWT;
    @SerializedName("COSTPRICEWOT")
    @Expose
    public double costPriceWOT;
    @SerializedName("STATUS")
    @Expose
    public String status;
}
