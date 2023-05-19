package com.nsretail.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nsretail.data.model.BranchModel.Branch;
import com.nsretail.databinding.ItemStockDetailBinding;

import java.util.List;

public class StockDetailsAdapter extends RecyclerView.Adapter<StockDetailsAdapter.ViewHolder> {

    private final List<Branch> branchList;
    private final Context mContext;

    public StockDetailsAdapter(Context context, List<Branch> branchArrayList) {
        this.mContext = context;
        this.branchList = branchArrayList;
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

//        holder.itemStockBinding.textBranch.setText(branchList.get(position).branchName);

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
        return 10;
    }
}