package com.nsretail.ui.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.google.zxing.client.android.Intents;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;
import com.nsretail.Globals;
import com.nsretail.R;
import com.nsretail.data.api.BaseURL;
import com.nsretail.data.api.StatusAPI;
import com.nsretail.data.model.BranchModel.Branch;
import com.nsretail.data.model.ItemDetail.BranchStock;
import com.nsretail.data.model.ItemDetail.ItemBranch;
import com.nsretail.data.model.ItemDetail.ItemCost;
import com.nsretail.data.model.ItemDetail.ItemResult;
import com.nsretail.data.model.ItemDetail.Offer;
import com.nsretail.data.model.ItemModel.Item;
import com.nsretail.data.model.ItemModel.ItemCode;
import com.nsretail.data.model.ItemModel.ItemModel;
import com.nsretail.data.model.ItemModel.ItemPrice;
import com.nsretail.databinding.ActivityItemDetailBinding;
import com.nsretail.ui.Interface.OnItemClickListener;
import com.nsretail.ui.activities.StockDispatch.CategoryActivity;
import com.nsretail.ui.adapter.CostPriceAdapter;
import com.nsretail.ui.adapter.ItemCodeAdapter;
import com.nsretail.ui.adapter.OfferDetailAdapter;
import com.nsretail.ui.adapter.PriceDetailAdapter;
import com.nsretail.ui.adapter.StockDetailsAdapter;
import com.nsretail.utils.NetworkStatus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemDetailActivity extends AppCompatActivity implements OnItemClickListener {

    ActivityItemDetailBinding binding;
    ActivityResultLauncher<ScanOptions> barcodeLauncher;
    List<Branch> branchList;
    ArrayList<String> branchNames;
    ArrayList<Item> itemList;
    ArrayList<ItemCode> itemCodeList;
    ArrayList<ItemPrice> itemPrices;
    ArrayList<Offer> offerList;
    ArrayList<ItemCost> itemCostList;
    ArrayList<BranchStock> stockList;
    Dialog dialog;
    int branchId;
    boolean isFocus;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityItemDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.includeItem.textTitle.setText("Item Details");
        binding.includeItem.imageBack.setVisibility(View.VISIBLE);
        binding.includeItem.imageCamera.setVisibility(View.VISIBLE);

        if (NetworkStatus.getInstance(ItemDetailActivity.this).isConnected())
            getBranch();
        else
            Toast.makeText(ItemDetailActivity.this, "No internet Connection", Toast.LENGTH_SHORT).show();


        binding.includeItem.imageBack.setOnClickListener(view -> finish());

        binding.selectBranch.setOnItemClickListener((adapterView, view, pos, l) -> {
            for (int i = 0; i < branchList.size(); i++) {
                if (branchList.get(i).branchName.equalsIgnoreCase(binding.selectBranch.getText().toString())) {
                    branchId = branchList.get(i).branchId;
                }
            }
        });

        binding.includeItem.imageCamera.setOnClickListener(view -> {
            ScanOptions options = new ScanOptions();
            options.setPrompt("Scan a barcode");
            options.setCameraId(0); // Use a specific camera of the device
            options.setOrientationLocked(true);
            barcodeLauncher.launch(options);
        });

        barcodeLauncher = registerForActivityResult(new ScanContract(), result -> {
            if (result.getContents() == null) {
                Intent originalIntent = result.getOriginalIntent();
                if (originalIntent == null) {
                    Log.d("MainActivity", "Cancelled scan");
                    Toast.makeText(ItemDetailActivity.this, "Cancelled", Toast.LENGTH_LONG).show();
                } else if (originalIntent.hasExtra(Intents.Scan.MISSING_CAMERA_PERMISSION)) {
                    Log.d("MainActivity", "Cancelled scan due to missing camera permission");
                    Toast.makeText(ItemDetailActivity.this, "Cancelled due to missing camera permission", Toast.LENGTH_LONG).show();
                }
            } else {
                Log.d("MainActivity", "Scanned");
                Toast.makeText(ItemDetailActivity.this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                binding.editEANCode.setText(result.getContents());
                if (NetworkStatus.getInstance(ItemDetailActivity.this).isConnected())
                    getItemData(result.getContents());
                else Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();

            }
        });


        binding.editEANCode.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (binding.editEANCode.getText().length() > 0) {
                    if (NetworkStatus.getInstance(ItemDetailActivity.this).isConnected())
                        getItemData(binding.editEANCode.getText().toString());
                    else
                        Toast.makeText(ItemDetailActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
                } else {
                    binding.editEANCode.setError("Enter Item code");
                }
                return true;
            } else if (actionId == EditorInfo.IME_NULL
                    && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                if (NetworkStatus.getInstance(ItemDetailActivity.this).isConnected())
                    getItemData(binding.editEANCode.getText().toString());
                else
                    Toast.makeText(ItemDetailActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
                return true;
            }

            return false;
        });

    }

    private void getBranch() {
        binding.progressBar.setVisibility(View.VISIBLE);
        StatusAPI branchApi = BaseURL.getStatusAPI();
        Call<ItemBranch> call = branchApi.getItemBranch(Globals.userResponse.user.get(0).userId);
        call.enqueue(new Callback<ItemBranch>() {
            @Override
            public void onResponse(Call<ItemBranch> call, Response<ItemBranch> response) {
                binding.progressBar.setVisibility(View.GONE);

                if (response.code() == 200) {

                    branchList = new ArrayList<>();
                    branchNames = new ArrayList<>();
                    branchList = response.body().branchlist;

                    for (int i = 0; i < branchList.size(); i++) {
                        branchNames.add(branchList.get(i).branchName);
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.item_spinner, branchNames);
                    if (binding.selectBranch != null) {
                        binding.selectBranch.setAdapter(adapter);
                    }

                    binding.selectBranch.requestFocus();

                    if (binding.priceLayout != null)
                        if (response.body().accessList.get(0).showItemPrice)
                            binding.priceLayout.setVisibility(View.VISIBLE);
                        else
                            binding.priceLayout.setVisibility(View.GONE);

                    if (binding.offerLayout != null)
                        if (response.body().accessList.get(0).showOffer)
                            binding.offerLayout.setVisibility(View.VISIBLE);
                        else
                            binding.offerLayout.setVisibility(View.GONE);

                    if (binding.costPriceLayout != null)
                        if (response.body().accessList.get(0).showCostPrice)
                            binding.costPriceLayout.setVisibility(View.VISIBLE);
                        else
                            binding.costPriceLayout.setVisibility(View.GONE);

                    if (binding.stockLayout != null)
                        if (response.body().accessList.get(0).showStock)
                            binding.stockLayout.setVisibility(View.VISIBLE);
                        else
                            binding.stockLayout.setVisibility(View.GONE);

                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ItemDetailActivity.this);
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
            public void onFailure(Call<ItemBranch> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                AlertDialog.Builder builder = new AlertDialog.Builder(ItemDetailActivity.this);
                try {
                    builder.setMessage(t.getMessage())
                            .setCancelable(false)
                            .setPositiveButton("OK", (dialog, id) ->
                                    dialog.cancel());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

    private void getItemData(String itemCode) {
        binding.progressBar.setVisibility(View.VISIBLE);
        itemList = new ArrayList<>();
        itemCodeList = new ArrayList<>();

        StatusAPI itemAPI = BaseURL.getStatusAPI();
        Call<ItemModel> call = itemAPI.getItemDetail(itemCode, binding.wareRadio.isChecked());
        call.enqueue(new Callback<ItemModel>() {
            @Override
            public void onResponse(Call<ItemModel> call, Response<ItemModel> response) {
                binding.progressBar.setVisibility(View.GONE);
                try {
                    if (response.code() == 200) {

                        itemList = response.body().itemList;
                        itemCodeList = response.body().itemCodeList;


                        if (itemCodeList.size() > 1) {
                            showDialog();
                        } else {
//                            binding.editEANCode.setText(itemCodeList.get(0).itemCode);

                            setEANCode(itemCodeList.get(0).itemCode);

                            if (NetworkStatus.getInstance(ItemDetailActivity.this).isConnected())
                                getItemPrice(itemCodeList.get(0).itemCodeId);
                            else
                                Toast.makeText(ItemDetailActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();

                        }

                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ItemDetailActivity.this);
                        builder.setMessage(response.errorBody().string()).setCancelable(false).setPositiveButton("OK", (dialog, id) -> {
                            dialog.cancel();
                            binding.editEANCode.setText("");
                            clearData();
                            binding.editEANCode.requestFocus();
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(Call<ItemModel> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                AlertDialog.Builder builder = new AlertDialog.Builder(ItemDetailActivity.this);
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

    private void setEANCode(String itemCode) {

        binding.editEANCode.setText(itemCode);

        binding.editItemName.setText(itemList.get(0).itemName);
        binding.editSKUCode.setText(itemList.get(0).skuCode);
        binding.editEANCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (binding.editEANCode.getText().length() > 0)
                    clearData();
            }
        });

    }

    private void showDialog() {

        dialog = new Dialog(ItemDetailActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_dialog);

        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        TextView textView = dialog.findViewById(R.id.textView);
        textView.setText("EAN Code List");

        ImageView imageCancel = dialog.findViewById(R.id.imageCancel);
        imageCancel.setOnClickListener(view -> {
            dialog.dismiss();
            binding.editEANCode.setText("");
            clearData();
            binding.editEANCode.requestFocus();
        });

        RecyclerView recyclerView = dialog.findViewById(R.id.recyclerViewItem);

        DividerItemDecoration horizontalDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        Drawable divider = ContextCompat.getDrawable(this, R.drawable.divider);
        if (divider != null) {
            horizontalDecoration.setDrawable(divider);
        }
        recyclerView.addItemDecoration(horizontalDecoration);

        ItemCodeAdapter adapter = new ItemCodeAdapter(dialog.getContext(), itemCodeList, ItemDetailActivity.this);
        recyclerView.setAdapter(adapter);

        dialog.show();
    }


    private void clearData() {
        isFocus = false;
        binding.editSKUCode.setText("");
        binding.editItemName.setText("");

        if (itemCodeList != null)
            itemCostList.clear();
        if (itemPrices != null)
            itemPrices.clear();
        if (offerList != null)
            offerList.clear();
        if (stockList != null) {
            stockList.clear();
            setPriceData();
        }

    }

    @Override
    public void onItemClick(int position, View view) {

//        binding.editEANCode.setText(itemCodeList.get(position).itemCode);

        setEANCode(itemCodeList.get(position).itemCode);


        if (NetworkStatus.getInstance(ItemDetailActivity.this).isConnected())
            getItemPrice(itemCodeList.get(position).itemCodeId);
        else Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();

        dialog.dismiss();
    }

    private void getItemPrice(int itemCodeId) {
        binding.progressBar.setVisibility(View.VISIBLE);
        itemList = new ArrayList<>();
        itemCodeList = new ArrayList<>();

        StatusAPI itemAPI = BaseURL.getStatusAPI();
        Call<ItemResult> call = itemAPI.getItemPrice(itemCodeId, branchId, binding.wareRadio.isChecked());
        call.enqueue(new Callback<ItemResult>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<ItemResult> call, Response<ItemResult> response) {
                binding.progressBar.setVisibility(View.GONE);
                try {
                    if (response.code() == 200) {

                        itemCostList = response.body().itemCostList;
                        itemPrices = response.body().itemPriceList;
                        offerList = response.body().offerList;
                        stockList = response.body().branchStockList;

                        setPriceData();

                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ItemDetailActivity.this);
                        builder.setMessage(response.errorBody().string()).setCancelable(false).setPositiveButton("OK", (dialog, id) -> {
                            dialog.cancel();
                            clearData();
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(Call<ItemResult> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);

            }
        });
    }

    private void setPriceData() {

        PriceDetailAdapter adapter = new PriceDetailAdapter(ItemDetailActivity.this, itemPrices);
        binding.recyclerViewPrice.setAdapter(adapter);

        CostPriceAdapter adapter1 = new CostPriceAdapter(ItemDetailActivity.this, itemCostList);
        binding.recyclerCostPrice.setAdapter(adapter1);

        OfferDetailAdapter adapter2 = new OfferDetailAdapter(ItemDetailActivity.this, offerList);
        binding.recyclerViewOffer.setAdapter(adapter2);

        StockDetailsAdapter adapter3 = new StockDetailsAdapter(ItemDetailActivity.this, stockList);
        binding.recyclerStock.setAdapter(adapter3);
    }

}