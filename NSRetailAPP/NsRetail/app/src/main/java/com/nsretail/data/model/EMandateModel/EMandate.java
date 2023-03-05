package com.nsretail.data.model.EMandateModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EMandate {

    @SerializedName("success")
    @Expose
    public boolean success;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("response")
    @Expose
    public EMandate response;
    @SerializedName("id")
    @Expose
    public int id;
    @SerializedName("user_id")
    @Expose
    public int user_id;
    @SerializedName("loan_id")
    @Expose
    public int loan_id;
    @SerializedName("plan_id")
    @Expose
    public String plan_id;
    @SerializedName("subscription_id")
    @Expose
    public String subscription_id;
    @SerializedName("customer_name")
    @Expose
    public String customer_name;
    @SerializedName("customer_email")
    @Expose
    public String customer_email;
    @SerializedName("customer_phone")
    @Expose
    public String customer_phone;
    @SerializedName("sub_reference_id")
    @Expose
    public int sub_reference_id;
    @SerializedName("auth_link")
    @Expose
    public String auth_link;
    @SerializedName("expires_on")
    @Expose
    public String expires_on;
    @SerializedName("payment_mode")
    @Expose
    public String payment_mode;
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("added_on")
    @Expose
    public String added_on;
    @SerializedName("scheduled_on")
    @Expose
    public String scheduled_on;
    @SerializedName("current_cycle")
    @Expose
    public int current_cycle;
    @SerializedName("first_charge_date")
    @Expose
    public String first_charge_date;
    @SerializedName("bank_account_number")
    @Expose
    public String bank_account_number;
    @SerializedName("bank_account_holder")
    @Expose
    public String bank_account_holder;
    @SerializedName("umrn")
    @Expose
    public String umrn;
    @SerializedName("response_json")
    @Expose
    public String response_json;
    @SerializedName("created_at")
    @Expose
    public String created_at;
    @SerializedName("updated_at")
    @Expose
    public String updated_at;
}
