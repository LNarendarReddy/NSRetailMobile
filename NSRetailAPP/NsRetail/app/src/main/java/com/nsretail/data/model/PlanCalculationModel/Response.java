package com.nsretail.data.model.PlanCalculationModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Response {

    @SerializedName("plan_details")
    @Expose
    public PlanDetails plan_details;


}
