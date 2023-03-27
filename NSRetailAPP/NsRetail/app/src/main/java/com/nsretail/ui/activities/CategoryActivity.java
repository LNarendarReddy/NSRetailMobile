package com.nsretail.ui.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;

import com.google.gson.JsonObject;
import com.nsretail.Globals;
import com.nsretail.R;
import com.nsretail.data.api.BaseURL;
import com.nsretail.data.api.StatusAPI;
import com.nsretail.data.model.CategoryModel.Category;
import com.nsretail.databinding.ActivityBranchBinding;
import com.nsretail.ui.Interface.OnItemClickListener;
import com.nsretail.ui.adapter.CategoryAdapter;
import com.nsretail.utils.NetworkStatus;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryActivity extends AppCompatActivity implements OnItemClickListener {

    ActivityBranchBinding binding;
    List<Category> categoryList;
    CategoryAdapter adapter;

    int branchId;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBranchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.includeBranch.textTitle.setText("Category");

        if (getIntent() != null) {
            branchId = getIntent().getIntExtra("ToBranchId", -1);
        }

        DividerItemDecoration horizontalDecoration = new DividerItemDecoration(binding.recyclerViewBranch.getContext(),
                DividerItemDecoration.VERTICAL);
        Drawable divider = ContextCompat.getDrawable(this, R.drawable.divider);
        if (divider != null) {
            horizontalDecoration.setDrawable(divider);
        }
        binding.recyclerViewBranch.addItemDecoration(horizontalDecoration);

        if (NetworkStatus.getInstance(CategoryActivity.this).isConnected())
            getCategory();
        else
            Toast.makeText(CategoryActivity.this, "No internet Connection", Toast.LENGTH_SHORT).show();

        binding.includeBranch.imageBack.setVisibility(View.VISIBLE);
        binding.includeBranch.imageBack.setOnClickListener(v -> finish());

    }

    private void getCategory() {
        binding.progressBar.setVisibility(View.VISIBLE);
        categoryList = new ArrayList<>();
        StatusAPI branchApi = BaseURL.getStatusAPI();
        Call<List<Category>> call = branchApi.getCategory(Globals.userResponse.user.get(0).userId);

        call.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                binding.progressBar.setVisibility(View.GONE);

                if (response.code() == 200) {
                    categoryList = response.body();
                    adapter = new CategoryAdapter(getApplicationContext(), categoryList, CategoryActivity.this);
                    binding.recyclerViewBranch.setAdapter(adapter);

                }

            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                Log.e("GST", "" + t.getMessage());

            }
        });

    }

    @Override
    public void onItemClick(int position, View view) {

        if (NetworkStatus.getInstance(CategoryActivity.this).isConnected())
            postDispatch(position);
        else
            Toast.makeText(CategoryActivity.this, "No internet Connection", Toast.LENGTH_SHORT).show();

    }

    private void postDispatch(int pos) {

        binding.progressBar.setVisibility(View.VISIBLE);

        StatusAPI supplierAPI = BaseURL.getStatusAPI();

        JsonObject jsonObject = null;
        try {
            jsonObject = new JsonObject();
            jsonObject.addProperty("STOCKDISPATCHID", 0);
            jsonObject.addProperty("FROMBRANCHID", Globals.userResponse.user.get(0).branchId);
            jsonObject.addProperty("TOBRANCHID", branchId);
            jsonObject.addProperty("CATEGORYID", categoryList.get(pos).categoryId);
            jsonObject.addProperty("USERID", Globals.userResponse.user.get(0).userId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Call<ResponseBody> call = supplierAPI.saveDispatch(true, jsonObject);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                binding.progressBar.setVisibility(View.GONE);
                if (response.code() == 200) {
                    Intent h = new Intent(CategoryActivity.this, StockDispatchActivity.class);
                    h.putExtra("stockDispatchId", response.message());
                    startActivity(h);
                    finish();
                } else {
                    Toast.makeText(CategoryActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(CategoryActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
}