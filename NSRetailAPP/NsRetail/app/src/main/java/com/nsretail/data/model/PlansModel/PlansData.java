package com.nsretail.data.model.PlansModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PlansData implements Serializable {

    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("plan_tenure")
    @Expose
    public Integer planTenure;
    @SerializedName("emis")
    @Expose
    public Integer emis;
    @SerializedName("plan_name")
    @Expose
    public String planName;
    @SerializedName("tenure_type")
    @Expose
    public String tenureType;
    @SerializedName("min_monthly_salary")
    @Expose
    public int minMonthlySalary;
    @SerializedName("credit_limit")
    @Expose
    public int creditLimit;
    @SerializedName("interest_rate")
    @Expose
    public double interestRate;
    @SerializedName("GST")
    @Expose
    public int GST;
    @SerializedName("processing_fee")
    @Expose
    public int processingFee;
    @SerializedName("color_code")
    @Expose
    public String colorCode;

    @SerializedName("eligible_to_apply")
    @Expose
    public Boolean eligibleApply;
    @SerializedName("max_loan_amount")
    @Expose
    public int maxLoanAmount;
    @SerializedName("min_loan_amount")
    @Expose
    public int minLoanAmount;


    public String getColorCode() {
        return colorCode;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPlanTenure() {
        return planTenure;
    }

    public void setPlanTenure(Integer planTenure) {
        this.planTenure = planTenure;
    }

    public Integer getEmis() {
        return emis;
    }

    public void setEmis(Integer emis) {
        this.emis = emis;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public String getTenureType() {
        return tenureType;
    }

    public void setTenureType(String tenureType) {
        this.tenureType = tenureType;
    }

    public int getMinMonthlySalary() {
        return minMonthlySalary;
    }

    public void setMinMonthlySalary(int minMonthlySalary) {
        this.minMonthlySalary = minMonthlySalary;
    }

    public int getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(int creditLimit) {
        this.creditLimit = creditLimit;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }

    public int getGST() {
        return GST;
    }

    public void setGST(int GST) {
        this.GST = GST;
    }

    public int getProcessingFee() {
        return processingFee;
    }

    public void setProcessingFee(int processingFee) {
        this.processingFee = processingFee;
    }

    public Boolean getEligibleApply() {
        return eligibleApply;
    }

    public void setEligibleApply(Boolean eligibleApply) {
        this.eligibleApply = eligibleApply;
    }

    public int getMaxLoanAmount() {
        return maxLoanAmount;
    }

    public void setMaxLoanAmount(int maxLoanAmount) {
        this.maxLoanAmount = maxLoanAmount;
    }

    public int getMinLoanAmount() {
        return minLoanAmount;
    }

    public void setMinLoanAmount(int minLoanAmount) {
        this.minLoanAmount = minLoanAmount;
    }
}