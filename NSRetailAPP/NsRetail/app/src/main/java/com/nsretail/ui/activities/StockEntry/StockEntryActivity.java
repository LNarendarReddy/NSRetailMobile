package com.nsretail.ui.activities.StockEntry;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.nsretail.Globals;
import com.nsretail.data.api.BaseURL;
import com.nsretail.data.api.StatusAPI;
import com.nsretail.data.model.StockEntry.Invoice;
import com.nsretail.data.model.StockEntry.StockEntry;
import com.nsretail.databinding.ActivityStockBinding;
import com.nsretail.ui.adapter.StockEntryAdapter;
import com.nsretail.utils.NetworkStatus;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StockEntryActivity extends AppCompatActivity {

    ActivityStockBinding binding;
    StockEntryAdapter adapter;
    ArrayList<StockEntry> stockEntry;
    Boolean isAllFabsVisible;
    int isIGST;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStockBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.includeStock.textTitle.setText("Stock Entry");

        if (getIntent() != null){
            isIGST = getIntent().getIntExtra("isIGST", -1);
        }

        if (NetworkStatus.getInstance(StockEntryActivity.this).isConnected())
            getStockData();
        else
            Toast.makeText(this, "Please check your internet connection", Toast.LENGTH_SHORT).show();


        binding.addItemFab.setVisibility(View.GONE);
        binding.addSubmitFab.setVisibility(View.GONE);
        binding.addCancelFab.setVisibility(View.GONE);
        binding.addSubmitActionText.setVisibility(View.GONE);
        binding.addItemActionText.setVisibility(View.GONE);
        binding.addCancelActionText.setVisibility(View.GONE);

        isAllFabsVisible = false;

        binding.includeStock.imageBack.setOnClickListener(view -> finish());

        binding.addFab.setOnClickListener(view -> {
            if (!isAllFabsVisible) {

                binding.addItemFab.show();
                binding.addSubmitFab.show();
                binding.addCancelFab.show();
                binding.addSubmitActionText.setVisibility(View.VISIBLE);
                binding.addItemActionText.setVisibility(View.VISIBLE);
                binding.addCancelActionText.setVisibility(View.VISIBLE);

                isAllFabsVisible = true;

            } else {
                binding.addItemFab.hide();
                binding.addSubmitFab.hide();
                binding.addCancelFab.hide();
                binding.addSubmitActionText.setVisibility(View.GONE);
                binding.addItemActionText.setVisibility(View.GONE);
                binding.addCancelActionText.setVisibility(View.GONE);

                isAllFabsVisible = false;
            }
        });

        binding.addItemFab.setOnClickListener(view -> {
            Intent h= new Intent(StockEntryActivity.this, AddStockEntryActivity.class);
            h.putExtra("stockEntry", stockEntry);
            h.putExtra("isIGST", isIGST);
            startActivity(h);

            binding.addItemFab.hide();
            binding.addSubmitFab.hide();
            binding.addCancelFab.hide();
            binding.addSubmitActionText.setVisibility(View.GONE);
            binding.addItemActionText.setVisibility(View.GONE);
            binding.addCancelActionText.setVisibility(View.GONE);

            isAllFabsVisible = false;

        });

    }

    private void getStockData() {

        stockEntry = new ArrayList<>();
        StatusAPI stockAPI = BaseURL.getStatusAPI();

        Call<Invoice> call = stockAPI.getInvoiceData(Globals.userResponse.user.get(0).categoryId,
                Globals.userResponse.user.get(0).userId, true);

        call.enqueue(new Callback<Invoice>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<Invoice> call, Response<Invoice> response) {

                if (response.code() == 200) {

                    stockEntry =  response.body().stockEntry;

                    binding.textDealerName.setText("Dealer Name : " + stockEntry.get(0).dealerName);

                    adapter = new StockEntryAdapter(StockEntryActivity.this, response.body().getStockEntryDetails());
                    binding.recyclerViewStock.setAdapter(adapter);
                }

            }

            @Override
            public void onFailure(Call<Invoice> call, Throwable t) {

            }
        });


    }
}
