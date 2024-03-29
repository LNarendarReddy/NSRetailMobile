package com.nsretail.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.nsretail.R;
import com.nsretail.data.model.DispatchModel.DispatchDetail;
import com.nsretail.databinding.ItemStockEntryBinding;
import com.nsretail.ui.Interface.OnItemClickListener;
import com.nsretail.ui.activities.StockDispatch.StockDispatchActivity;
import com.nsretail.utils.NetworkStatus;

import java.util.ArrayList;

public class StockDispatchAdapter extends RecyclerView.Adapter<StockDispatchAdapter.ViewHolder> {

    private ArrayList<DispatchDetail> dispatchDetails;
    private final Context mContext;
    OnItemClickListener listener;

    public StockDispatchAdapter(Context context, ArrayList<DispatchDetail> dispatchDetailArrayList, OnItemClickListener listener) {
        this.mContext = context;
        this.dispatchDetails = dispatchDetailArrayList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemStockEntryBinding itemBinding = ItemStockEntryBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(itemBinding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.itemStockBinding.textSNO.setText("" + (position + 1) + ") ");

        holder.itemStockBinding.textStock.setText(dispatchDetails.get(position).itemCode
                + " " + dispatchDetails.get(position).itemName);

        if (dispatchDetails.get(position).isOpenItem) {
            holder.itemStockBinding.textPrice.setText("MRP: " + dispatchDetails.get(position).mrp
                    + ", W/Kgs: " + dispatchDetails.get(position).weightInKgs
                    + ", Tray No: " + dispatchDetails.get(position).trayNumber);
        } else {
            holder.itemStockBinding.textPrice.setText("MRP: " + dispatchDetails.get(position).mrp
                    + ", Qty: " + dispatchDetails.get(position).dispatchQuantity
                    + ", Tray No: " + dispatchDetails.get(position).trayNumber);
        }

        holder.itemStockBinding.imageDelete.setOnClickListener(view -> {
            if (NetworkStatus.getInstance(mContext).isConnected()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.AlertDialogCustom);
                builder.setMessage("Are you sure you want to delete?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", (dialog, id) -> {
                            ((StockDispatchActivity) mContext).deleteItem(position);
                            dialog.cancel();
                        }).setNegativeButton("No", (dialogInterface, i) -> dialogInterface.cancel());
                AlertDialog alert = builder.create();
                alert.show();
            } else
                Toast.makeText(mContext, "No internet connection", Toast.LENGTH_SHORT).show();
        });

        holder.itemView.setOnLongClickListener(view -> {

            ((StockDispatchActivity) mContext).updateItemData(position);

            return false;
        });

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemStockEntryBinding itemStockBinding;

        public ViewHolder(ItemStockEntryBinding itemBinding) {
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
        return dispatchDetails.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateList(ArrayList<DispatchDetail> filterCountingDetails) {
        dispatchDetails = filterCountingDetails;
        notifyDataSetChanged();
    }

}