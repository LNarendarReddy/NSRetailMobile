package com.nsretail.data.model.ItemModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Item {

    @SerializedName("ITEMID")
    @Expose
    public int itemId;
    @SerializedName("SKUCODE")
    @Expose
    public String skuCode;
    @SerializedName("ITEMNAME")
    @Expose
    public String itemName;
    @SerializedName("ISOPENITEM")
    @Expose
    public boolean isOpenItem;
    @SerializedName("PARENTITEMID")
    @Expose
    public int parentItemId;
}
