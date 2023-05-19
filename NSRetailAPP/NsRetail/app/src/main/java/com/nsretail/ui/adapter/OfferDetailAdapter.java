package com.nsretail.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nsretail.data.model.BranchModel.Branch;
import com.nsretail.databinding.ItemOfferBinding;

import java.util.List;

public class OfferDetailAdapter extends RecyclerView.Adapter<OfferDetailAdapter.ViewHolder> {

    private final List<Branch> branchList;
    private final Context mContext;

    public OfferDetailAdapter(Context context, List<Branch> branchArrayList) {
        this.mContext = context;
        this.branchList = branchArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemOfferBinding itemBinding = ItemOfferBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(itemBinding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

//        holder.itemStockBinding.textBranch.setText(branchList.get(position).branchName);


    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemOfferBinding itemOfferBinding;

        public ViewHolder(ItemOfferBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemOfferBinding = itemBinding;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}