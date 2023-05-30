package com.nsretail.data.model.ItemDetail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.nsretail.data.model.BranchModel.Branch;

import java.util.ArrayList;

public class ItemBranch {

    @SerializedName("ACCESS")
    @Expose
    public ArrayList<Access> accessList;
    @SerializedName("BRANCH")
    @Expose
    public ArrayList<Branch> branchlist;

}
