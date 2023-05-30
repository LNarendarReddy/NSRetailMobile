package com.nsretail.data.model.ItemDetail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Access {

    @SerializedName("SHOWITEMPRICE")
    @Expose
    public boolean showItemPrice;
    @SerializedName("SHOWOFFER")
    @Expose
    public boolean showOffer;
    @SerializedName("SHOWCOSTPRICE")
    @Expose
    public boolean showCostPrice;
    @SerializedName("SHOWSTOCK")
    @Expose
    public boolean showStock;

}
