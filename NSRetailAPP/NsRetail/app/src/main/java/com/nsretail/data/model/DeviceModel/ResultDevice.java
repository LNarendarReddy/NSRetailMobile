
package com.nsretail.data.model.DeviceModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResultDevice {

    @SerializedName("success")
    @Expose
    public Boolean success;
    @SerializedName("message")
    @Expose
    public String message;
//    @SerializedName("response")
//    @Expose
//    public int response;

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

    /*public int getResponse() {
        return response;
    }

    public void setResponse(int response) {
        this.response = response;
    }
*/
}
