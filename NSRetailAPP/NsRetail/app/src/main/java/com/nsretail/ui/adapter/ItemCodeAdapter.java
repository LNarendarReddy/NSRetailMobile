package com.nsretail.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nsretail.data.model.ItemModel.ItemCode;
import com.nsretail.databinding.ItemCustomDialogBinding;
import com.nsretail.ui.Interface.OnItemClickListener;

import java.util.ArrayList;

public class ItemCodeAdapter extends RecyclerView.Adapter<ItemCodeAdapter.ViewHolder> {

    private final ArrayList<ItemCode> itemCodeList;
    private final Context mContext;
    private OnItemClickListener listener;

    public ItemCodeAdapter(Context context, ArrayList<ItemCode> itemCode, OnItemClickListener listener) {
        this.mContext = context;
        this.itemCodeList = itemCode;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCustomDialogBinding itemBinding = ItemCustomDialogBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(itemBinding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.itemCodeBinding.textItemCode.setText(itemCodeList.get(position).itemCode);

        holder.itemView.setOnClickListener(view -> {
            listener.onItemClick(position, view);
        });

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemCustomDialogBinding itemCodeBinding;

        public ViewHolder(ItemCustomDialogBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemCodeBinding = itemBinding;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return itemCodeList.size();
    }
}