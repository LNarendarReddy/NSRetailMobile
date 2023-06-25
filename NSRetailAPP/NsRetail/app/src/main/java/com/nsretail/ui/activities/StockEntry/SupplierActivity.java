package com.nsretail.ui.activities.StockEntry;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonObject;
import com.nsretail.Globals;
import com.nsretail.R;
import com.nsretail.data.api.BaseURL;
import com.nsretail.data.api.StatusAPI;
import com.nsretail.data.model.StockEntry.Invoice;
import com.nsretail.data.model.StockEntry.StockEntry;
import com.nsretail.data.model.SupplierModel.Supplier;
import com.nsretail.databinding.ActivitySupplierBinding;
import com.nsretail.utils.NetworkStatus;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SupplierActivity extends AppCompatActivity {

    ActivitySupplierBinding binding;

    List<Supplier> supplierList;
    ArrayList<String> supplierArray;
    ArrayList<StockEntry> stockEntry;
    int supplierId, isIGST;
    SimpleDateFormat serverDate, userDate;
    DatePickerDialog datePickerDialog;
    String dateValue;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySupplierBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.includeSupplier.textTitle.setText("Stock Entry");

        serverDate = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        userDate = new SimpleDateFormat("dd MMM yyyy", Locale.US);


        binding.editDate.setInputType(InputType.TYPE_NULL);
        binding.editDate.setKeyListener(null);
        binding.editDate.setEnabled(true);
        binding.editDate.setTextIsSelectable(true);
        binding.editDate.setFocusable(false);
        binding.editDate.setFocusableInTouchMode(false);

        setDateTimeField();


        binding.includeSupplier.imageBack.setOnClickListener(view -> finish());

        binding.buttonNext.setOnClickListener(view -> {

            if (stockEntry.size() > 0) {
                Intent h = new Intent(SupplierActivity.this, StockEntryActivity.class);
                h.putExtra("stockEntryId", stockEntry.get(0).stockEntryId);
                h.putExtra("isIGST", isIGST);
                startActivity(h);
            } else {
                if (binding.selectSupplier.getText().length() > 0) {
                    if (binding.editInvoice.getText().length() > 0) {
                        if (binding.editDate.getText().length() > 0) {
                            if (NetworkStatus.getInstance(SupplierActivity.this).isConnected())
                                supplierPost();
                            else
                                Toast.makeText(SupplierActivity.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
                        } else binding.editDate.setError("Enter invoice date");
                    } else binding.editInvoice.setError("Enter the invoice number");
                } else
                    Toast.makeText(SupplierActivity.this, "Please select Supplier", Toast.LENGTH_SHORT).show();
            }

        });
    }

    @Override
    protected void onResume() {
        if (NetworkStatus.getInstance(SupplierActivity.this).isConnected()) {
            getSupplierData();
        } else
            Toast.makeText(SupplierActivity.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
        super.onResume();
    }

    private void getStockData() {
        stockEntry = new ArrayList<>();
        StatusAPI stockAPI = BaseURL.getStatusAPI();

        Call<Invoice> call = stockAPI.getInvoiceData(Globals.userResponse.user.get(0).categoryId, Globals.userResponse.user.get(0).userId, true);

        call.enqueue(new Callback<Invoice>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<Invoice> call, Response<Invoice> response) {

                if (response.code() == 200) {
                    stockEntry = response.body().stockEntry;

                    binding.selectSupplier.setText(stockEntry.get(0).dealerName);
                    binding.editGst.setText(stockEntry.get(0).gstIn);
                    binding.editInvoice.setText(stockEntry.get(0).supplierInvoiceNo);
                    binding.editDate.setText(stockEntry.get(0).invoiceDate);
                    binding.checkTax.setChecked(stockEntry.get(0).taxInclusiveValue);

                    binding.selectSupplier.setEnabled(false);
                    binding.editInvoice.setEnabled(false);
                    binding.editDate.setEnabled(false);
                    binding.checkTax.setEnabled(false);

                }


            }

            @Override
            public void onFailure(Call<Invoice> call, Throwable t) {

            }
        });


    }


    private void setDateTimeField() {

        binding.editDate.setOnClickListener(view -> datePickerDialog.show());

        Calendar newCalendar = Calendar.getInstance();
        newCalendar.add(Calendar.YEAR, 0);

        datePickerDialog = new DatePickerDialog(this, (view, year, monthOfYear, dayOfMonth) -> {
            Calendar newDate = Calendar.getInstance();
            newDate.set(year, monthOfYear, dayOfMonth);
            binding.editDate.setText(userDate.format(newDate.getTime()));
            dateValue = serverDate.format(newDate.getTime());
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMaxDate(newCalendar.getTimeInMillis());

    }

    private void getSupplierData() {
        binding.progressBar.setVisibility(View.VISIBLE);
        StatusAPI planAPI = BaseURL.getStatusAPI();
        Call<List<Supplier>> call = planAPI.supplierData(true);

        call.enqueue(new Callback<List<Supplier>>() {
            @Override
            public void onResponse(Call<List<Supplier>> call, Response<List<Supplier>> response) {
                binding.progressBar.setVisibility(View.GONE);

                if (response.code() == 200) {
                    supplierList = new ArrayList<>();
                    supplierArray = new ArrayList<>();
                    supplierList = response.body();
                    for (int i = 0; i < supplierList.size(); i++) {
                        supplierArray.add(supplierList.get(i).dealerName);
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.item_spinner, supplierArray);
                    binding.selectSupplier.setAdapter(adapter);

                    binding.selectSupplier.setOnItemClickListener((adapterView, view, pos, l) -> {
                        for (int i = 0; i < supplierList.size(); i++) {
                            if (supplierList.get(i).dealerName.equalsIgnoreCase(binding.selectSupplier.getText().toString())) {
                                supplierId = supplierList.get(i).dealerId;
                                isIGST = supplierList.get(i).isIGST;
                                binding.editGst.setText(supplierList.get(i).gstin);
                            }
                        }
                    });


                }
                if (NetworkStatus.getInstance(SupplierActivity.this).isConnected()) {
                    getStockData();
                } else
                    Toast.makeText(SupplierActivity.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<List<Supplier>> call, Throwable t) {

                binding.progressBar.setVisibility(View.GONE);

                AlertDialog.Builder builder = new AlertDialog.Builder(SupplierActivity.this);
                if (t.getMessage().equalsIgnoreCase("Failed to connect to nsoftsol.com/122.175.62.71:6002")) {
                    builder.setMessage("Network Issue!!").setCancelable(false).setPositiveButton("OK", (dialog, id) -> {
                        dialog.cancel();
                        finish();
                    });
                } else {
                    builder.setMessage(t.getMessage()).setCancelable(false).setPositiveButton("OK", (dialog, id) -> {
                        dialog.cancel();
                    });
                }
                AlertDialog alert = builder.create();
                alert.show();

                if (NetworkStatus.getInstance(SupplierActivity.this).isConnected()) {
                    getStockData();
                } else
                    Toast.makeText(SupplierActivity.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();


            }
        });

    }

    private void supplierPost() {
        binding.progressBar.setVisibility(View.VISIBLE);

        StatusAPI supplierAPI = BaseURL.getStatusAPI();

        JsonObject jsonObject = null;
        try {
            jsonObject = new JsonObject();
            jsonObject.addProperty("STOCKENTRYID", 0);
            jsonObject.addProperty("SUPPLIERID", supplierId);
            jsonObject.addProperty("SUPPLIERINVOICENO", binding.editInvoice.getText().toString());
            jsonObject.addProperty("INVOICEDATE", binding.editDate.getText().toString());
            jsonObject.addProperty("TAXINCLUSIVE", binding.checkTax.isChecked());
            jsonObject.addProperty("TCS", 0);
            jsonObject.addProperty("DISCOUNTPER", 0);
            jsonObject.addProperty("DISCOUNT", 0);
            jsonObject.addProperty("EXPENSES", 0);
            jsonObject.addProperty("TRANSPORT", 0);
            jsonObject.addProperty("CATEGORYID", Globals.userResponse.user.get(0).categoryId);
            jsonObject.addProperty("USERID", Globals.userResponse.user.get(0).userId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Call<ResponseBody> call = supplierAPI.saveInvoiceData(true, jsonObject);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                binding.progressBar.setVisibility(View.GONE);
                if (response.code() == 200) {
                    Intent h = new Intent(SupplierActivity.this, StockEntryActivity.class);
                    h.putExtra("stockEntryId", response.message());
                    startActivity(h);
                } else {
                    Toast.makeText(SupplierActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                AlertDialog.Builder builder = new AlertDialog.Builder(SupplierActivity.this);
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