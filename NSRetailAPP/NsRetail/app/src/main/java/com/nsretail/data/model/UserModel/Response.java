package com.nsretail.data.model.UserModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Response {

    @SerializedName("User")
    @Expose
    public ArrayList<UserModel> user;
    @SerializedName("FeatureAccess")
    @Expose
    public ArrayList<FeatureAccess> featureAccess;

    public ArrayList<UserModel> getUser() {
        return user;
    }

    public void setUser(ArrayList<UserModel> user) {
        this.user = user;
    }

    public ArrayList<FeatureAccess> getFeatureAccess() {
        return featureAccess;
    }

    public void setFeatureAccess(ArrayList<FeatureAccess> featureAccess) {
        this.featureAccess = featureAccess;
    }

}