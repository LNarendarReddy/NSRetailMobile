package com.nsretail.data.model.DispatchModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Dispatch {

    @SerializedName("Dispatch")
    @Expose
    public ArrayList<DispatchModel> dispatchList;
    @SerializedName("DispatchDetail")
    @Expose
    public ArrayList<DispatchDetail> dispatchDetail;
}
