package com.nsretail.data.model.PlansModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Response {

    @SerializedName("plans")
    @Expose
    private ArrayList<PlansData> plans = null;

    public ArrayList<PlansData> getPlans() {
        return plans;
    }

    public void setPlans(ArrayList<PlansData> plans) {
        this.plans = plans;
    }

}
