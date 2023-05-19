package com.nsretail.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nsretail.data.model.BranchModel.Branch;
import com.nsretail.databinding.ItemPriceDetailBinding;

import java.util.List;

public class PriceDetailAdapter extends RecyclerView.Adapter<PriceDetailAdapter.ViewHolder> {

    private final List<Branch> branchList;
    private final Context mContext;

    public PriceDetailAdapter(Context context, List<Branch> branchArrayList) {
        this.mContext = context;
        this.branchList = branchArrayList;
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

//        holder.itemPriceBinding.textMrp.setText("Mrp");


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
        return 3;
    }
}