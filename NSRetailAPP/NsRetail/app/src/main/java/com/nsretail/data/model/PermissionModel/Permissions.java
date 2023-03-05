package com.nsretail.data.model.PermissionModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Permissions {

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("response")
    @Expose
    public ArrayList<Result> permissionsArrayList;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<Result> getPermissionsArrayList() {
        return permissionsArrayList;
    }

    public void setPermissionsArrayList(ArrayList<Result> permissionsList) {
        this.permissionsArrayList = permissionsList;
    }
}