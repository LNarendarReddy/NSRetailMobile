package com.nsretail.data.model.UserModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.onboard.data.model.PlansModel.PlansData;

import java.util.ArrayList;

public class LoanDetails {

    @SerializedName("id")
    @Expose
    public int id;
    @SerializedName("user_id")
    @Expose
    public int user_id;
    @SerializedName("plan_id")
    @Expose
    public int plan_id;
    @SerializedName("max_eligible_amount")
    @Expose
    public int max_eligible_amount;
    @SerializedName("in_hand_amount")
    @Expose
    public String in_hand_amount;
    @SerializedName("paid_amount")
    @Expose
    public String paid_amount;
    @SerializedName("loan_amount")
    @Expose
    public String loan_amount;
    @SerializedName("closure_amount")
    @Expose
    public String closure_amount;
    @SerializedName("start_date")
    @Expose
    public String start_date;
    @SerializedName("end_date")
    @Expose
    public String end_date;
    @SerializedName("total_emi")
    @Expose
    public int total_emi;
    @SerializedName("rem_emi")
    @Expose
    public int rem_emi;
    @SerializedName("emi_amount")
    @Expose
    public String emi_amount;
    @SerializedName("due_date")
    @Expose
    public String due_date;
    @SerializedName("interest_on_emi_amount")
    @Expose
    public String interest_on_emi_amount;
    @SerializedName("loan_purpose")
    @Expose
    public String loan_purpose;
    @SerializedName("promo_code")
    @Expose
    public String promo_code;
    @SerializedName("status")
    @Expose
    public int status;
    @SerializedName("sub_status")
    @Expose
    public int sub_status;
    @SerializedName("offer_id")
    @Expose
    public int offer_id;
    @SerializedName("verified_at")
    @Expose
    public String verified_at;
    @SerializedName("loan_agreement")
    @Expose
    public String loan_agreement;
    @SerializedName("esign_enabled")
    @Expose
    public int esign_enabled;
    @SerializedName("doc_id")
    @Expose
    public String doc_id;
    @SerializedName("nach_doc")
    @Expose
    public String nach_doc;
    @SerializedName("esign_doc")
    @Expose
    public String esign_doc;
    @SerializedName("esign_docket_id")
    @Expose
    public String esign_docket_id;
    @SerializedName("esign_document_id")
    @Expose
    public String esign_document_id;
    @SerializedName("disbursement_status")
    @Expose
    public int disbursement_status;
    @SerializedName("urm")
    @Expose
    public String urm;
    @SerializedName("nbfc_doc")
    @Expose
    public String nbfc_doc;
    @SerializedName("cir_doc")
    @Expose
    public String cir_doc;
    @SerializedName("kyc_zip_link")
    @Expose
    public String kyc_zip_link;
    @SerializedName("is_kyc_zip_generated")
    @Expose
    public int is_kyc_zip_generated;
    @SerializedName("write_off_approved_by")
    @Expose
    public String write_off_approved_by;
    @SerializedName("write_off_approved_at")
    @Expose
    public String write_off_approved_at;
    @SerializedName("is_write_off")
    @Expose
    public int is_write_off;
    @SerializedName("loan_closed_date")
    @Expose
    public String loan_closed_date;
    @SerializedName("is_loan_closed")
    @Expose
    public int is_loan_closed;
    @SerializedName("is_waive_off")
    @Expose
    public int is_waive_off;
    @SerializedName("waive_off_amount")
    @Expose
    public String waive_off_amount;
    @SerializedName("loan_noc")
    @Expose
    public String loan_noc;
    @SerializedName("payment_id")
    @Expose
    public String payment_id;
    @SerializedName("created_at")
    @Expose
    public String created_at;
    @SerializedName("updated_at")
    @Expose
    public String updated_at;
    @SerializedName("plan_details")
    @Expose
    public PlansData plan_details;
    @SerializedName("statustype")
    @Expose
    public StatusType statustype;
    @SerializedName("substatustype")
    @Expose
    public String substatustype;
    @SerializedName("disbursement")
    @Expose
    public String disbursement;
    @SerializedName("loan_installments")
    @Expose
    public ArrayList<String> loan_installments;
    @SerializedName("installment")
    @Expose
    public String installment;
    @SerializedName("loan_repayments")
    @Expose
    public ArrayList<String> loan_repayments;
    @SerializedName("loan_status_transaction")
    @Expose
    public ArrayList<LoanStatusTransaction> loan_status_transaction;
    @SerializedName("loan_status_tracking")
    @Expose
    public ArrayList<LoanStatusTracking> loanStatusTracking;
}
