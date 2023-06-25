package com.nsretail.ui.activities.StockCounting;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;

import com.nsretail.Globals;
import com.nsretail.R;
import com.nsretail.data.api.BaseURL;
import com.nsretail.data.api.StatusAPI;
import com.nsretail.data.model.BranchModel.Branch;
import com.nsretail.databinding.ActivityBranchBinding;
import com.nsretail.ui.Interface.OnItemClickListener;
import com.nsretail.ui.adapter.BranchAdapter;
import com.nsretail.utils.NetworkStatus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CountingBranchActivity extends AppCompatActivity implements OnItemClickListener {

    ActivityBranchBinding binding;
    List<Branch> branchList;
    BranchAdapter adapter;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBranchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.includeBranch.textTitle.setText("Branches");

        DividerItemDecoration horizontalDecoration = new DividerItemDecoration(binding.recyclerViewBranch.getContext(),
                DividerItemDecoration.VERTICAL);
        Drawable divider = ContextCompat.getDrawable(this, R.drawable.divider);
        if (divider != null) {
            horizontalDecoration.setDrawable(divider);
        }
        binding.recyclerViewBranch.addItemDecoration(horizontalDecoration);

        if (NetworkStatus.getInstance(CountingBranchActivity.this).isConnected())
            getBranch();
        else
            Toast.makeText(CountingBranchActivity.this, "No internet Connection", Toast.LENGTH_SHORT).show();

        binding.includeBranch.imageBack.setVisibility(View.VISIBLE);
        binding.includeBranch.imageBack.setOnClickListener(v -> finish());

    }

    private void getBranch() {
        binding.progressBar.setVisibility(View.VISIBLE);
        branchList = new ArrayList<>();
        StatusAPI branchApi = BaseURL.getStatusAPI();
        Call<List<Branch>> call = branchApi.getBranch("stockcounting/getbranch", Globals.userResponse.user.get(0).userId);
        call.enqueue(new Callback<List<Branch>>() {
            @Override
            public void onResponse(Call<List<Branch>> call, Response<List<Branch>> response) {
                binding.progressBar.setVisibility(View.GONE);

                if (response.code() == 200) {
                    branchList = response.body();
                    adapter = new BranchAdapter(getApplicationContext(), branchList, CountingBranchActivity.this);
                    binding.recyclerViewBranch.setAdapter(adapter);

                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(CountingBranchActivity.this);
                    try {
                        builder.setMessage(response.errorBody().string())
                                .setCancelable(false)
                                .setPositiveButton("OK", (dialog, id) ->
                                        dialog.cancel());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }

            @Override
            public void onFailure(Call<List<Branch>> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                AlertDialog.Builder builder = new AlertDialog.Builder(CountingBranchActivity.this);
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

    @Override
    public void onItemClick(int position, View view) {

        if (NetworkStatus.getInstance(CountingBranchActivity.this).isConnected())
            postCounting(position);
        else
            Toast.makeText(CountingBranchActivity.this, "No internet Connection", Toast.LENGTH_SHORT).show();

    }

    private void postCounting(int pos) {

        binding.progressBar.setVisibility(View.VISIBLE);

        StatusAPI countingAPI = BaseURL.getStatusAPI();


        Call<ResponseBody> call = countingAPI.postCounting(0,Globals.userResponse.user.get(0).userId, branchList.get(pos).branchId);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                binding.progressBar.setVisibility(View.GONE);
                if (response.code() == 200) {
                    Intent h = new Intent(CountingBranchActivity.this, StockCountingActivity.class);
                    h.putExtra("stockCountingId", response.message());
                    startActivity(h);
                    finish();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(CountingBranchActivity.this);
                    try {
                        builder.setMessage(response.errorBody().string())
                                .setCancelable(false)
                                .setPositiveButton("OK", (dialog, id) ->
                                        dialog.cancel());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                AlertDialog.Builder builder = new AlertDialog.Builder(CountingBranchActivity.this);
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