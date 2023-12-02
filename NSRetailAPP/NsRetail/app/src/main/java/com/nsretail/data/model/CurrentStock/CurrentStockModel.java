package com.nsretail.data.model.CurrentStock;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CurrentStockModel {

    @SerializedName("WHSTOCK")
    @Expose
    public double whStock;
    @SerializedName("BRANCHSTOCK")
    @Expose
    public double branchStock;
}
