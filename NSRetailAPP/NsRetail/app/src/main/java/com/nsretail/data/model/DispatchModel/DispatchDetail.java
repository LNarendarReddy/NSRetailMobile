package com.nsretail.data.model.DispatchModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DispatchDetail {

    @SerializedName("STOCKDISPATCHDETAILID")
    @Expose
    public int stockDispatchDetailId;
    @SerializedName("ITEMID")
    @Expose
    public int itemId;
    @SerializedName("ITEMCODEID")
    @Expose
    public int itemCodeId;
    @SerializedName("ITEMPRICEID")
    @Expose
    public int itemPriceId;
    @SerializedName("SKUCODE")
    @Expose
    public String skuCode;
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
    @SerializedName("DISPATCHQUANTITY")
    @Expose
    public int dispatchQuantity;
    @SerializedName("WEIGHTINKGS")
    @Expose
    public double weightInKgs;
    @SerializedName("TRAYNUMBER")
    @Expose
    public int trayNumber;
    @SerializedName("ISOPENITEM")
    @Expose
    public boolean isOpenItem;
}
