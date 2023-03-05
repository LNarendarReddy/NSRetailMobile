package com.nsretail.data.model.UserModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoanStatusTransaction {

    @SerializedName("id")
    @Expose
    public int id;
    @SerializedName("team_id")
    @Expose
    public int team_id;
    @SerializedName("custumer_id")
    @Expose
    public int custumer_id;
    @SerializedName("loan_id")
    @Expose
    public int loan_id;
    @SerializedName("status_type")
    @Expose
    public int status_type;
    @SerializedName("loan_status")
    @Expose
    public int loan_status;
    @SerializedName("loan_sub_status")
    @Expose
    public String loan_sub_status;
    @SerializedName("comment")
    @Expose
    public String comment;
    @SerializedName("created_at")
    @Expose
    public String created_at;
    @SerializedName("veriferdetails")
    @Expose
    public VeriferDetails veriferdetails;
    @SerializedName("statustype")
    @Expose
    public StatusType statustype;
    @SerializedName("substatustype")
    @Expose
    public Object substatustype;
    @SerializedName("disbursement")
    @Expose
    public Object disbursement;
    @SerializedName("loanstatus")
    @Expose
    public StatusType loanstatus;

}
