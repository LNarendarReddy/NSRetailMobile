package com.nsretail.data.model.CountingModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CountingDetail implements Serializable {

    @SerializedName("SNO")
    @Expose
    public int sNo;
    @SerializedName("STOCKCOUNTINGDETAILID")
    @Expose
    public int stockCountingDetailId;
    @SerializedName("STOCKCOUNTINGID")
    @Expose
    public int stockCountingId;
    @SerializedName("ITEMPRICEID")
    @Expose
    public int itemPriceId;
    @SerializedName("ITEMCODE")
    @Expose
    public String itemCode;
    @SerializedName("ITEMNAME")
    @Expose
    public String itemName;
    @SerializedName("MRP")
    @Expose
    public double mrp;
    @SerializedName("SALEPRICE")
    @Expose
    public double salePrice;
    @SerializedName("QUANTITY")
    @Expose
    public int quantity;
    @SerializedName("WEIGHTINKGS")
    @Expose
    public double weightInKgs;
    @SerializedName("ISOPENITEM")
    @Expose
    public boolean isOpenItem;
    @SerializedName("SKUCODE")
    @Expose
    public String skuCode;

}
