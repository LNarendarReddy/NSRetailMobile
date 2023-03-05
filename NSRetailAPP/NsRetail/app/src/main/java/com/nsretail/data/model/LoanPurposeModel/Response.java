package com.nsretail.data.model.LoanPurposeModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Response {

    @SerializedName("loan_purpose")
    @Expose
    public ArrayList<String> loanPurposeList;

    public ArrayList<String> getLoanPurposeList() {
        return loanPurposeList;
    }

    public void setLoanPurposeList(ArrayList<String> loanPurposeList) {
        this.loanPurposeList = loanPurposeList;
    }
}