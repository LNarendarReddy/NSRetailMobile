
package com.nsretail.data.model.StockEntry;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class StockEntryDetail implements Serializable {

    @SerializedName("STOCKENTRYDETAILID")
    @Expose
    public int stockEntryDetailId;
    @SerializedName("ITEMID")
    @Expose
    public int itemId;
    @SerializedName("ISOPENITEM")
    @Expose
    public boolean isOpenItem;
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
    @SerializedName("COSTPRICEWT")
    @Expose
    public double costPriceWt;
    @SerializedName("COSTPRICEWOT")
    @Expose
    public double costPriceWOT;
    @SerializedName("GSTID")
    @Expose
    public int gstId;
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
    @SerializedName("FREEQUANTITY")
    @Expose
    public int freeQuantity;
    @SerializedName("DISCOUNTFLAT")
    @Expose
    public double discountFlat;
    @SerializedName("DISCOUNTPERCENTAGE")
    @Expose
    public double discountPercentage;
    @SerializedName("SCHEMEPERCENTAGE")
    @Expose
    public double schemePercentage;
    @SerializedName("SCHEMEFLAT")
    @Expose
    public double schemeFlat;
    @SerializedName("TOTALPRICEWT")
    @Expose
    public double totalPriceWT;
    @SerializedName("TOTALPRICEWOT")
    @Expose
    public double totalPriceWOT;
    @SerializedName("APPLIEDDISCOUNT")
    @Expose
    public double appliedDiscount;
    @SerializedName("APPLIEDSCHEME")
    @Expose
    public double appliedScheme;
    @SerializedName("GSTCODE")
    @Expose
    public String gstCode;
    @SerializedName("APPLIEDDGST")
    @Expose
    public double appliedDGst;
    @SerializedName("FINALPRICE")
    @Expose
    public double finalPrice;
    @SerializedName("CGST")
    @Expose
    public double cGst;
    @SerializedName("SGST")
    @Expose
    public double sGst;
    @SerializedName("IGST")
    @Expose
    public double iGst;
    @SerializedName("CESS")
    @Expose
    public double cess;
    @SerializedName("HSNCODE")
    @Expose
    public double hsnCode;
}