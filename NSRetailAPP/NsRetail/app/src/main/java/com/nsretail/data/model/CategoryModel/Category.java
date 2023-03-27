package com.nsretail.data.model.CategoryModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Category {
    @SerializedName("CATEGORYID")
    @Expose
    public int categoryId;
    @SerializedName("CATEGORYNAME")
    @Expose
    public String categoryName;
 }
