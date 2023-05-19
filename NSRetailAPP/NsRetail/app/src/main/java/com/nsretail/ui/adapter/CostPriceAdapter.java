package com.nsretail.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nsretail.data.model.BranchModel.Branch;
import com.nsretail.databinding.ItemCostPriceBinding;

import java.util.List;

public class CostPriceAdapter extends RecyclerView.Adapter<CostPriceAdapter.ViewHolder> {

    private final List<Branch> branchList;
    private final Context mContext;

    public CostPriceAdapter(Context context, List<Branch> branchArrayList) {
        this.mContext = context;
        this.branchList = branchArrayList;
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

//        holder.itemPriceBinding.textMrp.setText("Mrp");


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
        return 7;
    }
}