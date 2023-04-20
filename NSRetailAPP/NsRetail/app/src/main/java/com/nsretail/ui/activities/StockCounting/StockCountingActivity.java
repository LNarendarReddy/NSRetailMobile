package com.nsretail.ui.activities.StockCounting;

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
import com.nsretail.data.model.CountingModel.Counting;
import com.nsretail.data.model.CountingModel.CountingDetail;
import com.nsretail.data.model.CountingModel.CountingModel;
import com.nsretail.databinding.ActivityStockBinding;
import com.nsretail.ui.Interface.OnItemClickListener;
import com.nsretail.ui.adapter.StockCountingAdapter;
import com.nsretail.utils.NetworkStatus;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StockCountingActivity extends AppCompatActivity implements OnItemClickListener {

    ActivityStockBinding binding;
    StockCountingAdapter adapter;
    ArrayList<CountingModel> countingList;
    ArrayList<CountingDetail> countingDetails;
    Boolean isAllFabsVisible;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStockBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.includeStock.textTitle.setText("Stock Counting");


        if (NetworkStatus.getInstance(StockCountingActivity.this).isConnected())
            getDispatchData();
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
            Intent h = new Intent(StockCountingActivity.this, AddCountingItemActivity.class);
            h.putExtra("stockCountingId", countingList.get(0).stockCountingId);
            activityResult.launch(h);

            binding.addItemFab.hide();
            binding.addSubmitFab.hide();
            binding.addCancelFab.hide();
            binding.addSubmitActionText.setVisibility(View.GONE);
            binding.addItemActionText.setVisibility(View.GONE);
            binding.addCancelActionText.setVisibility(View.GONE);

            isAllFabsVisible = false;

        });

        binding.addSubmitFab.setOnClickListener(view -> {
            if (NetworkStatus.getInstance(StockCountingActivity.this).isConnected())
                updateDispatch();
            else
                Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
        });

        binding.addCancelFab.setOnClickListener(view -> {
            if (NetworkStatus.getInstance(StockCountingActivity.this).isConnected())
                discardDispatch();
            else
                Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
        });

    }

    private void updateDispatch() {
        binding.progressBar.setVisibility(View.VISIBLE);
        StatusAPI updateAPI = BaseURL.getStatusAPI();
        Call<ResponseBody> call = updateAPI.updateCounting(countingList.get(0).stockCountingId, Globals.userResponse.user.get(0).userId);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                binding.progressBar.setVisibility(View.GONE);
                if (response.code() == 200) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(StockCountingActivity.this, R.style.AlertDialogCustom);
                    try {
                        builder.setMessage(response.body().string())
                                .setCancelable(false)
                                .setPositiveButton("OK", (dialog, id) -> {
                                    finish();
                                    dialog.cancel();
                                });
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    AlertDialog alert = builder.create();
                    alert.show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(StockCountingActivity.this);
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
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                AlertDialog.Builder builder = new AlertDialog.Builder(StockCountingActivity.this);
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

    private void discardDispatch() {
        binding.progressBar.setVisibility(View.VISIBLE);
        StatusAPI api = BaseURL.getStatusAPI();
        Call<ResponseBody> call = api.discardCounting(countingList.get(0).stockCountingId, Globals.userResponse.user.get(0).userId);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                binding.progressBar.setVisibility(View.GONE);
                if (response.code() == 200) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(StockCountingActivity.this, R.style.AlertDialogCustom);
                    try {
                        builder.setMessage(response.body().string())
                                .setCancelable(false)
                                .setPositiveButton("OK", (dialog, id) -> {
                                    finish();
                                    dialog.cancel();
                                });
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    AlertDialog alert = builder.create();
                    alert.show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(StockCountingActivity.this);
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
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                AlertDialog.Builder builder = new AlertDialog.Builder(StockCountingActivity.this);
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

    private void getDispatchData() {

        countingList = new ArrayList<>();
        countingDetails = new ArrayList<>();
        StatusAPI dispatchAPI = BaseURL.getStatusAPI();

        Call<Counting> call = dispatchAPI.getCounting(Globals.userResponse.user.get(0).userId);
        call.enqueue(new Callback<Counting>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<Counting> call, Response<Counting> response) {

                if (response.code() == 200) {

                    countingList = response.body().countingList;
                    countingDetails = response.body().countingDetail;

                    binding.textDealerName.setText(countingList.get(0).branchName + " / " + countingList.get(0).stockCountingId);

                    adapter = new StockCountingAdapter(StockCountingActivity.this, countingDetails, StockCountingActivity.this);
                    binding.recyclerViewStock.setAdapter(adapter);
                } else {
                    Toast.makeText(StockCountingActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<Counting> call, Throwable t) {
                Toast.makeText(StockCountingActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }


    ActivityResultLauncher<Intent> activityResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == 112) {
            Intent data = result.getData();
            if (data != null) {
                if (data.getBooleanExtra("refresh", false)) {
                    if (NetworkStatus.getInstance(StockCountingActivity.this).isConnected())
                        getDispatchData();
                    else
                        Toast.makeText(this, "Please check your internet connection", Toast.LENGTH_SHORT).show();

                }
            }
        }
    });

    public void updateItemData(int pos) {
        Intent h = new Intent(StockCountingActivity.this, AddCountingItemActivity.class);
        h.putExtra("stockCountingItem", countingDetails.get(pos));
        h.putExtra("stockCountingId", countingList.get(0).stockCountingId);
        activityResult.launch(h);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onItemClick(int position, View view) {
        countingDetails.remove(position);
        adapter.notifyDataSetChanged();
    }
}
