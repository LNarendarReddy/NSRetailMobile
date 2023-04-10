package com.nsretail.ui.activities.StockEntry;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonObject;
import com.nsretail.Globals;
import com.nsretail.R;
import com.nsretail.data.api.BaseURL;
import com.nsretail.data.api.StatusAPI;
import com.nsretail.data.model.StockEntry.StockEntry;
import com.nsretail.data.model.StockEntry.StockEntryDetail;
import com.nsretail.databinding.ActivityStockPreviewBinding;
import com.nsretail.ui.activities.MainActivity;
import com.nsretail.utils.NetworkStatus;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StockPreviewActivity extends AppCompatActivity {

    ActivityStockPreviewBinding binding;

    StockEntry stockEntry;
    ArrayList<StockEntryDetail> stockEntryDetail;

    double totalPriceWOT, totalPriceWT, gstValue, netPrice, finalPrice;
    double expenses, transport, tcs;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStockPreviewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.includePreview.textTitle.setText("Stock Entry Preview");
        binding.includePreview.imageBack.setVisibility(View.VISIBLE);

        if (getIntent() != null) {
            stockEntry = (StockEntry) getIntent().getSerializableExtra("Suppiler");
            stockEntryDetail = (ArrayList<StockEntryDetail>) getIntent().getSerializableExtra("StockEntryDetail");

            binding.editSupplier.setText(stockEntry.dealerName);
            binding.editInvoice.setText(stockEntry.supplierInvoiceNo);
            binding.editInvoiceDate.setText(stockEntry.invoiceDate);
            binding.checkTax.setChecked(stockEntry.taxInclusiveValue);
        }

        if (stockEntryDetail.size() > 0) {
            for (int i = 0; i < stockEntryDetail.size(); i++) {

                totalPriceWOT = totalPriceWOT + stockEntryDetail.get(i).totalPriceWOT;
                totalPriceWT = totalPriceWT + stockEntryDetail.get(i).totalPriceWT;
                gstValue = gstValue + stockEntryDetail.get(i).appliedDGst;
                netPrice = netPrice + stockEntryDetail.get(i).finalPrice;

            }

            binding.editPriceWOTax.setText("" + totalPriceWOT);
            binding.editPriceTax.setText("" + totalPriceWT);
            binding.editGstValue.setText("" + gstValue);
            binding.editNetPrice.setText("" + netPrice);

        }

        binding.editDiscount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @SuppressLint("DefaultLocale")
            @Override
            public void afterTextChanged(Editable editable) {
                if (binding.editDiscount.hasFocus()) {
                    binding.editDiscountFlat.setText("");
                    calculateFinalPrice();
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

            @SuppressLint("DefaultLocale")
            @Override
            public void afterTextChanged(Editable editable) {

                if (binding.editDiscountFlat.hasFocus()) {
                    binding.editDiscount.setText("");
                    calculateFinalPrice();
                }

            }
        });

        binding.editExpenses.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (binding.editExpenses.hasFocus()) {
                    calculateFinalPrice();
                }

            }
        });

        binding.editTCS.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (binding.editTCS.hasFocus()) {
                    calculateFinalPrice();
                }

            }
        });

        binding.editTransport.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (binding.editTransport.hasFocus()) {
                    calculateFinalPrice();
                }

            }
        });

        binding.includePreview.imageBack.setOnClickListener(view -> finish());

        binding.buttonSave.setOnClickListener(view -> {
            if (binding.editExpenses.getText().length() > 0) {
                if (binding.editTransport.getText().length() > 0) {
                    if (binding.editTCS.getText().length() > 0) {
                        if (NetworkStatus.getInstance(StockPreviewActivity.this).isConnected())
                            saveEntryData();
                        else
                            Toast.makeText(this, "Please check the internet connection", Toast.LENGTH_SHORT).show();
                    } else {
                        binding.editTCS.setError("Enter TCS");
                    }
                } else {
                    binding.editTransport.setError("Enter Transport");
                }
            } else {
                binding.editExpenses.setError("Enter expenses");
            }
        });

    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    private void calculateFinalPrice() {

        if (binding.editExpenses.getText().length() > 0)
            expenses = Double.parseDouble(binding.editExpenses.getText().toString());
        else
            expenses = 0;

        if (binding.editTransport.getText().length() > 0)
            transport = Double.parseDouble(binding.editTransport.getText().toString());
        else
            transport = 0;

        if (binding.editTCS.getText().length() > 0)
            tcs = Double.parseDouble(binding.editTCS.getText().toString());
        else
            tcs = 0;

        finalPrice = netPrice + expenses + transport + tcs;
        binding.editFinalPrice.setText(String.format("%.2f", finalPrice));

        if (binding.editDiscount.getText().length() > 0) {
            double discount = (netPrice * (Double.parseDouble(binding.editDiscount.getText().toString()) / 100));
            finalPrice = (netPrice - discount) + expenses + tcs + transport;
            binding.editFinalPrice.setText(String.format("%.2f", finalPrice));
        }

        if (binding.editDiscountFlat.getText().length() > 0) {
            finalPrice = (netPrice - Double.parseDouble(binding.editDiscountFlat.getText().toString()))
                    + expenses + tcs + transport;

            binding.editFinalPrice.setText(String.format("%.2f", finalPrice));
        }

    }

    private void saveEntryData() {
        binding.progressBar.setVisibility(View.VISIBLE);

        StatusAPI supplierAPI = BaseURL.getStatusAPI();

        JsonObject jsonObject = null;
        try {
            jsonObject = new JsonObject();
            jsonObject.addProperty("STOCKENTRYID", stockEntry.stockEntryId);
            jsonObject.addProperty("SUPPLIERID", stockEntry.supplierId);
            jsonObject.addProperty("SUPPLIERINVOICENO", binding.editInvoice.getText().toString());
            jsonObject.addProperty("INVOICEDATE", binding.editInvoiceDate.getText().toString());
            jsonObject.addProperty("TAXINCLUSIVE", binding.checkTax.isChecked());
            jsonObject.addProperty("TCS", binding.editTCS.getText().toString());
            if (binding.editDiscount.getText().length() > 0)
                jsonObject.addProperty("DISCOUNTPER", binding.editDiscount.getText().toString());
            else
                jsonObject.addProperty("DISCOUNTPER", 0);
            if (binding.editDiscountFlat.getText().length() > 0)
                jsonObject.addProperty("DISCOUNT", binding.editDiscountFlat.getText().toString());
            else
                jsonObject.addProperty("DISCOUNT", 0);
            jsonObject.addProperty("EXPENSES", binding.editExpenses.getText().toString());
            jsonObject.addProperty("TRANSPORT", binding.editTransport.getText().toString());
            jsonObject.addProperty("CATEGORYID", Globals.userResponse.user.get(0).categoryId);
            jsonObject.addProperty("USERID", Globals.userResponse.user.get(0).userId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Call<ResponseBody> call = supplierAPI.updateInvoice(jsonObject, true);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                binding.progressBar.setVisibility(View.GONE);
                try {
                    if (response.code() == 200) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(StockPreviewActivity.this, R.style.AlertDialogCustom);

                        builder.setMessage(response.body().string())
                                .setCancelable(false)
                                .setPositiveButton("OK", (dialog, id) -> {
                                    Intent i = new Intent(StockPreviewActivity.this, MainActivity.class);
                                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(i);
                                    dialog.cancel();
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(StockPreviewActivity.this);
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
                Toast.makeText(StockPreviewActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });


    }

}