package com.nsretail.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.nsretail.Globals;
import com.nsretail.data.api.BaseURL;
import com.nsretail.data.api.StatusAPI;
import com.nsretail.data.model.UserModel.Response;
import com.nsretail.databinding.ActivityLoginBinding;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.buttonSubmit.setOnClickListener(view -> {
            if (Objects.requireNonNull(binding.editUserName.getText()).toString().length() > 0) {
                if (Objects.requireNonNull(binding.editPassword.getText()).toString().length() > 0) {
                    loginData();
                } else {
                    binding.editPassword.setError("Enter Password");
                }
            } else {
                binding.editUserName.setError("Enter Username");
            }
        });


    }

    private void loginData() {
        StatusAPI loginAPI = BaseURL.getStatusAPI();

        Call<Response> call = loginAPI.login(binding.editUserName.getText().toString(), binding.editPassword.getText().toString(),
                "1.0");

        call.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                if (response.code() == 200) {

                    SharedPreferences preferences1 = getSharedPreferences("login", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor1 = preferences1.edit();
                    editor1.putString("name", binding.editUserName.getText().toString());
                    editor1.putString("password", binding.editPassword.getText().toString());
                    editor1.putString("version", "1.0");
                    editor1.apply();

                    Globals.userResponse = response.body();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {

            }
        });

    }
}