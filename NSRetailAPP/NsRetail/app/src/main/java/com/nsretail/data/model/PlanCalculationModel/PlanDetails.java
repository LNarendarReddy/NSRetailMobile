package com.nsretail.data.model.PlanCalculationModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PlanDetails implements Serializable {

    @SerializedName("loan_amount")
    @Expose
    public Integer loanAmount;
    @SerializedName("in_hand_amount")
    @Expose
    public Integer handAmount;
    @SerializedName("max_eligible_amount")
    @Expose
    public Integer maxEligibleAmount;
    @SerializedName("closure_amount")
    @Expose
    public Integer closureAmount;
    @SerializedName("emi_amount")
    @Expose
    public Integer emiAmount;
    @SerializedName("processing_fee")
    @Expose
    public String processingFee;
    @SerializedName("processing_fee_amount")
    @Expose
    public Integer processingFeeAmount;
    @SerializedName("interest_rate")
    @Expose
    public String interestRate;
    @SerializedName("interest_rate_amount")
    @Expose
    public double interestRateAmount;
    @SerializedName("GST")
    @Expose
    public String GST;
    @SerializedName("GST_amount")
    @Expose
    public Integer GSTAmount;
    @SerializedName("total_emi")
    @Expose
    public Integer totalEmi;

    public Integer getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(Integer loanAmount) {
        this.loanAmount = loanAmount;
    }

    public Integer getHandAmount() {
        return handAmount;
    }

    public void setHandAmount(Integer handAmount) {
        this.handAmount = handAmount;
    }

    public Integer getMaxEligibleAmount() {
        return maxEligibleAmount;
    }

    public void setMaxEligibleAmount(Integer maxEligibleAmount) {
        this.maxEligibleAmount = maxEligibleAmount;
    }

    public Integer getClosureAmount() {
        return closureAmount;
    }

    public void setClosureAmount(Integer closureAmount) {
        this.closureAmount = closureAmount;
    }

    public Integer getEmiAmount() {
        return emiAmount;
    }

    public void setEmiAmount(Integer emiAmount) {
        this.emiAmount = emiAmount;
    }

    public String getProcessingFee() {
        return processingFee;
    }

    public void setProcessingFee(String processingFee) {
        this.processingFee = processingFee;
    }

    public Integer getProcessingFeeAmount() {
        return processingFeeAmount;
    }

    public void setProcessingFeeAmount(Integer processingFeeAmount) {
        this.processingFeeAmount = processingFeeAmount;
    }

    public String getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(String interestRate) {
        this.interestRate = interestRate;
    }

    public Double getInterestRateAmount() {
        return interestRateAmount;
    }

    public void setInterestRateAmount(Double interestRateAmount) {
        this.interestRateAmount = interestRateAmount;
    }

    public String getGST() {
        return GST;
    }

    public void setGST(String GST) {
        this.GST = GST;
    }

    public Integer getGSTAmount() {
        return GSTAmount;
    }

    public void setGSTAmount(Integer GSTAmount) {
        this.GSTAmount = GSTAmount;
    }

    public Integer getTotalEmi() {
        return totalEmi;
    }

    public void setTotalEmi(Integer totalEmi) {
        this.totalEmi = totalEmi;
    }
}