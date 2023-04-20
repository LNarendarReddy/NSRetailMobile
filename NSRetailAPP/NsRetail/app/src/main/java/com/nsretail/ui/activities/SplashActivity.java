package com.nsretail.ui.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.nsretail.Globals;
import com.nsretail.R;
import com.nsretail.data.api.BaseURL;
import com.nsretail.data.api.StatusAPI;
import com.nsretail.data.model.UserModel.Response;

import retrofit2.Call;
import retrofit2.Callback;


@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {


    String name, password, version;
    Globals globals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_splash);
        globals = new Globals(SplashActivity.this);

        new Handler().postDelayed(() -> {
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            finish();
        }, 3000);

        /*SharedPreferences preferences = getSharedPreferences("login", Context.MODE_PRIVATE);
        name = preferences.getString("name", "");
        if (name.length() > 0) {
            password = preferences.getString("password", "");
            version = preferences.getString("version", "");
            loginData();
        } else {
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            finish();
        }*/


    }

    private void loginData() {
        StatusAPI loginAPI = BaseURL.getStatusAPI();

        Call<Response> call = loginAPI.login(name, password, version);

        call.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                if (response.code() == 200) {

                    Globals.userResponse = response.body();
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {

            }
        });

    }


}