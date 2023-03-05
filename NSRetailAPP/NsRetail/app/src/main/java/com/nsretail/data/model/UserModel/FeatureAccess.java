
package com.nsretail.data.model.UserModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FeatureAccess {

    @SerializedName("FeatureName")
    @Expose
    public String featureName;
    @SerializedName("AccessAvailable")
    @Expose
    public boolean accessAvailable;

    public String getFeatureName() {
        return featureName;
    }

    public void setFeatureName(String featureName) {
        this.featureName = featureName;
    }

    public boolean isAccessAvailable() {
        return accessAvailable;
    }

    public void setAccessAvailable(boolean accessAvailable) {
        this.accessAvailable = accessAvailable;
    }
}
