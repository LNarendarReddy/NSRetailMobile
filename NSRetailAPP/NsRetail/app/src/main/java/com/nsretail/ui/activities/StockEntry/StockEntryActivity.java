package com.nsretail.ui.activities.StockEntry;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.nsretail.Globals;
import com.nsretail.R;
import com.nsretail.data.api.BaseURL;
import com.nsretail.data.api.StatusAPI;
import com.nsretail.data.model.StockEntry.Invoice;
import com.nsretail.data.model.StockEntry.StockEntry;
import com.nsretail.data.model.StockEntry.StockEntryDetail;
import com.nsretail.databinding.ActivityStockBinding;
import com.nsretail.ui.Interface.OnItemClickListener;
import com.nsretail.ui.adapter.StockEntryAdapter;
import com.nsretail.utils.NetworkStatus;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StockEntryActivity extends AppCompatActivity implements OnItemClickListener {

    ActivityStockBinding binding;
    StockEntryAdapter adapter;
    ArrayList<StockEntry> stockEntry;
    ArrayList<StockEntryDetail> stockEntryDetails;
    Boolean isAllFabsVisible;
    int isIGST;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStockBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.includeStock.textTitle.setText("Stock Entry");

        if (getIntent() != null) {
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
            Intent h = new Intent(StockEntryActivity.this, AddStockEntryActivity.class);
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

        binding.addSubmitFab.setOnClickListener(view -> {
            Intent h = new Intent(StockEntryActivity.this, StockPreviewActivity.class);
            h.putExtra("Suppiler", stockEntry.get(0));
            h.putExtra("StockEntryDetail", stockEntryDetails);
            startActivity(h);

            binding.addItemFab.hide();
            binding.addSubmitFab.hide();
            binding.addCancelFab.hide();
            binding.addSubmitActionText.setVisibility(View.GONE);
            binding.addItemActionText.setVisibility(View.GONE);
            binding.addCancelActionText.setVisibility(View.GONE);

            isAllFabsVisible = false;

        });

        binding.addCancelFab.setOnClickListener(view -> {
            if (NetworkStatus.getInstance(StockEntryActivity.this).isConnected())
                discardDispatch();
            else
                Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
        });

    }

    private void discardDispatch() {
        binding.progressBar.setVisibility(View.VISIBLE);
        StatusAPI api = BaseURL.getStatusAPI();
        Call<ResponseBody> call = api.discardEntry(stockEntry.get(0).stockEntryId, Globals.userResponse.user.get(0).userId, true);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                binding.progressBar.setVisibility(View.GONE);
                try {
                    if (response.code() == 200) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(StockEntryActivity.this, R.style.AlertDialogCustom);

                        builder.setMessage(response.body().string())
                                .setCancelable(false)
                                .setPositiveButton("OK", (dialog, id) -> {
                                    finish();
                                    dialog.cancel();
                                });

                        AlertDialog alert = builder.create();
                        alert.show();
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(StockEntryActivity.this);
                        builder.setMessage(response.errorBody().string())
                                .setCancelable(false)
                                .setPositiveButton("OK", (dialog, id) -> {
                                    dialog.cancel();
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                AlertDialog.Builder builder = new AlertDialog.Builder(StockEntryActivity.this);
                builder.setMessage(t.getMessage())
                        .setCancelable(false)
                        .setPositiveButton("OK", (dialog, id) -> {
                            dialog.cancel();
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

    }


    private void getStockData() {

        stockEntry = new ArrayList<>();
        stockEntryDetails = new ArrayList<>();
        StatusAPI stockAPI = BaseURL.getStatusAPI();

        Call<Invoice> call = stockAPI.getInvoiceData(Globals.userResponse.user.get(0).categoryId,
                Globals.userResponse.user.get(0).userId, true);

        call.enqueue(new Callback<Invoice>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<Invoice> call, Response<Invoice> response) {

                if (response.code() == 200) {

                    stockEntry = response.body().stockEntry;
                    stockEntryDetails = response.body().stockEntryDetails;

                    binding.textDealerName.setText("Dealer Name : " + stockEntry.get(0).dealerName);

                    adapter = new StockEntryAdapter(StockEntryActivity.this, response.body().getStockEntryDetails(), StockEntryActivity.this);
                    binding.recyclerViewStock.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<Invoice> call, Throwable t) {

            }
        });
    }

    ActivityResultLauncher<Intent> activityResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == 113) {
            Intent data = result.getData();
            if (data != null) {
                if (data.getBooleanExtra("refresh", false)) {
                    if (NetworkStatus.getInstance(StockEntryActivity.this).isConnected())
                        getStockData();
                    else
                        Toast.makeText(this, "Please check your internet connection", Toast.LENGTH_SHORT).show();

                }
            }
        }
    });

    public void updateItemData(int pos) {
        Intent h = new Intent(StockEntryActivity.this, AddStockEntryActivity.class);
        h.putExtra("stockEntryDetail", stockEntryDetails.get(pos));
        h.putExtra("stockEntry", stockEntry);
        h.putExtra("isIGST", isIGST);
        activityResult.launch(h);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onItemClick(int position, View view) {
        stockEntryDetails.remove(position);
        adapter.notifyDataSetChanged();
    }
}
