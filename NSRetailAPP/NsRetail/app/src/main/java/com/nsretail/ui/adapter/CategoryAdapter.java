package com.nsretail.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nsretail.data.model.CategoryModel.Category;
import com.nsretail.databinding.ItemBranchBinding;
import com.nsretail.ui.Interface.OnItemClickListener;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private final List<Category> branchList;
    private final Context mContext;
    private OnItemClickListener listener;

    public CategoryAdapter(Context context, List<Category> branchArrayList, OnItemClickListener listener) {
        this.mContext = context;
        this.branchList = branchArrayList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemBranchBinding itemBinding = ItemBranchBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(itemBinding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.itemStockBinding.textBranch.setText(branchList.get(position).categoryName);

        holder.itemView.setOnClickListener(view -> {
            listener.onItemClick(position, view);
        });


    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemBranchBinding itemStockBinding;

        public ViewHolder(ItemBranchBinding itemBinding) {
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
        return branchList.size();
    }
}