package com.nsretail.data.model.ItemModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ItemCode {

    @SerializedName("ITEMID")
    @Expose
    public int itemId;
    @SerializedName("ITEMCODEID")
    @Expose
    public int itemCodeId;
    @SerializedName("ITEMCODE")
    @Expose
    public String itemCode;
    @SerializedName("HSNCODE")
    @Expose
    public String hsnCode;
}
