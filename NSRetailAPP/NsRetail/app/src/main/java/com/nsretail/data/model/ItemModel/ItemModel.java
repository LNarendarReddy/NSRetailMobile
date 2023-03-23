package com.nsretail.data.model.ItemModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ItemModel {

    @SerializedName("ITEM")
    @Expose
    public ArrayList<Item> itemList;
    @SerializedName("ITEMCODE")
    @Expose
    public ArrayList<ItemCode> itemCodeList;

    @SerializedName("ITEMPRICE")
    @Expose
    public ArrayList<ItemPrice> itemPriceList;

    public ArrayList<Item> getItemList() {
        return itemList;
    }

    public void setItemList(ArrayList<Item> itemList) {
        this.itemList = itemList;
    }

    public ArrayList<ItemCode> getItemCodeList() {
        return itemCodeList;
    }

    public void setItemCodeList(ArrayList<ItemCode> itemCodeList) {
        this.itemCodeList = itemCodeList;
    }

    public ArrayList<ItemPrice> getItemPriceList() {
        return itemPriceList;
    }

    public void setItemPriceList(ArrayList<ItemPrice> itemPriceList) {
        this.itemPriceList = itemPriceList;
    }
}