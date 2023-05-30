package com.nsretail.data.model.ItemDetail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BranchStock {

    @SerializedName("ITEMCODEID")
    @Expose
    public int itemCodeId;
    @SerializedName("QUANTITY")
    @Expose
    public int quantity;
    @SerializedName("WEIGHTINKGS")
    @Expose
    public double weightInKgs;

}
