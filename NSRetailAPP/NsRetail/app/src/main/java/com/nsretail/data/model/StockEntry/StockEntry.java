
package com.nsretail.data.model.StockEntry;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class StockEntry implements Serializable {

    @SerializedName("STOCKENTRYID")
    @Expose
    public int stockEntryId;
    @SerializedName("SUPPLIERID")
    @Expose
    public int supplierId;
    @SerializedName("SUPPLIERINVOICENO")
    @Expose
    public String supplierInvoiceNo;
    @SerializedName("TAXINCLUSIVE")
    @Expose
    public String taxInclusive;
    @SerializedName("TAXINCLUSIVEVALUE")
    @Expose
    public boolean taxInclusiveValue;
    @SerializedName("CATEGORYID")
    @Expose
    public int categoryId;
    @SerializedName("DEALERNAME")
    @Expose
    public String dealerName;
    @SerializedName("GSTIN")
    @Expose
    public String gstIn;
    @SerializedName("CATEGORYNAME")
    @Expose
    public String categoryName;
    @SerializedName("CREATEDBY")
    @Expose
    public String createdBy;
    @SerializedName("CREATEDDATE")
    @Expose
    public String createdDate;
    @SerializedName("INVOICEDATE")
    @Expose
    public String invoiceDate;
    @SerializedName("TCS")
    @Expose
    public double tcs;
    @SerializedName("DISCOUNTPER")
    @Expose
    public double discountPer;
    @SerializedName("DISCOUNT")
    @Expose
    public double discount;
    @SerializedName("EXPENSES")
    @Expose
    public double expenses;
    @SerializedName("TRANSPORT")
    @Expose
    public double transport;
    @SerializedName("FINALPRICE")
    @Expose
    public double finalPrice;
}