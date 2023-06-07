package com.nsretail.ui.activities.StockCounting;

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
import com.nsretail.R;
import com.nsretail.data.api.BaseURL;
import com.nsretail.data.api.StatusAPI;
import com.nsretail.data.model.CountingModel.CountingDetail;
import com.nsretail.data.model.ItemModel.Item;
import com.nsretail.data.model.ItemModel.ItemCode;
import com.nsretail.data.model.ItemModel.ItemModel;
import com.nsretail.data.model.ItemModel.ItemPrice;
import com.nsretail.databinding.ActivityAddItemBinding;
import com.nsretail.ui.Interface.OnItemClickListener;
import com.nsretail.ui.adapter.ItemCodeAdapter;
import com.nsretail.ui.adapter.ItemPriceAdapter;
import com.nsretail.utils.NetworkStatus;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddCountingItemActivity extends AppCompatActivity implements OnItemClickListener {

    ActivityAddItemBinding binding;
    ArrayList<Item> itemList;
    ArrayList<ItemCode> itemCodeList;
    ArrayList<ItemPrice> itemPriceList;
    ArrayList<ItemPrice> itemPriceSelectedList;
    ActivityResultLauncher<ScanOptions> barcodeLauncher;
    CountingDetail countingDetail;
    Dialog dialog, dialogPrice;
    int stockCountingId, categoryId, itemPriceId, stockDetailId;
    boolean isUpdateItem;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddItemBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.includeItemAdd.textTitle.setText("Add Stock Counting");
        binding.includeItemAdd.imageCamera.setVisibility(View.VISIBLE);

        binding.editTrayNo.setVisibility(View.GONE);

        if (getIntent() != null) {
            countingDetail = (CountingDetail) getIntent().getSerializableExtra("stockCountingItem");
            stockCountingId = getIntent().getIntExtra("stockCountingId", -1);
        }

        if (countingDetail != null) {
            isUpdateItem = true;
//            binding.editTrayNo.setEnabled(false);
            binding.editEANCode.setEnabled(false);
            binding.editItemName.setEnabled(false);
            binding.editMRP.setEnabled(false);
            binding.editSalePrice.setEnabled(false);

            if (countingDetail.isOpenItem) {
                binding.editWeight.setEnabled(true);
                binding.editQuantity.setEnabled(false);
                binding.editWeight.setText("" + countingDetail.weightInKgs);
            } else {
                binding.editWeight.setEnabled(false);
                binding.editQuantity.setEnabled(true);
                binding.editQuantity.setText("" + countingDetail.quantity);
            }

            stockDetailId = countingDetail.stockCountingDetailId;
            itemPriceId = countingDetail.itemPriceId;
//            binding.editTrayNo.setText("" + countingDetail.trayNumber);
            binding.editEANCode.setText(countingDetail.itemCode);
            binding.editItemName.setText(countingDetail.itemName);
            binding.editMRP.setText("" + countingDetail.mrp);
            binding.editSalePrice.setText("" + countingDetail.salePrice);

        } else {
            isUpdateItem = false;
            stockDetailId = 0;
//            binding.editTrayNo.setEnabled(true);
            binding.editEANCode.setEnabled(true);
            binding.editItemName.setEnabled(false);
            binding.editMRP.setEnabled(false);
            binding.editSalePrice.setEnabled(false);

            binding.editEANCode.requestFocus();
        }

        binding.includeItemAdd.imageBack.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.putExtra("refresh", true);
            setResult(112, intent);
            finish();
        });

        binding.includeItemAdd.imageCamera.setOnClickListener(view -> {
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
                    Toast.makeText(AddCountingItemActivity.this, "Cancelled", Toast.LENGTH_LONG).show();
                } else if (originalIntent.hasExtra(Intents.Scan.MISSING_CAMERA_PERMISSION)) {
                    Log.d("MainActivity", "Cancelled scan due to missing camera permission");
                    Toast.makeText(AddCountingItemActivity.this, "Cancelled due to missing camera permission", Toast.LENGTH_LONG).show();
                }
            } else {
                Log.d("MainActivity", "Scanned");
                Toast.makeText(AddCountingItemActivity.this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                binding.editEANCode.setText(result.getContents());
                if (NetworkStatus.getInstance(AddCountingItemActivity.this).isConnected())
                    getItemData(result.getContents());
                else Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();

            }
        });


        binding.editEANCode.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (binding.editEANCode.getText().length() > 0) {
                    if (NetworkStatus.getInstance(AddCountingItemActivity.this).isConnected())
                        getItemData(binding.editEANCode.getText().toString());
                    else
                        Toast.makeText(AddCountingItemActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
                } else {
                    binding.editEANCode.setError("Enter Item code");
                }
                return true;
            } else if (actionId == EditorInfo.IME_NULL
                    && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                if (NetworkStatus.getInstance(AddCountingItemActivity.this).isConnected())
                    getItemData(binding.editEANCode.getText().toString());
                else
                    Toast.makeText(AddCountingItemActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
                return true;
            }

            return false;
        });

        binding.editEANCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
//                if (binding.editEANCode.getText().length() > 0) {
                    clearData();
//                }
            }
        });

        binding.buttonSave.setOnClickListener(view -> {
//            if (binding.editTrayNo.getText().length() > 0) {
            if (binding.editEANCode.getText().length() > 0) {
                if (binding.editQuantity.isEnabled()) {
                    if (binding.editQuantity.getText().length() > 0) {
                        if (NetworkStatus.getInstance(AddCountingItemActivity.this).isConnected())
                            saveItemStock();
                        else
                            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                    } else {
                        binding.editQuantity.setError("Enter Quantity");
                    }
                } else {
                    if (binding.editWeight.getText().length() > 0) {
                        if (NetworkStatus.getInstance(AddCountingItemActivity.this).isConnected())
                            saveItemStock();
                        else
                            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                    } else {
                        binding.editWeight.setError("Enter Weight in KGs");
                    }
                }
            } else {
                binding.editEANCode.setError("Enter EAN Code");
            }
//            } else {
//                binding.editTrayNo.setError("Enter Tray No");
//            }
        });

    }

    private void saveItemStock() {

        binding.progressBar.setVisibility(View.VISIBLE);

        StatusAPI saveItemAPI = BaseURL.getStatusAPI();

        int quantity, weightInKgs;

        if (binding.editQuantity.isEnabled()) {
            quantity = Integer.parseInt(binding.editQuantity.getText().toString());
            weightInKgs = 0;
        } else {
            weightInKgs = Integer.parseInt(binding.editWeight.getText().toString());
            quantity = 1;
        }

        Call<ResponseBody> call = saveItemAPI.saveItemCounting(stockDetailId, stockCountingId, itemPriceId,
                quantity, weightInKgs);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                binding.progressBar.setVisibility(View.GONE);
                try {
                    if (response.code() == 200) {

                        if (response.body().string().equalsIgnoreCase("Successfully saved")) {
                            Toast.makeText(AddCountingItemActivity.this, "Successfully saved", Toast.LENGTH_SHORT).show();

                            binding.editEANCode.setText("");
                            clearData();
                            binding.editEANCode.requestFocus();
                        }

                    } else {

                        AlertDialog.Builder builder = new AlertDialog.Builder(AddCountingItemActivity.this);
                        builder.setMessage(response.errorBody().string())
                                .setCancelable(false)
                                .setPositiveButton("OK", (dialog, id) -> {
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
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(AddCountingItemActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void clearData() {

        if (isUpdateItem) {
//            binding.editTrayNo.setText("");
//            binding.editTrayNo.setEnabled(true);
            binding.editEANCode.setEnabled(true);
            binding.editItemName.setEnabled(true);
            binding.editQuantity.setEnabled(true);
            binding.editWeight.setEnabled(true);
            binding.editSalePrice.setEnabled(true);
            binding.editMRP.setEnabled(true);
        }

        binding.editItemName.setText("");
        binding.editQuantity.setText("");
        binding.editWeight.setText("");
        binding.editMRP.setText("");
        binding.editSalePrice.setText("");

    }

    private void getItemData(String itemCode) {
        binding.progressBar.setVisibility(View.VISIBLE);
        itemList = new ArrayList<>();
        itemCodeList = new ArrayList<>();
        itemPriceList = new ArrayList<>();
        itemPriceSelectedList = new ArrayList<>();

        StatusAPI itemAPI = BaseURL.getStatusAPI();
        Call<ItemModel> call = itemAPI.getCountingItemData(itemCode);

        call.enqueue(new Callback<ItemModel>() {
            @Override
            public void onResponse(Call<ItemModel> call, Response<ItemModel> response) {
                binding.progressBar.setVisibility(View.GONE);
                if (response.code() == 200) {

                    itemList = response.body().itemList;
                    itemCodeList = response.body().itemCodeList;
                    itemPriceList = response.body().itemPriceList;

//                    showItemCodeDialog();

                    if (itemCodeList.size() > 1) {
                        showItemCodeDialog();
                    } else {
                        binding.editEANCode.setText(itemCodeList.get(0).itemCode);
                        selectPriceData(0);
                    }
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(AddCountingItemActivity.this, R.style.AlertDialogCustom);
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
            public void onFailure(Call<ItemModel> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);

            }
        });

    }

    private void itemData() {

        binding.editItemName.setText(itemList.get(0).itemName);

        if (itemList.get(0).isOpenItem) {
            binding.editWeight.setEnabled(true);
            binding.editQuantity.setEnabled(false);
//            binding.editWeight.setText("1");
        } else {
            binding.editWeight.setEnabled(false);
            binding.editQuantity.setEnabled(true);
//            binding.editQuantity.setText("1");
        }
    }

    private void showItemCodeDialog() {

        dialog = new Dialog(AddCountingItemActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_dialog);

        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        TextView textView = dialog.findViewById(R.id.textView);
        textView.setText("EAN Code List");

        RecyclerView recyclerView = dialog.findViewById(R.id.recyclerViewItem);

        DividerItemDecoration horizontalDecoration = new DividerItemDecoration(recyclerView.getContext(),
                DividerItemDecoration.VERTICAL);
        Drawable divider = ContextCompat.getDrawable(this, R.drawable.divider);
        if (divider != null) {
            horizontalDecoration.setDrawable(divider);
        }
        recyclerView.addItemDecoration(horizontalDecoration);

        ItemCodeAdapter adapter = new ItemCodeAdapter(dialog.getContext(), itemCodeList, AddCountingItemActivity.this);
        recyclerView.setAdapter(adapter);

        dialog.show();
    }

    @SuppressLint("SetTextI18n")
    private void selectPriceData(int pos) {

        for (int i = 0; i < itemPriceList.size(); i++) {
            if (itemCodeList.get(pos).itemCodeId == itemPriceList.get(i).itemCodeId) {
                itemPriceSelectedList.add(itemPriceList.get(i));
            }
        }

//        showPriceDialog();
        if (itemPriceSelectedList.size() > 1) {
            showPriceDialog();
        } else {
            itemPriceId = itemPriceList.get(0).itemPriceId;
            binding.editMRP.setText("" + itemPriceList.get(0).mrp);
            binding.editSalePrice.setText("" + itemPriceList.get(0).salePrice);
            binding.editQuantity.setText("");
            binding.editWeight.setText("");
            itemData();
        }

        if (itemList.get(0).isOpenItem) {
            binding.editWeight.requestFocus();
        } else {
            binding.editQuantity.requestFocus();
        }

    }

    @SuppressLint("SetTextI18n")
    private void showPriceDialog() {

        dialogPrice = new Dialog(AddCountingItemActivity.this);
        dialogPrice.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogPrice.setCancelable(false);
        dialogPrice.setContentView(R.layout.custom_dialog);

        Window window = dialogPrice.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);


        TextView textView = dialogPrice.findViewById(R.id.textView);
        textView.setText("MRP List");

        RecyclerView recyclerView = dialogPrice.findViewById(R.id.recyclerViewItem);

        DividerItemDecoration horizontalDecoration = new DividerItemDecoration(recyclerView.getContext(),
                DividerItemDecoration.VERTICAL);
        Drawable divider = ContextCompat.getDrawable(this, R.drawable.divider);
        if (divider != null) {
            horizontalDecoration.setDrawable(divider);
        }
        recyclerView.addItemDecoration(horizontalDecoration);


        ItemPriceAdapter adapter = new ItemPriceAdapter(dialogPrice.getContext(), itemPriceSelectedList, AddCountingItemActivity.this, true);
        recyclerView.setAdapter(adapter);

        dialogPrice.show();
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onItemClick(int position, View view) {

        if (dialog != null) {
//            Log.e("dialog check", " >>>> "+dialog.isShowing());
            if (dialog.isShowing()) {
                binding.editEANCode.setText(itemCodeList.get(position).itemCode);
                selectPriceData(position);
                dialog.dismiss();
            } else {
                if (dialogPrice.isShowing()) {
                    itemPriceId = itemPriceSelectedList.get(position).itemPriceId;
                    binding.editMRP.setText("" + itemPriceSelectedList.get(position).mrp);
                    binding.editSalePrice.setText("" + itemPriceSelectedList.get(position).salePrice);
                    itemData();
                    dialogPrice.dismiss();
                }
            }
        } else {
            if (dialogPrice != null)
                if (dialogPrice.isShowing()) {
                    itemPriceId = itemPriceSelectedList.get(position).itemPriceId;
                    binding.editMRP.setText("" + itemPriceSelectedList.get(position).mrp);
                    binding.editSalePrice.setText("" + itemPriceSelectedList.get(position).salePrice);
                    itemData();
                    dialogPrice.dismiss();
                }
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("refresh", true);
        setResult(112, intent);
        super.onBackPressed();

    }
}