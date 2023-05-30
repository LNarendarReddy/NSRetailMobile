package com.nsretail.data.model.ItemDetail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.nsretail.data.model.ItemModel.ItemPrice;

import java.util.ArrayList;

public class ItemResult {

    @SerializedName("ITEMPRICE")
    @Expose
    public ArrayList<ItemPrice> itemPriceList;
    @SerializedName("OFFER")
    @Expose
    public ArrayList<Offer> offerList;
    @SerializedName("ITEMCOSTPRICE")
    @Expose
    public ArrayList<ItemCost> itemCostList;
    @SerializedName("BRANCHSTOCK")
    @Expose
    public ArrayList<BranchStock> branchStockList;

}