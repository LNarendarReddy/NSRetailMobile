package com.nsretail.data.model.UserModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Financial {

    @SerializedName("id")
    @Expose
    public int id;
    @SerializedName("user_id")
    @Expose
    public int user_id;
    @SerializedName("bank_name")
    @Expose
    public String bank_name;
    @SerializedName("bank_code")
    @Expose
    public Object bank_code;
    @SerializedName("account_no")
    @Expose
    public String account_no;
    @SerializedName("account_type")
    @Expose
    public String account_type;
    @SerializedName("account_name")
    @Expose
    public String account_name;
    @SerializedName("ifsc_code")
    @Expose
    public String ifsc_code;
    @SerializedName("bank_address")
    @Expose
    public String bank_address;
    @SerializedName("pan_number")
    @Expose
    public String pan_number;
    @SerializedName("pan_verified")
    @Expose
    public String pan_verified;
    @SerializedName("pan_generated")
    @Expose
    public int pan_generated;
    @SerializedName("pan_user_name")
    @Expose
    public String pan_user_name;
    @SerializedName("pan_card_image")
    @Expose
    public String pan_card_image;
    @SerializedName("address_proof")
    @Expose
    public int address_proof;
    @SerializedName("aadhar_front_image")
    @Expose
    public String aadhar_front_image;
    @SerializedName("aadhar_back_image")
    @Expose
    public String aadhar_back_image;
    @SerializedName("aadhar_address")
    @Expose
    public String aadhar_address;
    @SerializedName("voter_id_number")
    @Expose
    public String voter_id_number;
    @SerializedName("voter_verified")
    @Expose
    public int voter_verified;
    @SerializedName("voter_generated")
    @Expose
    public int voter_generated;
    @SerializedName("voter_id_front_image")
    @Expose
    public String voter_id_front_image;
    @SerializedName("voter_id_back_image")
    @Expose
    public String voter_id_back_image;
    @SerializedName("dl_front_image")
    @Expose
    public String dl_front_image;
    @SerializedName("dl_back_image")
    @Expose
    public String dl_back_image;
    @SerializedName("dl_number")
    @Expose
    public String dl_number;
    @SerializedName("voter_address")
    @Expose
    public Object voter_address;
    @SerializedName("dl_verified")
    @Expose
    public int dl_verified;
    @SerializedName("dl_generated")
    @Expose
    public int dl_generated;
}
