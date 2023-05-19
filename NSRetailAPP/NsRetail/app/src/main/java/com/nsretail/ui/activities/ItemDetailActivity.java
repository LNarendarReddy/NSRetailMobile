package com.nsretail.ui.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.client.android.Intents;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;
import com.nsretail.data.model.BranchModel.Branch;
import com.nsretail.databinding.ActivityItemDetailBinding;
import com.nsretail.ui.adapter.CostPriceAdapter;
import com.nsretail.ui.adapter.OfferDetailAdapter;
import com.nsretail.ui.adapter.PriceDetailAdapter;
import com.nsretail.ui.adapter.StockDetailsAdapter;

import java.util.ArrayList;
import java.util.List;

public class ItemDetailActivity extends AppCompatActivity {

    ActivityItemDetailBinding binding;
    ActivityResultLauncher<ScanOptions> barcodeLauncher;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityItemDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.includeItem.textTitle.setText("Item Details");
        binding.includeItem.imageBack.setVisibility(View.VISIBLE);
        binding.includeItem.imageCamera.setVisibility(View.VISIBLE);


        binding.includeItem.imageBack.setOnClickListener(view -> {
            finish();
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
//                if (NetworkStatus.getInstance(ItemDetailActivity.this).isConnected())
//                    getItemData(result.getContents());
//                else Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();

            }
        });


        /*binding.editEANCode.setOnEditorActionListener((textView, actionId, keyEvent) -> {
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
        });*/

        /*binding.editEANCode.addTextChangedListener(new TextWatcher() {
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
        });*/

        List<Branch> branchList = new ArrayList<>();

        PriceDetailAdapter adapter = new PriceDetailAdapter(ItemDetailActivity.this, branchList);
        CostPriceAdapter adapter1 = new CostPriceAdapter(ItemDetailActivity.this, branchList);
        OfferDetailAdapter adapter2 = new OfferDetailAdapter(ItemDetailActivity.this, branchList);
        StockDetailsAdapter adapter3 = new StockDetailsAdapter(ItemDetailActivity.this, branchList);

        binding.recyclerViewPrice.setAdapter(adapter);
        binding.recyclerCostPrice.setAdapter(adapter1);
        binding.recyclerViewOffer.setAdapter(adapter2);
        binding.recyclerViewStock.setAdapter(adapter3);

    }

    private void clearData() {

        binding.editEANCode.setText("");
        binding.editEANCode.requestFocus();
        binding.editSKUCode.setText("");
        binding.editItemName.setText("");

    }
}