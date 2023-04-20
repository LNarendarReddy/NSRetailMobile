package com.nsretail.data.model.CountingModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Counting {

    @SerializedName("Counting")
    @Expose
    public ArrayList<CountingModel> countingList;
    @SerializedName("CountingDetail")
    @Expose
    public ArrayList<CountingDetail> countingDetail;
}
