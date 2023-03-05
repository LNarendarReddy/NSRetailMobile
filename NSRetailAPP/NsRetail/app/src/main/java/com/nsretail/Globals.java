package com.nsretail;

import android.annotation.SuppressLint;
import android.content.Context;

import com.nsretail.data.model.UserModel.Response;


public class Globals {

    @SuppressLint("StaticFieldLeak")
    private static Context mCtx;
    public static Response userResponse;

    public Globals(Context context) {
        mCtx = context;
    }
}