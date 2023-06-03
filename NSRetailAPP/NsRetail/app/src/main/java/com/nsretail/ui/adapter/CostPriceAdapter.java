package com.nsretail.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nsretail.data.model.ItemDetail.ItemCost;
import com.nsretail.databinding.ItemCostPriceBinding;

import java.util.ArrayList;

public class CostPriceAdapter extends RecyclerView.Adapter<CostPriceAdapter.ViewHolder> {

    private final ArrayList<ItemCost> itemCostList;
    private final Context mContext;

    public CostPriceAdapter(Context context, ArrayList<ItemCost> itemCosts) {
        this.mContext = context;
        this.itemCostList = itemCosts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCostPriceBinding itemBinding = ItemCostPriceBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(itemBinding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.itemCostBinding.textPriceWT.setText("" + itemCostList.get(position).costPriceWT);
        holder.itemCostBinding.textPriceWOT.setText("" + itemCostList.get(position).costPriceWOT);
        holder.itemCostBinding.textGst.setText("" + itemCostList.get(position).gstCode);

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemCostPriceBinding itemCostBinding;

        public ViewHolder(ItemCostPriceBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemCostBinding = itemBinding;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return itemCostList.size();
    }
}