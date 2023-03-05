
package com.nsretail.data.model.UserModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserModel {

    @SerializedName("USERID")
    @Expose
    public int userId;
    @SerializedName("USERNAME")
    @Expose
    public String userName;
    @SerializedName("FULLNAME")
    @Expose
    public String fullName;
    @SerializedName("ROLEID")
    @Expose
    public int roleId;
    @SerializedName("ROLENAME")
    @Expose
    public String roleName;
    @SerializedName("CATEGORYID")
    @Expose
    public int categoryId;
    @SerializedName("CATEGORYNAME")
    @Expose
    public String categoryName;
    @SerializedName("BRANCHID")
    @Expose
    public int branchId;
    @SerializedName("BRANCHNAME")
    @Expose
    public String branchName;

}