package com.nsretail.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.nsretail.Globals;
import com.nsretail.R;
import com.nsretail.data.api.BaseURL;
import com.nsretail.data.api.StatusAPI;
import com.nsretail.data.model.UserModel.Response;
import com.nsretail.databinding.ActivityLoginBinding;

import java.io.IOException;
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

//        binding.editUserName.setText("naren");
//        binding.editPassword.setText("1234");

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
        binding.progressBar.setVisibility(View.VISIBLE);
        
        StatusAPI loginAPI = BaseURL.getStatusAPI();

        Call<Response> call = loginAPI.login(binding.editUserName.getText().toString(), binding.editPassword.getText().toString(),
                "1.4");
        call.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                binding.progressBar.setVisibility(View.GONE);
                if (response.code() == 200) {

                    SharedPreferences preferences1 = getSharedPreferences("login", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor1 = preferences1.edit();
                    editor1.putString("name", binding.editUserName.getText().toString());
                    editor1.putString("password", binding.editPassword.getText().toString());
                    editor1.putString("version", "1.4");
                    editor1.apply();

                    Globals.userResponse = response.body();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this, R.style.AlertDialogCustom);
                    try {
                        builder.setMessage(response.errorBody().string())
                                .setCancelable(false)
                                .setPositiveButton("OK", (dialog, id) -> {
                                    dialog.cancel();
                                });
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                if (t.getMessage().equalsIgnoreCase("Failed to connect to nsoftsol.com/122.175.62.71:6002")) {
                    builder.setMessage("Network Issue!!").setCancelable(false).setPositiveButton("OK", (dialog, id) -> {
                        dialog.cancel();
                    });
                } else {
                    builder.setMessage(t.getMessage()).setCancelable(false).setPositiveButton("OK", (dialog, id) -> {
                        dialog.cancel();
                    });
                }
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

    }
}