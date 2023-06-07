package com.nsretail.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nsretail.data.model.ItemDetail.BranchStock;
import com.nsretail.databinding.ItemStockDetailBinding;

import java.util.ArrayList;

public class StockDetailsAdapter extends RecyclerView.Adapter<StockDetailsAdapter.ViewHolder> {

    private final ArrayList<BranchStock> stockList;
    private final Context mContext;

    public StockDetailsAdapter(Context context, ArrayList<BranchStock> stockArrayList) {
        this.mContext = context;
        this.stockList = stockArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemStockDetailBinding itemBinding = ItemStockDetailBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(itemBinding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.itemStockBinding.textQuantity.setText("" + stockList.get(position).quantity);
        holder.itemStockBinding.textWeightInKgs.setText("" + stockList.get(position).weightInKgs);

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemStockDetailBinding itemStockBinding;

        public ViewHolder(ItemStockDetailBinding itemBinding) {
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
        return stockList.size();
    }
}