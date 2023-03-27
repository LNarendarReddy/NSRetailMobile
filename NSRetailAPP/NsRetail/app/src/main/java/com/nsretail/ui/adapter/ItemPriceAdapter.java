package com.nsretail.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nsretail.data.model.ItemModel.ItemPrice;
import com.nsretail.databinding.ItemPriceBinding;
import com.nsretail.ui.Interface.OnItemClickListener;

import java.util.ArrayList;

public class ItemPriceAdapter extends RecyclerView.Adapter<ItemPriceAdapter.ViewHolder> {

    private final ArrayList<ItemPrice> itemPriceArrayList;
    private final Context mContext;
    OnItemClickListener listener;

    boolean fromDialog;

    public ItemPriceAdapter(Context context, ArrayList<ItemPrice> itemPrices, OnItemClickListener listener, boolean dialogFrom) {
        this.mContext = context;
        this.itemPriceArrayList = itemPrices;
        this.listener = listener;
        this.fromDialog = dialogFrom;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPriceBinding itemBinding = ItemPriceBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(itemBinding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        holder.itemPriceBinding.textPrice.setText("MRP: " + itemPriceArrayList.get(position).mrp
                + ", SalePrice: " + itemPriceArrayList.get(position).salePrice);

        if (!fromDialog) {
            holder.itemPriceBinding.textTax.setVisibility(View.VISIBLE);
            holder.itemPriceBinding.textTax.setText("CPWT: " + itemPriceArrayList.get(position).costPriceWT
                    + ", CPWOT: " + itemPriceArrayList.get(position).costPriceWOT);
        } else {
            holder.itemPriceBinding.textTax.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(view -> {
            listener.onItemClick(position, view);
        });

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemPriceBinding itemPriceBinding;

        public ViewHolder(ItemPriceBinding itemBinding) {
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
        return itemPriceArrayList.size();
    }
}