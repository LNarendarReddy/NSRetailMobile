package com.nsretail.data.model.UserModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Address {

    @SerializedName("id")
    @Expose
    public int id;
    @SerializedName("user_id")
    @Expose
    public int user_id;
    @SerializedName("address_mode")
    @Expose
    public String address_mode;
    @SerializedName("staying_years")
    @Expose
    public String staying_years;
    @SerializedName("address")
    @Expose
    public String address;
    @SerializedName("locality")
    @Expose
    public String locality;
    @SerializedName("city")
    @Expose
    public CityAddress city;
    @SerializedName("state")
    @Expose
    public StateAddress state;
    @SerializedName("pincode")
    @Expose
    public String pincode;
    @SerializedName("address_type")
    @Expose
    public int address_type;
    @SerializedName("lat")
    @Expose
    public String lat;
    @SerializedName("long")
    @Expose
    public String mylong;
    @SerializedName("status")
    @Expose
    public int status;
    @SerializedName("created_at")
    @Expose
    public String created_at;
    @SerializedName("updated_at")
    @Expose
    public String updated_at;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getAddress_mode() {
        return address_mode;
    }

    public void setAddress_mode(String address_mode) {
        this.address_mode = address_mode;
    }

    public String getStaying_years() {
        return staying_years;
    }

    public void setStaying_years(String staying_years) {
        this.staying_years = staying_years;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public CityAddress getCity() {
        return city;
    }

    public void setCity(CityAddress city) {
        this.city = city;
    }

    public StateAddress getState() {
        return state;
    }

    public void setState(StateAddress state) {
        this.state = state;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public int getAddress_type() {
        return address_type;
    }

    public void setAddress_type(int address_type) {
        this.address_type = address_type;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getMylong() {
        return mylong;
    }

    public void setMylong(String mylong) {
        this.mylong = mylong;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}
