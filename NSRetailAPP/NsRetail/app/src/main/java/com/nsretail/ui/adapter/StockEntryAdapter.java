package com.nsretail.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nsretail.data.model.StockEntry.StockEntryDetail;
import com.nsretail.databinding.ItemStockEntryBinding;

import java.util.ArrayList;

public class StockEntryAdapter extends RecyclerView.Adapter<StockEntryAdapter.ViewHolder> {

    private final ArrayList<StockEntryDetail> stockEntryList;
    private final Context mContext;

    public StockEntryAdapter(Context context, ArrayList<StockEntryDetail> stockList) {
        this.mContext = context;
        this.stockEntryList = stockList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemStockEntryBinding itemBinding = ItemStockEntryBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(itemBinding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.itemStockBinding.textStock.setText(stockEntryList.get(position).itemCode
                + " " + stockEntryList.get(position).itemName);

        if (stockEntryList.get(position).isOpenItem) {
            holder.itemStockBinding.textPrice.setText("MRP: " + stockEntryList.get(position).mrp
                    + ", CP: " + stockEntryList.get(position).costPriceWt
                    + ", W/Kgs: " + stockEntryList.get(position).weightInKgs);
        } else {
            holder.itemStockBinding.textPrice.setText("MRP: " + stockEntryList.get(position).mrp
                    + ", CP: " + stockEntryList.get(position).costPriceWt
                    + ", Qty: " + stockEntryList.get(position).quantity);
        }

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemStockEntryBinding itemStockBinding;

        public ViewHolder(ItemStockEntryBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemStockBinding = itemBinding;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return stockEntryList.size();
    }
}