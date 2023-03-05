package com.nsretail.data.model.UserModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoanStatusTracking {

    @SerializedName("id")
    @Expose
    public int id;
    @SerializedName("user_id")
    @Expose
    public int user_id;
    @SerializedName("loan_id")
    @Expose
    public int loan_id;
    @SerializedName("status")
    @Expose
    public int status;
    @SerializedName("loan_status")
    @Expose
    public int loanStatus;
    @SerializedName("title")
    @Expose
    public String title;
    @SerializedName("description")
    @Expose
    public String description;
    @SerializedName("created_at")
    @Expose
    public String created_at;
    @SerializedName("updated_at")
    @Expose
    public String updated_at;

}
