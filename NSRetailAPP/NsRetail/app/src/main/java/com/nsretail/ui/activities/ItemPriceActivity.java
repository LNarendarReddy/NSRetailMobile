package com.nsretail.ui.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.nsretail.data.model.ItemModel.ItemPrice;
import com.nsretail.databinding.ActivityItemPriceBinding;
import com.nsretail.ui.Interface.OnItemClickListener;
import com.nsretail.ui.adapter.ItemPriceAdapter;

import java.util.ArrayList;

public class ItemPriceActivity extends AppCompatActivity implements OnItemClickListener {

    ActivityItemPriceBinding binding;
    ItemPriceAdapter adapter;
    ArrayList<ItemPrice> itemPricesList;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityItemPriceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.includePrice.textTitle.setText("Item Price");

        if (getIntent() != null) {
            itemPricesList = (ArrayList<ItemPrice>) getIntent().getSerializableExtra("itemPrice");

            adapter = new ItemPriceAdapter(getApplicationContext(), itemPricesList, ItemPriceActivity.this);
            binding.recyclerViewPrice.setAdapter(adapter);

        }


    }

    @Override
    public void onItemClick(int position, View view) {
        Intent intent = new Intent();
        intent.putExtra("position", position);
        setResult(111, intent);
        finish();
    }
}
