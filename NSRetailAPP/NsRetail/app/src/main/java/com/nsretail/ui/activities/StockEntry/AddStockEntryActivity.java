package com.nsretail.ui.activities.StockEntry;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
import com.nsretail.data.model.GSTModel.GST;
import com.nsretail.data.model.ItemModel.Item;
import com.nsretail.data.model.ItemModel.ItemCode;
import com.nsretail.data.model.ItemModel.ItemModel;
import com.nsretail.data.model.ItemModel.ItemPrice;
import com.nsretail.data.model.StockEntry.StockEntry;
import com.nsretail.databinding.ActivityAddStockBinding;
import com.nsretail.ui.Interface.OnItemClickListener;
import com.nsretail.ui.adapter.ItemCodeAdapter;
import com.nsretail.utils.NetworkStatus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddStockEntryActivity extends AppCompatActivity implements OnItemClickListener {

    ActivityAddStockBinding binding;

    List<GST> gstList;
    ArrayList<String> gstArray;
    ArrayList<Item> itemList;
    ArrayList<ItemCode> itemCodeList;
    ArrayList<ItemPrice> itemPriceList;
    ArrayList<ItemPrice> itemPriceSelectedList;
    ArrayList<StockEntry> stockEntry;
    int isIGST, gstId;
    double gstValue, finalPriceWO = -1, finalPrice = -1;
    double cGST, sGST, iGST, cess;
    ActivityResultLauncher<ScanOptions> barcodeLauncher;
    Dialog dialog;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddStockBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.includeStockAdd.textTitle.setText("Stock Entry Detail");
        binding.includeStockAdd.imageCamera.setVisibility(View.VISIBLE);
        binding.includeStockAdd.imageBack.setVisibility(View.VISIBLE);

        stockEntry = new ArrayList<>();

        if (getIntent() != null) {
            stockEntry = (ArrayList<StockEntry>) getIntent().getSerializableExtra("stockEntry");
            isIGST = getIntent().getIntExtra("isIGST", -1);
        }

        if (NetworkStatus.getInstance(AddStockEntryActivity.this).isConnected()) getGSTData();
        else
            Toast.makeText(AddStockEntryActivity.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();

        binding.editItemName.setEnabled(false);
        binding.editAppliedGST.setEnabled(false);
        binding.editAppliedDiscount.setEnabled(false);
        binding.editAppliedScheme.setEnabled(false);
        binding.editPriceTax.setEnabled(false);
        binding.editPriceWOTax.setEnabled(false);
        binding.editFinalPrice.setEnabled(false);
        binding.editCGST.setEnabled(false);
        binding.editSGST.setEnabled(false);
        binding.editIGST.setEnabled(false);
        binding.editCESS.setEnabled(false);

        binding.includeStockAdd.imageBack.setOnClickListener(view -> finish());

        binding.selectGST.setOnItemClickListener((adapterView, view, pos, l) -> {
            for (int i = 0; i < gstList.size(); i++) {
                if (gstList.get(i).gstCode.equalsIgnoreCase(binding.selectGST.getText().toString())) {
                    gstId = gstList.get(i).gstId;
                    gstValue = gstList.get(i).gstValue;
                    sGST = gstList.get(i).sGST;
                    cGST = gstList.get(i).cGST;
                    iGST = gstList.get(i).iGST;
                    cess = gstList.get(i).cess;
                }
            }
            if (!itemList.get(0).isOpenItem)
                if (binding.editQuantity.getText().length() > 0)
                    calculateTax(Integer.parseInt(binding.editQuantity.getText().toString()));
                else if (binding.editWeight.getText().length() > 0)
                    calculateTax(Integer.parseInt(binding.editWeight.getText().toString()));

        });

        binding.includeStockAdd.imageCamera.setOnClickListener(view -> {
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
                    Toast.makeText(AddStockEntryActivity.this, "Cancelled", Toast.LENGTH_LONG).show();
                } else if (originalIntent.hasExtra(Intents.Scan.MISSING_CAMERA_PERMISSION)) {
                    Log.d("MainActivity", "Cancelled scan due to missing camera permission");
                    Toast.makeText(AddStockEntryActivity.this, "Cancelled due to missing camera permission", Toast.LENGTH_LONG).show();
                }
            } else {
                Log.d("MainActivity", "Scanned");
                Toast.makeText(AddStockEntryActivity.this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                binding.editEANCode.setText(result.getContents());
                if (NetworkStatus.getInstance(AddStockEntryActivity.this).isConnected())
                    getItemData(result.getContents());
                else Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();

            }
        });

        binding.editEANCode.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (binding.editEANCode.getText().length() > 0) {
                    if (NetworkStatus.getInstance(AddStockEntryActivity.this).isConnected())
                        getItemData(binding.editEANCode.getText().toString());
                    else
                        Toast.makeText(AddStockEntryActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
                } else {
                    binding.editEANCode.setError("Enter Item code");
                }
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
                if (binding.editEANCode.getText().length() > 0) {
                    clearData();
                }
            }
        });

        binding.editQuantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (binding.editQuantity.hasFocus()) {
//                    if (binding.editQuantity.getText().length() > 0)
                    calculateTax(Integer.parseInt(binding.editQuantity.getText().toString()));
                }
            }
        });

        binding.editWeight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (binding.editWeight.hasFocus()) {
//                    if (binding.editWeight.getText().length() > 0)
                    calculateTax(Integer.parseInt(binding.editWeight.getText().toString()));
                }
            }
        });

        binding.editDiscount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (binding.editDiscount.hasFocus()) {
                    binding.editDiscountFlat.setText("");
                    if (binding.editQuantity.getText().length() > 0)
                        calculateTax(Integer.parseInt(binding.editQuantity.getText().toString()));
                    else if (binding.editWeight.getText().length() > 0)
                        calculateTax(Integer.parseInt(binding.editWeight.getText().toString()));
                }
            }
        });

        binding.editDiscountFlat.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (binding.editDiscountFlat.hasFocus()) {
                    binding.editDiscount.setText("");
                    if (binding.editQuantity.getText().length() > 0)
                        calculateTax(Integer.parseInt(binding.editQuantity.getText().toString()));
                    else if (binding.editWeight.getText().length() > 0)
                        calculateTax(Integer.parseInt(binding.editWeight.getText().toString()));
                }
            }
        });

        binding.editScheme.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (binding.editScheme.hasFocus()) {
                    binding.editSchemeFlat.setText("");
                    if (binding.editQuantity.getText().length() > 0)
                        calculateTax(Integer.parseInt(binding.editQuantity.getText().toString()));
                    else if (binding.editWeight.getText().length() > 0)
                        calculateTax(Integer.parseInt(binding.editWeight.getText().toString()));
                }
            }
        });

        binding.editSchemeFlat.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (binding.editSchemeFlat.hasFocus()) {
                    binding.editScheme.setText("");
                    if (binding.editQuantity.getText().length() > 0)
                        calculateTax(Integer.parseInt(binding.editQuantity.getText().toString()));
                    else if (binding.editWeight.getText().length() > 0)
                        calculateTax(Integer.parseInt(binding.editWeight.getText().toString()));
                }
            }
        });

        binding.editCPTax.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (binding.editCPTax.hasFocus())
//                    if (binding.editCPTax.getText().length() > 0) {
                    if (binding.editQuantity.getText().length() > 0)
                        calculateTax(Integer.parseInt(binding.editQuantity.getText().toString()));
                    else if (binding.editWeight.getText().length() > 0)
                        calculateTax(Integer.parseInt(binding.editWeight.getText().toString()));
//                    }
            }
        });

        binding.editCPTaxWO.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (binding.editCPTaxWO.hasFocus())
//                    if (binding.editCPTaxWO.getText().length() > 0) {
                    if (binding.editQuantity.getText().length() > 0)
                        calculateTax(Integer.parseInt(binding.editQuantity.getText().toString()));
                    else if (binding.editWeight.getText().length() > 0)
                        calculateTax(Integer.parseInt(binding.editWeight.getText().toString()));
//                    }
            }
        });


    }

    private void clearData() {
        binding.editItemName.setText("");
        binding.editQuantity.setText("");
        binding.editWeight.setText("");
        binding.editMRP.setText("");
        binding.editSalePrice.setText("");
        binding.editCPTax.setText("");
        binding.editCPTaxWO.setText("");
        binding.editDiscount.setText("");
        binding.editDiscountFlat.setText("");
        binding.editScheme.setText("");
        binding.editSchemeFlat.setText("");
        binding.selectGST.setText("");
        binding.editAppliedGST.setText("");
        binding.editCGST.setText("");
        binding.editSGST.setText("");
        binding.editIGST.setText("");
        binding.editCESS.setText("");
        binding.editAppliedDiscount.setText("");
        binding.editAppliedScheme.setText("");
        binding.editPriceWOTax.setText("");
        binding.editPriceTax.setText("");
        binding.editHSNCode.setText("");
        binding.editFinalPrice.setText("");

        gstValue = 0;
        finalPrice = -1;
        finalPriceWO = -1;
        cGST = 0;
        sGST = 0;
        iGST = 0;
        cess = 0;
    }

    @SuppressLint("SetTextI18n")
    ActivityResultLauncher<Intent> activityResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == 111) {
            Intent data = result.getData();
            if (data != null) {

                int pos = data.getIntExtra("position", -1);

                binding.editMRP.setText("" + itemPriceSelectedList.get(pos).mrp);
                binding.editSalePrice.setText("" + itemPriceSelectedList.get(pos).salePrice);
                binding.editCPTax.setText("" + itemPriceSelectedList.get(pos).costPriceWT);
                binding.editCPTaxWO.setText("" + itemPriceSelectedList.get(pos).costPriceWOT);

                for (int i = 0; i < gstList.size(); i++) {
                    if (gstList.get(i).gstId == itemPriceSelectedList.get(pos).gstId) {
                        binding.selectGST.setText("" + gstList.get(i).gstCode);
                        gstValue = gstList.get(i).gstValue;
                        sGST = gstList.get(i).sGST;
                        cGST = gstList.get(i).cGST;
                        iGST = gstList.get(i).iGST;
                        cess = gstList.get(i).cess;
                    }
                }

                itemData();

            }

        }
    });

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    private void calculateTax(int qty) {
        Log.e("calculate", ">>>> ");
        if (stockEntry.size() > 0) {
            if (stockEntry.get(0).taxInclusiveValue) {
                binding.editCPTax.setEnabled(true);
                binding.editCPTaxWO.setEnabled(false);

                if (binding.editCPTax.getText().length() > 0) {
                    double cpTax = Double.parseDouble(binding.editCPTax.getText().toString()) - (Double.parseDouble(binding.editCPTax.getText().toString()) * gstValue);
                    binding.editCPTaxWO.setText(String.format("%.4f", cpTax));
                }

            } else {
                binding.editCPTax.setEnabled(false);
                binding.editCPTaxWO.setEnabled(true);

                if (binding.editCPTaxWO.getText().length() > 0) {
                    double cpTax = Double.parseDouble(binding.editCPTaxWO.getText().toString()) + (Double.parseDouble(binding.editCPTaxWO.getText().toString()) * gstValue);
                    binding.editCPTax.setText(String.format("%.4f", cpTax));
                }
            }
        }

        if (binding.editCPTax.getText().length() > 0)
            binding.editPriceTax.setText(String.format("%.2f", (Double.parseDouble(binding.editCPTax.getText().toString()) * qty)));
        if (binding.editCPTaxWO.getText().length() > 0)
            binding.editPriceWOTax.setText(String.format("%.2f", (Double.parseDouble(binding.editCPTaxWO.getText().toString()) * qty)));

        if (binding.editDiscountFlat.getText().length() > 0) {
            binding.editAppliedDiscount.setText(binding.editDiscountFlat.getText());
        } else if (binding.editDiscount.getText().length() > 0) {

            double discountValue = Math.abs(Double.parseDouble(binding.editPriceWOTax.getText().toString()) *
                    (Double.parseDouble(binding.editDiscount.getText().toString()) / 100));

            binding.editAppliedDiscount.setText("" + discountValue);
        } else {
            binding.editAppliedDiscount.setText("0");
        }

        if (Objects.requireNonNull(binding.editSchemeFlat.getText()).length() > 0) {
            binding.editAppliedScheme.setText(binding.editSchemeFlat.getText());
        } else if (Objects.requireNonNull(binding.editScheme.getText()).length() > 0) {

            double schemeValue = Math.abs(Double.parseDouble(Objects.requireNonNull(binding.editPriceWOTax.getText()).toString()) * (Double.parseDouble(binding.editScheme.getText().toString()) / 100));

            binding.editAppliedScheme.setText("" + schemeValue);
        } else {
            binding.editAppliedScheme.setText("0");
        }

        if (binding.editPriceWOTax.getText().length() > 0)
            finalPriceWO = Double.parseDouble(binding.editPriceWOTax.getText().toString()) -
                    Double.parseDouble(binding.editAppliedDiscount.getText().toString()) -
                    Double.parseDouble(binding.editAppliedScheme.getText().toString());

        if (binding.editPriceTax.getText().length() > 0)
            finalPrice = Double.parseDouble(binding.editPriceTax.getText().toString()) -
                    Double.parseDouble(binding.editAppliedDiscount.getText().toString()) -
                    Double.parseDouble(binding.editAppliedScheme.getText().toString());

        if (stockEntry.get(0).taxInclusiveValue) {
            if (finalPrice != -1) {
//                double appliedGst = Math.round(finalPrice * gstValue / 100);
//                binding.editAppliedGST.setText(String.format("%.2f", appliedGst));

                if (iGST == 1) {
                    binding.editIGST.setText("" + String.format("%.2f", Math.abs(finalPrice * (iGST / 100))));
                    binding.editCGST.setText("0");
                    binding.editSGST.setText("0");
                } else {
                    binding.editIGST.setText("0");
                    binding.editCGST.setText("" + String.format("%.2f", Math.abs(finalPrice * (cGST / 100))));
                    binding.editSGST.setText("" + String.format("%.2f", Math.abs(finalPrice * (sGST / 100))));
                }
                binding.editCESS.setText("" + String.format("%.2f", Math.abs(finalPrice * (cess / 100))));

                double appliedGst = Double.parseDouble(binding.editCGST.getText().toString())
                        + Double.parseDouble(binding.editSGST.getText().toString())
                        + Double.parseDouble(binding.editCESS.getText().toString())
                        + Double.parseDouble(binding.editIGST.getText().toString());
                binding.editAppliedGST.setText("" + appliedGst);

                binding.editFinalPrice.setText(String.format("%.3f", finalPrice));

            }
        } else {
            if (finalPriceWO != -1) {
//                double appliedGst = Math.round(finalPriceWO * gstValue / 100);
//                binding.editAppliedGST.setText(String.format("%.2f", appliedGst));

                if (iGST == 1) {
                    binding.editIGST.setText("" + String.format("%.2f", Math.abs(finalPriceWO * (iGST / 100))));
                    binding.editCGST.setText("0");
                    binding.editSGST.setText("0");
                } else {
                    binding.editIGST.setText("0");
                    binding.editCGST.setText("" + String.format("%.2f", Math.abs(finalPriceWO * (cGST / 100))));
                    binding.editSGST.setText("" + String.format("%.2f", Math.abs(finalPriceWO * (sGST / 100))));
                }

                binding.editCESS.setText("" + String.format("%.2f", Math.abs(finalPriceWO * (cess / 100))));

                double appliedGst = Double.parseDouble(binding.editCGST.getText().toString())
                        + Double.parseDouble(binding.editSGST.getText().toString())
                        + Double.parseDouble(binding.editCESS.getText().toString())
                        + Double.parseDouble(binding.editIGST.getText().toString());
                binding.editAppliedGST.setText("" + appliedGst);

                binding.editFinalPrice.setText(String.format("%.3f", (finalPriceWO + appliedGst)));


            }
        }


    }

    private void getItemData(String itemCode) {
        binding.progressBar.setVisibility(View.VISIBLE);
        itemList = new ArrayList<>();
        itemCodeList = new ArrayList<>();
        itemPriceList = new ArrayList<>();
        itemPriceSelectedList = new ArrayList<>();

        StatusAPI itemAPI = BaseURL.getStatusAPI();
        Call<ItemModel> call = itemAPI.getEntryItemData(true, itemCode);

        call.enqueue(new Callback<ItemModel>() {
            @Override
            public void onResponse(Call<ItemModel> call, Response<ItemModel> response) {
                binding.progressBar.setVisibility(View.GONE);
                try {
                    if (response.code() == 200) {

                        itemList = response.body().itemList;
                        itemCodeList = response.body().itemCodeList;
                        itemPriceList = response.body().itemPriceList;

                        if (itemCodeList.size() > 1) {
                            showDialog();
                        } else {
                            Log.v("server response >>", " >>>> ");
                            binding.editEANCode.setText(itemCodeList.get(0).itemCode);
                            binding.editHSNCode.setText(itemCodeList.get(0).hsnCode);
                            selectPriceData(0);
                        }
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(AddStockEntryActivity.this);
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
            binding.editWeight.setText("1");
        } else {
            binding.editWeight.setEnabled(false);
            binding.editQuantity.setEnabled(true);
            binding.editQuantity.setText("1");
        }

        calculateTax(1);

    }

    private void showDialog() {

        dialog = new Dialog(AddStockEntryActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_dialog);

        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        RecyclerView recyclerView = dialog.findViewById(R.id.recyclerViewItem);

        DividerItemDecoration horizontalDecoration = new DividerItemDecoration(recyclerView.getContext(),
                DividerItemDecoration.VERTICAL);
        Drawable divider = ContextCompat.getDrawable(this, R.drawable.divider);
        if (divider != null) {
            horizontalDecoration.setDrawable(divider);
        }
        recyclerView.addItemDecoration(horizontalDecoration);

        ItemCodeAdapter adapter = new ItemCodeAdapter(dialog.getContext(), itemCodeList, AddStockEntryActivity.this);
        recyclerView.setAdapter(adapter);

        dialog.show();
    }

    private void getGSTData() {
        StatusAPI planAPI = BaseURL.getStatusAPI();
        Call<List<GST>> call = planAPI.gstData(true);

        call.enqueue(new Callback<List<GST>>() {
            @Override
            public void onResponse(Call<List<GST>> call, Response<List<GST>> response) {

                if (response.code() == 200) {
                    gstList = new ArrayList<>();
                    gstArray = new ArrayList<>();
                    gstList = response.body();
                    for (int i = 0; i < gstList.size(); i++) {
                        gstArray.add(gstList.get(i).gstCode);
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.item_spinner, gstArray);
                    binding.selectGST.setAdapter(adapter);
                }

            }

            @Override
            public void onFailure(Call<List<GST>> call, Throwable t) {
                Log.e("GST", "" + t.getMessage());

            }
        });

    }

    @Override
    public void onItemClick(int position, View view) {

        binding.editEANCode.setText(itemCodeList.get(position).itemCode);
        if (itemCodeList.get(position).hsnCode != null)
            binding.editHSNCode.setText(itemCodeList.get(position).hsnCode);
        Log.v("onclick dialog >>", " >>>> ");

        selectPriceData(position);

        dialog.dismiss();

    }

    @SuppressLint("SetTextI18n")
    private void selectPriceData(int pos) {

        for (int i = 0; i < itemPriceList.size(); i++) {
            if (itemCodeList.get(pos).itemCodeId == itemPriceList.get(i).itemCodeId) {
                itemPriceSelectedList.add(itemPriceList.get(i));
            }
        }

        if (itemPriceSelectedList.size() > 1) {
            Intent h = new Intent(AddStockEntryActivity.this, ItemPriceActivity.class);
            h.putExtra("itemPrice", itemPriceList);
            activityResult.launch(h);
        } else {

            binding.editMRP.setText("" + itemPriceSelectedList.get(0).mrp);
            binding.editSalePrice.setText("" + itemPriceSelectedList.get(0).salePrice);
            binding.editCPTax.setText("" + itemPriceSelectedList.get(0).costPriceWT);
            binding.editCPTaxWO.setText("" + itemPriceSelectedList.get(0).costPriceWOT);

            for (int i = 0; i < gstList.size(); i++) {
                if (gstList.get(i).gstId == itemPriceSelectedList.get(0).gstId) {
                    binding.selectGST.setText(gstList.get(i).gstCode);
                    gstValue = gstList.get(i).gstValue;
                    sGST = gstList.get(i).sGST;
                    cGST = gstList.get(i).cGST;
                    iGST = gstList.get(i).iGST;
                    cess = gstList.get(i).cess;
                }
            }
            Log.v("onclick dialog cGST>>", " >>>> " + cGST);

            itemData();

        }

    }
}