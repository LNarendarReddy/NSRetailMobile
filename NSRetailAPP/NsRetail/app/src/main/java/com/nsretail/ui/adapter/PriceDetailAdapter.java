package com.nsretail.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nsretail.data.model.ItemModel.ItemPrice;
import com.nsretail.databinding.ItemPriceDetailBinding;

import java.util.ArrayList;

public class PriceDetailAdapter extends RecyclerView.Adapter<PriceDetailAdapter.ViewHolder> {

    private final ArrayList<ItemPrice> itemPriceList;
    private final Context mContext;

    public PriceDetailAdapter(Context context, ArrayList<ItemPrice> itemPrices) {
        this.mContext = context;
        this.itemPriceList = itemPrices;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPriceDetailBinding itemBinding = ItemPriceDetailBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(itemBinding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.itemPriceBinding.textMrp.setText("MRP: "+itemPriceList.get(position).mrp);
        holder.itemPriceBinding.textSalePrice.setText("SP: "+itemPriceList.get(position).salePrice);
        holder.itemPriceBinding.textStatus.setText("Status: "+itemPriceList.get(position).status);

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemPriceDetailBinding itemPriceBinding;

        public ViewHolder(ItemPriceDetailBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemPriceBinding = itemBinding;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return itemPriceList.size();
    }
}