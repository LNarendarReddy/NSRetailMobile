package com.nsretail.data.model.ItemDetail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Offer {

    @SerializedName("OFFERTYPENAME")
    @Expose
    public String offerTypeName;
    @SerializedName("OFFERCODE")
    @Expose
    public String offerCode;
    @SerializedName("OFFERVALUE")
    @Expose
    public double offerValue;
    
}