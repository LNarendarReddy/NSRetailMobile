package com.nsretail.data.model.StockEntry;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Invoice {

    @SerializedName("StockEntry")
    @Expose
    public ArrayList<StockEntry> stockEntry;
    @SerializedName("StockEntryDetail")
    @Expose
    public ArrayList<StockEntryDetail> stockEntryDetails;

    public ArrayList<StockEntry> getStockEntry() {
        return stockEntry;
    }

    public void setStockEntry(ArrayList<StockEntry> entryArrayList) {
        this.stockEntry = entryArrayList;
    }

    public ArrayList<StockEntryDetail> getStockEntryDetails() {
        return stockEntryDetails;
    }

    public void setStockEntryDetails(ArrayList<StockEntryDetail> stockEntryDetails1) {
        this.stockEntryDetails = stockEntryDetails1;
    }

}