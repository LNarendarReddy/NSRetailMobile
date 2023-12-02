package com.nsretail.ui.activities.StockDispatch;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;

import com.nsretail.Globals;
import com.nsretail.R;
import com.nsretail.data.api.BaseURL;
import com.nsretail.data.api.StatusAPI;
import com.nsretail.data.model.DispatchModel.Dispatch;
import com.nsretail.data.model.DispatchModel.DispatchDetail;
import com.nsretail.data.model.DispatchModel.DispatchModel;
import com.nsretail.databinding.ActivityStockBinding;
import com.nsretail.ui.Interface.OnItemClickListener;
import com.nsretail.ui.activities.StockCounting.StockCountingActivity;
import com.nsretail.ui.adapter.StockDispatchAdapter;
import com.nsretail.utils.NetworkStatus;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StockDispatchActivity extends AppCompatActivity implements OnItemClickListener {

    ActivityStockBinding binding;
    StockDispatchAdapter adapter;
    ArrayList<DispatchModel> dispatchList;
    ArrayList<DispatchDetail> dispatchDetail;
    ArrayList<DispatchDetail> filteredData;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStockBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.includeStock.textTitle.setText("Stock Dispatch");

        DividerItemDecoration horizontalDecoration = new DividerItemDecoration(binding.recyclerViewStock.getContext(),
                DividerItemDecoration.VERTICAL);
        Drawable divider = ContextCompat.getDrawable(this, R.drawable.divider);
        if (divider != null) {
            horizontalDecoration.setDrawable(divider);
        }
        binding.recyclerViewStock.addItemDecoration(horizontalDecoration);

        if (NetworkStatus.getInstance(StockDispatchActivity.this).isConnected())
            getDispatchData();
        else
            Toast.makeText(this, "Please check your internet connection", Toast.LENGTH_SHORT).show();

        binding.includeStock.searchView.setVisibility(View.VISIBLE);

        binding.includeStock.imageBack.setOnClickListener(view -> finish());

        binding.addItemFab.setOnClickListener(view -> {
            Intent h = new Intent(StockDispatchActivity.this, AddStockItemActivity.class);
            h.putExtra("stockDispatchId", dispatchList.get(0).stockDispatchId);
            h.putExtra("categoryId", dispatchList.get(0).categoryId);
            h.putExtra("fromBranchId", dispatchList.get(0).fromBranchId);
            h.putExtra("toBranchId", dispatchList.get(0).toBranchId);
            activityResult.launch(h);
        });

        binding.includeStock.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if (newText.length() > 0) {
                    searchData(newText);
                }
                return false;
            }
        });

        binding.addSubmitFab.setOnClickListener(view -> {
            if (NetworkStatus.getInstance(StockDispatchActivity.this).isConnected()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(StockDispatchActivity.this, R.style.AlertDialogCustom);
                builder.setMessage("Are you sure you want to Update the stock?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", (dialog, id) -> {
                            updateDispatch();
                            dialog.cancel();
                        }).setNegativeButton("No", (dialogInterface, i) -> dialogInterface.cancel());
                AlertDialog alert = builder.create();
                alert.show();
            } else
                Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
        });

        binding.addCancelFab.setOnClickListener(view -> {
            if (NetworkStatus.getInstance(StockDispatchActivity.this).isConnected()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(StockDispatchActivity.this, R.style.AlertDialogCustom);
                builder.setMessage("Are you sure you want to Discard the stock?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", (dialog, id) -> {
                            discardDispatch();
                            dialog.cancel();
                        }).setNegativeButton("No", (dialogInterface, i) -> dialogInterface.cancel());
                AlertDialog alert = builder.create();
                alert.show();
            } else
                Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
        });

    }

    private void updateDispatch() {
        binding.progressBar.setVisibility(View.VISIBLE);
        StatusAPI updateAPI = BaseURL.getStatusAPI();
        Call<ResponseBody> call = updateAPI.updateDispatch(dispatchList.get(0).stockDispatchId, true);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                binding.progressBar.setVisibility(View.GONE);
                if (response.code() == 200) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(StockDispatchActivity.this, R.style.AlertDialogCustom);
                    try {
                        builder.setMessage(response.body().string())
                                .setCancelable(false)
                                .setPositiveButton("OK", (dialog, id) -> {
                                    finish();
                                    dialog.cancel();
                                });
                    } catch (Exception e) {
                       e.printStackTrace();
                    }
                    AlertDialog alert = builder.create();
                    alert.show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(StockDispatchActivity.this);
                    try {
                        builder.setMessage(response.errorBody().string())
                                .setCancelable(false)
                                .setPositiveButton("OK", (dialog, id) -> {
                                    dialog.cancel();
                                });
                    } catch (Exception e) {
                       e.printStackTrace();
                    }
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                AlertDialog.Builder builder = new AlertDialog.Builder(StockDispatchActivity.this);
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

    private void discardDispatch() {
        binding.progressBar.setVisibility(View.VISIBLE);
        StatusAPI api = BaseURL.getStatusAPI();
        Call<ResponseBody> call = api.discardDispatch(dispatchList.get(0).stockDispatchId, Globals.userResponse.user.get(0).userId, true);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                binding.progressBar.setVisibility(View.GONE);
                if (response.code() == 200) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(StockDispatchActivity.this, R.style.AlertDialogCustom);
                    try {
                        builder.setMessage(response.body().string())
                                .setCancelable(false)
                                .setPositiveButton("OK", (dialog, id) -> {
                                    finish();
                                    dialog.cancel();
                                });
                    } catch (Exception e) {
                       e.printStackTrace();
                    }
                    AlertDialog alert = builder.create();
                    alert.show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(StockDispatchActivity.this);
                    try {
                        builder.setMessage(response.errorBody().string())
                                .setCancelable(false)
                                .setPositiveButton("OK", (dialog, id) -> {
                                    dialog.cancel();
                                });
                    } catch (Exception e) {
                       e.printStackTrace();
                    }
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                AlertDialog.Builder builder = new AlertDialog.Builder(StockDispatchActivity.this);
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

    private void getDispatchData() {
        binding.progressBar.setVisibility(View.VISIBLE);

        dispatchList = new ArrayList<>();
        dispatchDetail = new ArrayList<>();
        StatusAPI dispatchAPI = BaseURL.getStatusAPI();

        Call<Dispatch> call = dispatchAPI.getDispatch(Globals.userResponse.user.get(0).categoryId,
                Globals.userResponse.user.get(0).userId
                , true);

        call.enqueue(new Callback<Dispatch>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<Dispatch> call, Response<Dispatch> response) {
                binding.progressBar.setVisibility(View.GONE);

                if (response.code() == 200) {

                    dispatchList = response.body().dispatchList;
                    dispatchDetail = response.body().dispatchDetail;

                    binding.textDealerName.setText(dispatchList.get(0).toBranchName + " / " +
                            dispatchList.get(0).categoryName + " / " + dispatchList.get(0).stockDispatchId);

                    adapter = new StockDispatchAdapter(StockDispatchActivity.this, dispatchDetail, StockDispatchActivity.this);
                    binding.recyclerViewStock.setAdapter(adapter);
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(StockDispatchActivity.this);
                    builder.setMessage(response.message())
                            .setCancelable(false)
                            .setPositiveButton("OK", (dialog, id) -> {
                                dialog.cancel();
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }

            }

            @Override
            public void onFailure(Call<Dispatch> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                AlertDialog.Builder builder = new AlertDialog.Builder(StockDispatchActivity.this);
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

    @SuppressLint("NotifyDataSetChanged")
    ActivityResultLauncher<Intent> activityResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == 112) {
            Intent data = result.getData();
            if (data != null) {
                if (data.getBooleanExtra("refresh", false)) {
                    binding.includeStock.searchView.setIconified(true);
                    binding.includeStock.searchView.onActionViewCollapsed();
                    if (dispatchDetail.size() > 0){
                        dispatchDetail.clear();
                        adapter.notifyDataSetChanged();
                    }
                    if (NetworkStatus.getInstance(StockDispatchActivity.this).isConnected())
                        getDispatchData();
                    else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(StockDispatchActivity.this);
                        builder.setMessage("Please check your internet connection")
                                .setCancelable(false)
                                .setPositiveButton("OK", (dialog, id) -> {
                                    dialog.cancel();
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }

                }
            }
        }
    });

    public void searchData(String searchText) {
        filteredData = new ArrayList<>();

        for (DispatchDetail detail : dispatchDetail) {
            if (detail.itemCode.contains(searchText) || detail.itemName.toLowerCase().contains(searchText.toLowerCase())) {
                filteredData.add(detail);
            }
        }

        adapter.updateList(filteredData);

    }

    public void updateItemData(int pos) {
        Intent h = new Intent(StockDispatchActivity.this, AddStockItemActivity.class);
        if (filteredData != null) {
            if (filteredData.size() > 0)
                h.putExtra("stockDispatchItem", filteredData.get(pos));
            else
                h.putExtra("stockDispatchItem", dispatchDetail.get(pos));
        } else {
            h.putExtra("stockDispatchItem", dispatchDetail.get(pos));
        }
        h.putExtra("stockDispatchId", dispatchList.get(0).stockDispatchId);
        h.putExtra("categoryId", dispatchList.get(0).categoryId);
        h.putExtra("fromBranchId", dispatchList.get(0).fromBranchId);
        h.putExtra("toBranchId", dispatchList.get(0).toBranchId);
        activityResult.launch(h);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onItemClick(int position, View view) {
        dispatchDetail.remove(position);
        adapter.notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void deleteItem(int position){
        binding.progressBar.setVisibility(View.VISIBLE);
        StatusAPI deleteAPI = BaseURL.getStatusAPI();

        Call<ResponseBody> call;

        if (filteredData != null) {
            if (filteredData.size() > 0) {
                call = deleteAPI.deleteDispatch(filteredData.get(position).stockDispatchDetailId, true);
            } else {
                call = deleteAPI.deleteDispatch(dispatchDetail.get(position).stockDispatchDetailId, true);
            }
        } else {
            call = deleteAPI.deleteDispatch(dispatchDetail.get(position).stockDispatchDetailId, true);
        }

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                binding.progressBar.setVisibility(View.GONE);
                if (response.code() == 200) {

                    try {
                        Toast.makeText(StockDispatchActivity.this, response.body().string(), Toast.LENGTH_SHORT).show();
                        dispatchDetail.remove(position);
                        adapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(StockDispatchActivity.this);
                    try {
                        builder.setMessage(response.errorBody().string())
                                .setCancelable(false)
                                .setPositiveButton("OK", (dialog, id) -> {
                                    dialog.cancel();
                                });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                AlertDialog.Builder builder = new AlertDialog.Builder(StockDispatchActivity.this);
                if (t.getMessage().equalsIgnoreCase("Failed to connect to nsoftsol.com/122.175.62.71:6002")) {
                    builder.setMessage("Network Issue!!").setCancelable(false).setPositiveButton("OK", (dialog, id) -> {
                        dialog.cancel();
                    });
                } else {
                    if (t.getMessage().toString().length() > 0) {
                        builder.setMessage(t.getMessage()).setCancelable(false).setPositiveButton("OK", (dialog, id) -> {
                            dialog.cancel();
                        });
                    } else {
                        try {
                            builder.setMessage("Empty response : " + call.execute().code())
                                    .setCancelable(false)
                                    .setPositiveButton("OK", (dialog, id) -> {
                                        dialog.cancel();
                                    });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                AlertDialog alert = builder.create();
                alert.show();

            }
        });
    }

}
